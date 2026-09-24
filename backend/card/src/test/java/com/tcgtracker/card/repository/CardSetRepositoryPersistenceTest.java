package com.tcgtracker.card.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import com.tcgtracker.card.entity.CardSet.Language;
import com.tcgtracker.card.entity.CatalogueSource;
import com.tcgtracker.card.entity.CardGame;
import com.tcgtracker.card.entity.CardSet;

@DataJpaTest
@ContextConfiguration(
    classes = CardSetRepositoryPersistenceTest.JpaTestConfiguration.class
)
public class CardSetRepositoryPersistenceTest {
    @Configuration(proxyBeanMethods = false)
    @AutoConfigurationPackage(basePackages = "com.tcgtracker.card")
    @EntityScan(basePackageClasses = CardSet.class)
    @Import(CardSetRepository.class)
    static class JpaTestConfiguration {}

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CardSetRepository cardSetRepository;

    CardGame cardGame = new CardGame(CardGame.GameName.POKEMON);

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

    @Test
    void findByExternalIdTest() {
        entityManager.persist(cardGame);
        entityManager.persist(svBaseSet);
        entityManager.persist(paldeaEvolvedSet);

        entityManager.flush();
        entityManager.clear();

        String targetExternalId = paldeaEvolvedSet.getExternalId();
        Optional<CardSet> result = cardSetRepository.findByExternalId(targetExternalId);

        assertTrue(result.isPresent());
        assertEquals(targetExternalId, result.orElseThrow().getExternalId());
        assertTrue(cardSetRepository.findByExternalId("missing-id").isEmpty());
    }

    @Test
    void findBySetCodeTest() {
        entityManager.persist(cardGame);
        entityManager.persist(svBaseSet);
        entityManager.persist(paldeaEvolvedSet);

        entityManager.flush();
        entityManager.clear();

        String targetSetCode = paldeaEvolvedSet.getSetCode();
        Optional<CardSet> result = cardSetRepository.findBySetCode(targetSetCode);

        assertTrue(result.isPresent());
        assertEquals(targetSetCode, result.orElseThrow().getSetCode());
        assertTrue(cardSetRepository.findBySetCode("MISSING").isEmpty());
    }

    @Test
    void findByCardGameIdTest() {
        CardGame onePiece = new CardGame(CardGame.GameName.ONE_PIECE);
        CardSet onePieceSet = new CardSet(
            "op01-en",
            "OP01",
            "Romance Dawn",
            Language.ENGLISH,
            onePiece,
            LocalDate.of(2022, 12, 2),
            121,
            CatalogueSource.TCGDEX,
            null
        );

        entityManager.persist(cardGame);
        entityManager.persist(onePiece);
        entityManager.persist(svBaseSet);
        entityManager.persist(paldeaEvolvedSet);
        entityManager.persist(onePieceSet);

        entityManager.flush();
        entityManager.clear();

        Long targetCardGameId = cardGame.getId();

        List<CardSet> result = cardSetRepository.findByCardGameId(targetCardGameId);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(set -> set.getCardGame().getId().equals(targetCardGameId)));
        assertTrue(result.stream().noneMatch(set -> set.getId().equals(onePieceSet.getId())));
        assertTrue(cardSetRepository.findByCardGameId(Long.MAX_VALUE).isEmpty());
    }

    @Test
    void duplicateSetCodeIsRejected() {
        CardSet duplicateSetCode = new CardSet(
            "different-external-id",
            svBaseSet.getSetCode(),
            "Duplicate set code",
            Language.JAPANESE,
            cardGame,
            null,
            null,
            CatalogueSource.TCGDEX,
            null
        );

        entityManager.persist(cardGame);
        entityManager.persist(svBaseSet);

        assertThrows(PersistenceException.class, () -> {
            entityManager.persist(duplicateSetCode);
            entityManager.flush();
        });
    }

}
