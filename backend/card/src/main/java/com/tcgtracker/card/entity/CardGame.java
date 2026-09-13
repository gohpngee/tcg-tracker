package com.tcgtracker.card.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card_game")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter 
public class CardGame {
    public enum GameName {
        POKEMON,
        ONE_PIECE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private GameName gameName;

    //include constructor so that gameName cannot be null
    public CardGame(GameName gameName) {
        this.gameName = Objects.requireNonNull(gameName);
    }
}
