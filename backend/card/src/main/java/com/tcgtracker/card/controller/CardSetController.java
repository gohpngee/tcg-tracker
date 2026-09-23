package com.tcgtracker.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

import com.tcgtracker.card.entity.CardSet;
import com.tcgtracker.card.service.CatalogueQueryService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/card-sets")
@RequiredArgsConstructor 
@RestController
public class CardSetController {
    @Autowired 
    private final CatalogueQueryService catalogueQueryService;

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<CardSet>> getCardSets(@PathVariable Long gameId) {
        return ResponseEntity.ok(catalogueQueryService.getCardSets(gameId));
    }

    @GetMapping("/{setCode}")
    public ResponseEntity<Optional<CardSet>> getCardSetBySetCode(@PathVariable String setCode) {
        return ResponseEntity.ok(catalogueQueryService.getCardSetBySetCode(setCode));
    }

}
