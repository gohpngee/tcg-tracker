package com.tcgtracker.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.tcgtracker.card.entity.CardGame;
import com.tcgtracker.card.entity.CardSet;
import com.tcgtracker.card.entity.Card;
import com.tcgtracker.card.service.CatalogueQueryService;

@RequiredArgsConstructor 
@RestController 
public class CardGameController {
    @Autowired
    private final CatalogueQueryService catalogueQueryService;

    @GetMapping("/card-games")
    public ResponseEntity<List<CardGame>> getCardGames() {
        return ResponseEntity.ok(catalogueQueryService.getCardGames());
    }

}
