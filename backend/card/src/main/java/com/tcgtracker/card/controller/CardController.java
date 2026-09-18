package com.tcgtracker.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import lombok.RequiredArgsConstructor;

import com.tcgtracker.card.entity.Card;
import com.tcgtracker.card.service.CatalogueQueryService;

@RequiredArgsConstructor
@RestController
public class CardController {
    @Autowired 
    private final CatalogueQueryService catalogueQueryService;

    @GetMapping("/{cardSetCode}/{cardNumber}")
    public ResponseEntity<Optional<Card>> getCard(@PathVariable String cardSetCode, @PathVariable String cardNumber) {
        return ResponseEntity.ok(catalogueQueryService.getCardByCardSetCodeAndCardNumber(cardSetCode, cardNumber));
    }
}
