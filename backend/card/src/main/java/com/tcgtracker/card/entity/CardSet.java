package com.tcgtracker.card.entity;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card_set")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardSet {
    public enum Language {
        ENGLISH,
        JAPANESE,
        CHINESE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    //for external source API's id
    @Column(name = "external_id", nullable = false, unique = true)
    private String externalId;

    @Column(name = "set_code", nullable = false, unique = true)
    private String setCode;
    
    @Column(nullable = false, unique = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false, unique = false)
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_game_id", nullable = false)
    private CardGame cardGame;

    @Column(nullable = true, unique = false)
    private LocalDate releaseDate;

    @Column(name = "total_cards", nullable = true, unique = false)
    private Integer totalCards;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private CatalogueSource source;

    @Column(nullable = true, unique = false)
    private String logoUrl;
    
    public CardSet(String externalId, String setCode, String name, Language language, CardGame cardGame, LocalDate releaseDate, Integer totalCards, CatalogueSource source, String logoUrl) {
        this.externalId = Objects.requireNonNull(externalId);
        this.setCode = Objects.requireNonNull(setCode);
        this.name = Objects.requireNonNull(name);
        this.language = Objects.requireNonNull(language);
        this.cardGame = Objects.requireNonNull(cardGame);
        this.releaseDate = releaseDate;
        this.totalCards = totalCards;
        this.source = Objects.requireNonNull(source);
        this.logoUrl = logoUrl;
    }

    //for updating the information about the set later on, using a custom method instead of a setter
    public void updateMetadata(LocalDate releaseDate, Integer totalCards, String logoUrl){
        this.releaseDate = releaseDate;
        this.totalCards = totalCards;
        this.logoUrl = logoUrl;
    }
}
