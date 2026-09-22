package com.tcgtracker.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.List;
import lombok.RequiredArgsConstructor;

import com.tcgtracker.card.entity.Card;
import com.tcgtracker.card.service.CatalogueQueryService;

@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@RestController
public class CardController {
    @Autowired 
    private final CatalogueQueryService catalogueQueryService;

    @GetMapping("/{cardSetCode}/{cardNumber}")
    public ResponseEntity<Optional<Card>> getCard(@PathVariable String cardSetCode, @PathVariable String cardNumber) {
        return ResponseEntity.ok(catalogueQueryService.getCardByCardSetCodeAndCardNumber(cardSetCode, cardNumber));
    }

    @GetMapping("/{cardSetCode}")
    public ResponseEntity<List<Card>> getCardsByCardSetCode(@PathVariable String cardSetCode) {
        return ResponseEntity.ok(catalogueQueryService.getCardsBySetCode(cardSetCode));
    }

}
