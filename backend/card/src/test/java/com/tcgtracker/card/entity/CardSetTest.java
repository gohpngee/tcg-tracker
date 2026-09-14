package com.tcgtracker.card.entity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import com.tcgtracker.card.entity.CardSet.Language;

public class CardSetTest {
    @Test 
    void updateMetadataDoesNotChangeOtherFieldsTest() {
        CardGame cardGame = new CardGame(CardGame.GameName.POKEMON);
        CardSet cardset = new CardSet(
            "sv01",
            "SV01",
            "Scarlet & Violet Base",
            Language.ENGLISH,
            cardGame,
            LocalDate.of(2023, 3, 30),
            257,
            CatalogueSource.TCGDEX,
            "https://www.tcgdex.net/images/sets/sv02.png"
        );

        LocalDate updatedReleaseDate = LocalDate.of(2023, 3, 31);
        Integer updatedTotalCards = 258;
        String updatedLogoUrl = "https://www.tcgdex.net/images/sets/sv01.png";

        cardset.updateMetadata(updatedReleaseDate, updatedTotalCards, updatedLogoUrl);

        assertEquals("sv01", cardset.getExternalId());
        assertEquals(CatalogueSource.TCGDEX, cardset.getSource());

        assertEquals(updatedReleaseDate, cardset.getReleaseDate());
        assertEquals(updatedTotalCards,cardset.getTotalCards());
        assertEquals(updatedLogoUrl, cardset.getLogoUrl());

        assertEquals("SV01", cardset.getSetCode());
        assertEquals("Scarlet & Violet Base", cardset.getName());
        assertEquals(Language.ENGLISH, cardset.getLanguage());
        assertEquals(cardGame, cardset.getCardGame());
    }

    @Test
    void newCardSetIdNullTest() {
        CardGame cardGame = new CardGame(CardGame.GameName.POKEMON);
        CardSet cardset = new CardSet(
            "sv01",
            "SV01",
            "Scarlet & Violet Base",
            Language.ENGLISH,
            cardGame,
            LocalDate.of(2023, 3, 31),
            258,
            CatalogueSource.TCGDEX,
            "https://www.tcgdex.net/images/sets/sv01.png"
        );

        assertNull(cardset.getId());
    }
}
