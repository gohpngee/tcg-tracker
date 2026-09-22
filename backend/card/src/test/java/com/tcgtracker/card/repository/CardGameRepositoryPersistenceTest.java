package com.tcgtracker.card.repository;

import jakarta.persistence.EntityManager;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.tcgtracker.card.entity.CardGame;

@DataJpaTest
@ContextConfiguration(
    classes = CardGameRepositoryPersistenceTest.JpaTestConfiguration.class
)
public class CardGameRepositoryPersistenceTest {
    @Configuration(proxyBeanMethods = false)
    @AutoConfigurationPackage(basePackages = "com.tcgtracker.card")
    @EntityScan(basePackageClasses = CardGame.class)
    @Import(CardGameRepository.class)
    static class JpaTestConfiguration {}

    @Autowired
    private EntityManager entityManager;

    @Autowired 
    private CardGameRepository cardGameRepository;

    @Test
    void findAllTest() {
        CardGame cardGame1 = new CardGame(CardGame.GameName.POKEMON);
        CardGame cardGame2 = new CardGame(CardGame.GameName.ONE_PIECE);

        entityManager.persist(cardGame1);
        entityManager.persist(cardGame2);

        entityManager.flush();
        entityManager.clear();

        List<CardGame> cardGames = cardGameRepository.findAll();

        assertEquals(2, cardGames.size());
        //checking Pokemon for index 1 because sorted alphabetically asc order
        assertEquals(cardGame1.getId(), cardGames.get(1).getId());
    }

    @Test
    void existsByGameNameReturnsTrueWhenGameExists() {
        CardGame pokemon = new CardGame(CardGame.GameName.POKEMON);

        entityManager.persist(pokemon);
        entityManager.flush();
        entityManager.clear();

        assertTrue(
            cardGameRepository.existsByGameName(CardGame.GameName.POKEMON)
        );
    }

    @Test
    void existsByGameNameReturnsFalseWhenGameDoesNotExist() {
        CardGame pokemon = new CardGame(CardGame.GameName.POKEMON);

        entityManager.persist(pokemon);
        entityManager.flush();
        entityManager.clear();

        assertFalse(
            cardGameRepository.existsByGameName(CardGame.GameName.ONE_PIECE)
        );
    }
}
