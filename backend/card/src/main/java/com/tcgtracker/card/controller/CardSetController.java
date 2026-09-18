package com.tcgtracker.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import java.util.List;

import com.tcgtracker.card.entity.CardSet;
import com.tcgtracker.card.entity.Card;
import com.tcgtracker.card.service.CatalogueQueryService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@RestController
public class CardSetController {
    @Autowired 
    private final CatalogueQueryService catalogueQueryService;

    @GetMapping("/card-sets/{gameId}")
    public ResponseEntity<List<CardSet>> getCardSets(@PathVariable Long gameId) {
        return ResponseEntity.ok(catalogueQueryService.getCardSets(gameId));
    }

    @GetMapping("/cards/{setCode}")
    public ResponseEntity<List<Card>> getCardsBySetCode(@PathVariable String setCode) {
        return ResponseEntity.ok(catalogueQueryService.getCardsBySetCode(setCode));
    }
}
