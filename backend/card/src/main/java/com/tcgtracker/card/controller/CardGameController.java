package com.tcgtracker.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

import com.tcgtracker.card.entity.CardGame;
import com.tcgtracker.card.service.CatalogueQueryService;

@RequestMapping("/api/v1")
@RequiredArgsConstructor 
@RestController 
public class CardGameController {
    @Autowired
    private final CatalogueQueryService catalogueQueryService;

    @GetMapping("/card-games")
    public ResponseEntity<List<CardGame>> getCardGames() {
        return ResponseEntity.ok(catalogueQueryService.getCardGames());
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByGameName(@RequestParam String gameName) {
        return ResponseEntity.ok(catalogueQueryService.existsByGameName(CardGame.GameName.valueOf(gameName)));
    }

}
