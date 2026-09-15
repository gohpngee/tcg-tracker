package com.tcgtracker.card.repository;

import jakarta.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.tcgtracker.card.entity.Card;

@DataJpaTest
@ContextConfiguration(
    classes = CardRepositoryPersistenceTest.JpaTestConfiguration.class
)
public class CardRepositoryPersistenceTest {
    @Configuration(proxyBeanMethods = false)
    @EntityScan(basePackageClasses = Card.class)
    @Import(CardRepository.class)
    static class JpaTestConfiguration {}

    @Autowired
    private EntityManager entityManager;

    @Autowired 
    private CardRepository cardRepository;
}
