package com.tcgtracker.card.repository;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import java.time.LocalDate;
import java.util.List;

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

    @Test
    void findByCardSetIdTest() {
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

        Card mausholdIRPE = new Card(
            "226", "226/193", "Maushold", "Illustration Rare", paldeaEvolvedSet, null);
        
        Card gardevoirSIRSV = new Card(
            "245", "245/198", "Gardevoir ex", "Secret Illustration Rare", svBaseSet, null);

        entityManager.persist(cardGame);
        entityManager.persist(svBaseSet);
        entityManager.persist(paldeaEvolvedSet);
        entityManager.persist(mausholdIRPE);
        entityManager.persist(gardevoirSIRSV);

        Long targetSetId = svBaseSet.getId();
        Long targetCardId = gardevoirSIRSV.getId();

        entityManager.flush();
        entityManager.clear();

        List<Card> results = cardRepository.findByCardSetId(targetSetId);

        assertEquals(1, results.size());
        assertEquals(targetCardId, results.get(0).getId());
        //get the card item, then check it's cardSet attribute and retrieve the id
        assertEquals(targetSetId, results.get(0).getCardSet().getId());
    }

}
