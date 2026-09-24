package com.tcgtracker.card.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.tcgtracker.card.entity.CardGame;
import com.tcgtracker.card.entity.CardSet;
import com.tcgtracker.card.entity.CardSet.Language;
import com.tcgtracker.card.entity.CatalogueSource;

import com.tcgtracker.card.entity.Card;

@DataJpaTest
@ContextConfiguration(
    classes = CardRepositoryPersistenceTest.JpaTestConfiguration.class
)
public class CardRepositoryPersistenceTest {
    @Configuration(proxyBeanMethods = false)
    @AutoConfigurationPackage(basePackages = "com.tcgtracker.card")
    @EntityScan(basePackageClasses = Card.class)
    @Import(CardRepository.class)
    static class JpaTestConfiguration {}

    @Autowired
    private EntityManager entityManager;

    @Autowired 
    private CardRepository cardRepository;

    CardGame cardGame = new CardGame(CardGame.GameName.POKEMON);

    @Test
    void findByCardSetIdTest() {
        CardSet svBaseSet = new CardSet(
            "sv01",
            "SV01",
            "Scarlet & Violet Base",
            Language.ENGLISH,
            cardGame,
            LocalDate.of(2023, 3, 31),
            258,
            CatalogueSource.TCGDEX,
            "https://www.tcgdex.net/images/sets/sv01.png"
        );

        CardSet paldeaEvolvedSet = new CardSet(
            "sv02",
            "SV02",
            "Paldea Evolved",
            Language.ENGLISH,
            cardGame,
            LocalDate.of(2023, 06, 9),
            279,
            CatalogueSource.TCGDEX,
            "https://www.tcgdex.net/images/sets/sv02.png"
        );

        Card mausholdIRPE = new Card(
            "226", "226/193", "Maushold", "Illustration Rare", paldeaEvolvedSet, null);
        
        Card gardevoirSIRSV = new Card(
            "245", "245/198", "Gardevoir ex", "Secret Illustration Rare", svBaseSet, null);

        Card spidopsIRSV = new Card(
            "218", "218/198", "Spidops ex", "Illustration Rare", svBaseSet, null);

        entityManager.persist(cardGame);
        entityManager.persist(svBaseSet);
        entityManager.persist(paldeaEvolvedSet);
        entityManager.persist(mausholdIRPE);
        entityManager.persist(gardevoirSIRSV);
        entityManager.persist(spidopsIRSV);

        Long targetSetId = svBaseSet.getId();
        Long targetCardId = gardevoirSIRSV.getId();

        entityManager.flush();
        entityManager.clear();

        List<Card> results = cardRepository.findByCardSetId(targetSetId);

        assertEquals(2, results.size());
        assertEquals(
            List.of("218/198", "245/198"),
            results.stream().map(Card::getCardNumber).toList()
        );
        assertEquals(targetSetId, results.get(0).getCardSet().getId());
        assertEquals(targetCardId, results.get(1).getId());
        assertTrue(cardRepository.findByCardSetId(Long.MAX_VALUE).isEmpty());
    }

    @Test 
    void findByCardIdTest() {
        CardSet paldeaEvolvedSet = new CardSet(
            "sv02",
            "SV02",
            "Paldea Evolved",
            Language.ENGLISH,
            cardGame,
            LocalDate.of(2023, 06, 9),
            279,
            CatalogueSource.TCGDEX,
            "https://www.tcgdex.net/images/sets/sv02.png"
        );

        Card mausholdIRPE = new Card(
            "226", "226/193", "Maushold", "Illustration Rare", paldeaEvolvedSet, null);

        entityManager.persist(cardGame);
        entityManager.persist(paldeaEvolvedSet);
        entityManager.persist(mausholdIRPE);

        entityManager.flush();
        entityManager.clear();

        Long targetCardId = mausholdIRPE.getId();

        Optional<Card> result = cardRepository.findById(targetCardId);

        assertTrue(result.isPresent());
        assertEquals(targetCardId, result.orElseThrow().getId());
        assertTrue(cardRepository.findById(Long.MAX_VALUE).isEmpty());
    }

    @Test
    void findByExternalIdTest() {
        CardSet svBaseSet = new CardSet(
            "TCGDEX:sv01",
            "SV01",
            "Scarlet & Violet Base",
            Language.ENGLISH,
            cardGame,
            LocalDate.of(2023, 3, 31),
            258,
            CatalogueSource.TCGDEX,
            null
        );

        Card gardevoir = new Card(
            "TCGDEX:sv01-245",
            "245/198",
            "Gardevoir ex",
            "Secret Illustration Rare",
            svBaseSet,
            null
        );

        entityManager.persist(cardGame);
        entityManager.persist(svBaseSet);
        entityManager.persist(gardevoir);

        Long targetCardId = gardevoir.getId();
        String targetExternalId = gardevoir.getExternalId();

        entityManager.flush();
        entityManager.clear();

        Optional<Card> result = cardRepository.findByExternalId(targetExternalId);

        assertTrue(result.isPresent());
        assertEquals(targetCardId, result.orElseThrow().getId());
        assertTrue(cardRepository.findByExternalId("missing-external-id").isEmpty());
    }

    @Test
    void findByCardSetCodeAndCardNumberReturnsAllPrintings() {
        CardGame onePiece = new CardGame(CardGame.GameName.ONE_PIECE);
        CardSet royalBlood = new CardSet(
            "CINCAITCG:OP10",
            "OP10-jp",
            "Royal Blood",
            Language.JAPANESE,
            onePiece,
            null,
            null,
            CatalogueSource.CINCAITCG,
            null
        );
        CardSet anotherSet = new CardSet(
            "CINCAITCG:OP11",
            "OP11-jp",
            "A Fist of Divine Speed",
            Language.JAPANESE,
            onePiece,
            null,
            null,
            CatalogueSource.CINCAITCG,
            null
        );

        Card regularPrinting = new Card(
            "CINCAITCG:https://example.test/cards/op10-119-regular",
            "OP10-119",
            "Trafalgar Law",
            "SEC",
            royalBlood,
            null
        );
        Card parallelPrinting = new Card(
            "CINCAITCG:https://example.test/cards/op10-119-parallel",
            "OP10-119",
            "Trafalgar Law (Parallel)",
            "P-SEC",
            royalBlood,
            null
        );
        Card differentCardNumber = new Card(
            "CINCAITCG:https://example.test/cards/op10-120",
            "OP10-120",
            "Other Card",
            "R",
            royalBlood,
            null
        );
        Card sameNumberInAnotherSet = new Card(
            "CINCAITCG:https://example.test/cards/op11-119",
            "OP10-119",
            "Different Set Card",
            "R",
            anotherSet,
            null
        );

        entityManager.persist(onePiece);
        entityManager.persist(royalBlood);
        entityManager.persist(anotherSet);
        entityManager.persist(regularPrinting);
        entityManager.persist(parallelPrinting);
        entityManager.persist(differentCardNumber);
        entityManager.persist(sameNumberInAnotherSet);

        entityManager.flush();
        entityManager.clear();

        List<Card> results = cardRepository.findByCardSetCodeAndCardNumber(
            "OP10-jp",
            "OP10-119"
        );

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(card -> card.getCardSet().getSetCode().equals("OP10-jp")));
        assertTrue(results.stream().allMatch(card -> card.getCardNumber().equals("OP10-119")));
        assertTrue(results.stream().anyMatch(card ->
            card.getExternalId().equals("CINCAITCG:https://example.test/cards/op10-119-regular")
        ));
        assertTrue(results.stream().anyMatch(card ->
            card.getExternalId().equals("CINCAITCG:https://example.test/cards/op10-119-parallel")
        ));
        assertTrue(cardRepository.findByCardSetCodeAndCardNumber("OP10-jp", "MISSING").isEmpty());
    }

    @Test
    void findBySetCodeTest() {
        CardSet svBaseSet = new CardSet(
            "TCGDEX:sv01",
            "SV01",
            "Scarlet & Violet Base",
            Language.ENGLISH,
            cardGame,
            LocalDate.of(2023, 3, 31),
            258,
            CatalogueSource.TCGDEX,
            null
        );
        CardSet paldeaEvolvedSet = new CardSet(
            "TCGDEX:sv02",
            "SV02",
            "Paldea Evolved",
            Language.ENGLISH,
            cardGame,
            LocalDate.of(2023, 6, 9),
            279,
            CatalogueSource.TCGDEX,
            null
        );

        Card firstSvCard = new Card(
            "TCGDEX:sv01-001", "001/198", "Sprigatito", "Common", svBaseSet, null);
        Card secondSvCard = new Card(
            "TCGDEX:sv01-245", "245/198", "Gardevoir ex", "SIR", svBaseSet, null);
        Card otherSetCard = new Card(
            "TCGDEX:sv02-001", "001/193", "Other Card", "Common", paldeaEvolvedSet, null);

        entityManager.persist(cardGame);
        entityManager.persist(svBaseSet);
        entityManager.persist(paldeaEvolvedSet);
        entityManager.persist(firstSvCard);
        entityManager.persist(secondSvCard);
        entityManager.persist(otherSetCard);

        entityManager.flush();
        entityManager.clear();

        List<Card> results = cardRepository.findBySetCode("SV01");

        assertEquals(2, results.size());
        assertEquals(
            List.of("001/198", "245/198"),
            results.stream().map(Card::getCardNumber).toList()
        );
        assertTrue(results.stream().allMatch(card -> card.getCardSet().getSetCode().equals("SV01")));
        assertTrue(cardRepository.findBySetCode("MISSING").isEmpty());
    }

    @Test
    void duplicateExternalIdIsRejected() {
        CardSet svBaseSet = new CardSet(
            "TCGDEX:sv01",
            "SV01",
            "Scarlet & Violet Base",
            Language.ENGLISH,
            cardGame,
            LocalDate.of(2023, 3, 31),
            258,
            CatalogueSource.TCGDEX,
            null
        );
        Card firstPrinting = new Card(
            "TCGDEX:sv01-245", "245/198", "Gardevoir ex", "SIR", svBaseSet, null);
        Card duplicateExternalId = new Card(
            "TCGDEX:sv01-245", "245/198", "Gardevoir ex (Parallel)", "P-SIR", svBaseSet, null);

        entityManager.persist(cardGame);
        entityManager.persist(svBaseSet);
        entityManager.persist(firstPrinting);

        assertThrows(PersistenceException.class, () -> {
            entityManager.persist(duplicateExternalId);
            entityManager.flush();
        });
    }
}
