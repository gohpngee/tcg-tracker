package com.tcgtracker.card.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;

import com.tcgtracker.card.entity.CardGame;

@Repository 
public class CardGameRepository {
    private final EntityManager entityManager;

    public CardGameRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<CardGame> findAll() {
        return entityManager.createQuery("""
                SELECT cg FROM CardGame cg
                ORDER BY cg.gameName ASC
                """, CardGame.class)
                .getResultList();
    }

    public Boolean existsByGameName(CardGame.GameName gameName) {
        Long gameCount = entityManager.createQuery("""
                SELECT COUNT(cg) FROM CardGame cg
                WHERE cg.gameName = :gameName
                """, Long.class)
                .setParameter("gameName", gameName)
                .getSingleResult();
            
        return gameCount > 0;
    }

}
