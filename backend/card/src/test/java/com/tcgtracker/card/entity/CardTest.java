package com.tcgtracker.card.entity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

import com.tcgtracker.card.entity.CardGame.GameName;
import com.tcgtracker.card.entity.CardSet.Language;

public class CardTest {
    CardGame game = new CardGame(GameName.POKEMON);
    CardSet cardset = new CardSet(
        "sv01",
        "SV01",
        "Scarlet & Violet Base",
        Language.ENGLISH,
        game,
        LocalDate.of(2023, 3, 31),
        258,
        CatalogueSource.TCGDEX,
        "https://www.tcgdex.net/images/sets/sv01.png"
    );

    @Test 
    void newCardCreationTest() {
        Card bulbasaur = new Card(
            "1",
            "1",
            "Bulbasaur",
            "Common",
            cardset,
            "https://www.tcgdex.net/images/cards/1.png"
        );

        assertEquals("1", bulbasaur.getExternalId());
        assertEquals("1", bulbasaur.getCardNumber());
        assertEquals("Bulbasaur", bulbasaur.getName());
        assertEquals("Common", bulbasaur.getRarity());
        assertSame(cardset, bulbasaur.getCardSet());
        assertEquals("https://www.tcgdex.net/images/cards/1.png", bulbasaur.getImageUrl());
        assertEquals(CatalogueSource.TCGDEX, bulbasaur.getCardSet().getSource());
    }

    @Test
    void newCardIdNullTest() {
        Card bulbasaur = new Card(
            "1",
            "1",
            "Bulbasaur",
            "Common",
            cardset,
            "https://www.tcgdex.net/images/cards/1.png"
        );

        assertNull(bulbasaur.getId());
    }
}
