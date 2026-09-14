package com.tcgtracker.card.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "card")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Card {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //external id in provider API
    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rarity")
    private String rarity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_set_id", nullable = false)
    private CardSet cardSet;

    @Column(name = "image_url")
    private String imageUrl;

    public Card(String externalId, String cardNumber, String name, String rarity, CardSet cardSet, String imageUrl) {
        this.externalId = Objects.requireNonNull(externalId);
        this.cardNumber = Objects.requireNonNull(cardNumber);
        this.name = Objects.requireNonNull(name);
        this.rarity = rarity;
        this.cardSet = Objects.requireNonNull(cardSet);
        this.imageUrl = imageUrl;
    }
}
