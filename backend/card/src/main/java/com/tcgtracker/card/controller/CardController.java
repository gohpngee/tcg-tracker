package com.tcgtracker.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

import com.tcgtracker.card.entity.Card;
import com.tcgtracker.card.service.CatalogueQueryService;

@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@RestController
public class CardController {
    @Autowired 
    private final CatalogueQueryService catalogueQueryService;

    //using Request Param instead of Path Variable, to handle for input that might have special characters like slash /
    //endpoint will look like /api/v1/cards?cardSetCode=cardSetCode&cardNumber=cardNumber instead of the path directly
    @GetMapping("/{cardSetCode}")
    public ResponseEntity<List<Card>> getCard(@PathVariable String cardSetCode, @RequestParam String cardNumber) {
        return ResponseEntity.ok(catalogueQueryService.getCardByCardSetCodeAndCardNumber(cardSetCode, cardNumber));
    }

    @GetMapping("/id/{cardId}")
    public ResponseEntity<Optional<Card>> getCardById(@PathVariable Long cardId) {
        return ResponseEntity.ok(catalogueQueryService.getCardById(cardId));
    }

    @GetMapping("/set/{cardSetCode}")
    public ResponseEntity<List<Card>> getCardsByCardSetCode(@PathVariable String cardSetCode) {
        return ResponseEntity.ok(catalogueQueryService.getCardsBySetCode(cardSetCode));
    }

}
