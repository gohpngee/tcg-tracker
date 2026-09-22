package com.tcgtracker.card.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

import jakarta.persistence.EntityManager;

import com.tcgtracker.card.entity.Card;
import com.tcgtracker.card.entity.CardSet;
import com.tcgtracker.card.entity.CardGame;

@Repository 
public class CardSetRepository {
    private final EntityManager entityManager;

    public CardSetRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public Optional<CardSet> findByExternalId(String externalId) {
        //returning Optional.ofNullable because we are returning an Optional CardSet object, so need to use a wrapper
        return Optional.ofNullable(entityManager.createQuery("""
                SELECT cs FROM CardSet cs
                WHERE cs.externalId = :externalId   
                """, CardSet.class)
                .setParameter("externalId", externalId)
                .getSingleResultOrNull());
    }

    public List<CardSet> findBySetCode(String setCode) {
        return entityManager.createQuery("""
                SELECT cs FROM CardSet cs
                WHERE cs.setCode = :setCode
                """, CardSet.class)
                .setParameter("setCode", setCode)
                .getResultList();
    }

    public List<CardSet> findByCardGameId(Long cardGameId) {
        return entityManager.createQuery("""
                SELECT cs FROM CardSet cs
                WHERE cs.cardGame.id = :cardGameId
                """, CardSet.class)
                .setParameter("cardGameId", cardGameId)
                .getResultList();
    }

}
