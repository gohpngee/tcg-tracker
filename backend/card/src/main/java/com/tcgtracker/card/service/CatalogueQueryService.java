package com.tcgtracker.card.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.tcgtracker.card.entity.CardGame;
import com.tcgtracker.card.entity.CardSet;
import com.tcgtracker.card.entity.Card;

import com.tcgtracker.card.repository.CardGameRepository;
import com.tcgtracker.card.repository.CardSetRepository;
import com.tcgtracker.card.repository.CardRepository;

@RequiredArgsConstructor 
@Service 
public class CatalogueQueryService {
    private final CardGameRepository cardGameRepository;
    private final CardSetRepository cardSetRepository;
    private final CardRepository cardRepository;

    public List<CardGame> getCardGames() {
        return cardGameRepository.findAll();
    }

    public List<CardSet> getCardSets(Long gameId) {
        return cardSetRepository.findByCardGameId(gameId);
    }

    public Optional<CardSet> getCardSetBySetCode(String setCode) {
        return cardSetRepository.findBySetCode(setCode);
    }

    public Optional<Card> getCardById(Long cardId) {
        return cardRepository.findById(cardId);
    }

    public List<Card> getCardsBySetCode(String setCode) {
        return cardRepository.findBySetCode(setCode);
    }

    public List<Card> getCardByCardSetCodeAndCardNumber(String cardSetCode, String cardNumber) {
        return cardRepository.findByCardSetCodeAndCardNumber(cardSetCode, cardNumber);
    }

    public Boolean existsByGameName(CardGame.GameName gameName) {
        return cardGameRepository.existsByGameName(gameName);
    }
}
