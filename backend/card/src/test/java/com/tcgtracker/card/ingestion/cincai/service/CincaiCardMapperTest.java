package com.tcgtracker.card.ingestion.cincai.service;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.tcgtracker.card.entity.Card;
import com.tcgtracker.card.entity.CardGame;
import com.tcgtracker.card.entity.CardSet;
import com.tcgtracker.card.entity.CardSet.Language;
import com.tcgtracker.card.entity.CatalogueSource;
import com.tcgtracker.card.ingestion.ExternalIdFactory;
import com.tcgtracker.card.ingestion.cincai.dto.CincaiCardDto;
import com.tcgtracker.card.ingestion.cincai.dto.CincaiQueryResponseDto;
import com.tcgtracker.common.exception.classes.NoCardsFoundException;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CincaiCardMapperTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CincaiCardMapper mapper = new CincaiCardMapper();

    @Test
    void deserializesCincaiPayloadAndMapsCards() throws JacksonException {
        CincaiQueryResponseDto response = readResponse("""
            {
              "searchWord": "OP10",
              "count": 2,
              "ignoredByDto": "this proves unknown fields are ignored",
              "results": [
                {
                  "name": "Trafalgar Law",
                  "cardNumber": "OP10-119",
                  "rarity": "SEC",
                  "price": "320 円",
                  "set": "Royal Blood",
                  "image": "https://example.test/images/op10-119.jpg",
                  "link": "https://example.test/cards/op10-119-regular"
                },
                {
                  "name": "Trafalgar Law (Parallel)",
                  "cardNumber": "OP10-119",
                  "rarity": "P-SEC",
                  "set": "Royal Blood",
                  "image": "https://example.test/images/op10-119-parallel.jpg",
                  "link": "https://example.test/cards/op10-119-parallel"
                }
              ]
            }
            """);
        CardSet cardSet = cardSet();

        assertEquals("OP10", response.getSearchWord());
        assertEquals(2, response.getCount());
        assertEquals(2, response.getResults().size());

        CincaiCardDto firstDto = response.getResults().get(0);
        assertEquals("Royal Blood", firstDto.getSetName());
        assertEquals("https://example.test/images/op10-119.jpg", firstDto.getImageUrl());
        assertEquals("https://example.test/cards/op10-119-regular", firstDto.getSourceUrl());

        assertEquals("Royal Blood", mapper.extractSetName(response));

        List<Card> cards = mapper.toCards(response, cardSet);

        assertEquals(2, cards.size());
        assertEquals(
            List.of("OP10-119", "OP10-119"),
            cards.stream().map(Card::getCardNumber).toList()
        );
        assertEquals(
            List.of("Trafalgar Law", "Trafalgar Law (Parallel)"),
            cards.stream().map(Card::getName).toList()
        );
        assertEquals(
            List.of("SEC", "P-SEC"),
            cards.stream().map(Card::getRarity).toList()
        );
        assertEquals(
            List.of(
                ExternalIdFactory.from(
                    CatalogueSource.CINCAITCG,
                    "https://example.test/cards/op10-119-regular"
                ),
                ExternalIdFactory.from(
                    CatalogueSource.CINCAITCG,
                    "https://example.test/cards/op10-119-parallel"
                )
            ),
            cards.stream().map(Card::getExternalId).toList()
        );
        assertEquals(
            List.of(
                "https://example.test/images/op10-119.jpg",
                "https://example.test/images/op10-119-parallel.jpg"
            ),
            cards.stream().map(Card::getImageUrl).toList()
        );
        assertTrue(cards.stream().allMatch(card -> card.getCardSet() == cardSet));
        assertSame(cardSet, cards.get(0).getCardSet());
    }

    @Test
    void rejectsNullOrEmptyResults() throws JacksonException {
        CardSet cardSet = cardSet();
        CincaiQueryResponseDto emptyResults = readResponse("""
            { "searchWord": "OP10", "count": 0, "results": [] }
            """);
        CincaiQueryResponseDto nullResults = readResponse("""
            { "searchWord": "OP10", "count": 0, "results": null }
            """);

        assertThrows(NoCardsFoundException.class, () -> mapper.extractSetName(null));
        assertThrows(NoCardsFoundException.class, () -> mapper.toCards(null, cardSet));
        assertThrows(NoCardsFoundException.class, () -> mapper.extractSetName(emptyResults));
        assertThrows(NoCardsFoundException.class, () -> mapper.toCards(emptyResults, cardSet));
        assertThrows(NoCardsFoundException.class, () -> mapper.extractSetName(nullResults));
        assertThrows(NoCardsFoundException.class, () -> mapper.toCards(nullResults, cardSet));
    }

    @Test
    void rejectsNullMappingArguments() throws JacksonException {
        CincaiQueryResponseDto response = readResponse("""
            {
              "searchWord": "OP10",
              "count": 1,
              "results": [
                {
                  "name": "Trafalgar Law",
                  "cardNumber": "OP10-119",
                  "rarity": "SEC",
                  "set": "Royal Blood",
                  "image": "https://example.test/images/op10-119.jpg",
                  "link": "https://example.test/cards/op10-119-regular"
                }
              ]
            }
            """);
        CardSet cardSet = cardSet();

        assertThrows(IllegalArgumentException.class, () -> mapper.toCard(null, cardSet));
        assertThrows(IllegalArgumentException.class, () -> mapper.toCard(response.getResults().get(0), null));
        assertThrows(IllegalArgumentException.class, () -> mapper.toCards(response, null));
    }

    private CincaiQueryResponseDto readResponse(String json) throws JacksonException {
        return objectMapper.readValue(json, CincaiQueryResponseDto.class);
    }

    private CardSet cardSet() {
        return new CardSet(
            "CINCAITCG:OP10",
            "OP10-jp",
            "Royal Blood",
            Language.JAPANESE,
            new CardGame(CardGame.GameName.ONE_PIECE),
            null,
            null,
            CatalogueSource.CINCAITCG,
            null
        );
    }
}
