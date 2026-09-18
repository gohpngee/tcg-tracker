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

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import jakarta.persistence.EntityManager;

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

        String targetExternalId = paldeaEvolvedSet.getExternalId();
        Optional<CardSet> result = cardSetRepository.findByExternalId(targetExternalId);

        assertEquals(targetExternalId, result.get().getExternalId());
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

        assertEquals(targetSetCode, result.get().getSetCode());
    }

    @Test
    void findByCardGameIdTest() {
        entityManager.persist(cardGame);
        entityManager.persist(svBaseSet);
        entityManager.persist(paldeaEvolvedSet);

        entityManager.flush();
        entityManager.clear();

        Long targetCardGameId = cardGame.getId();

        List<CardSet> result = cardSetRepository.findByCardGameId(targetCardGameId);

        assertEquals(2, result.size());
    }

}
