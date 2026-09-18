package com.tcgtracker.card.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tcgtracker.card.entity.Card;

import jakarta.persistence.EntityManager;

@Repository
public class CardRepository {
    private final EntityManager entityManager;

    public CardRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<Card> findById(Long id) {
        return Optional.ofNullable(
            entityManager.find(Card.class, id)
        );
    }

    public Optional<Card> findByCardSetCodeAndCardNumber(String cardSetCode, String cardNumber) {
        return Optional.ofNullable(
            entityManager.createQuery("""
                SELECT c FROM Card c 
                WHERE c.cardSet.setCode = :cardSetCode AND c.cardNumber = :cardNumber
                """, Card.class)
                .setParameter("cardSetCode", cardSetCode)
                .setParameter("cardNumber", cardNumber)
                .getSingleResultOrNull());
    }

    public List<Card> findByCardSetId(Long cardSetId) {
        return entityManager.createQuery("""
                SELECT c FROM Card c 
                WHERE c.cardSet.id = :cardSetId
                ORDER BY c.cardNumber ASC
                """, Card.class)
                .setParameter("cardSetId", cardSetId)
                .getResultList();
    }

}
