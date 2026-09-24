package com.tcgtracker.card.ingestion.cincai.service;


import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

import com.tcgtracker.card.ingestion.cincai.dto.CincaiCardDto;
import com.tcgtracker.card.ingestion.cincai.dto.CincaiQueryResponseDto;

import com.tcgtracker.card.entity.Card;
import com.tcgtracker.card.entity.CardSet;
import com.tcgtracker.card.entity.CatalogueSource;
import com.tcgtracker.card.ingestion.ExternalIdFactory;

import com.tcgtracker.common.exception.classes.NoCardsFoundException;

@Component
public class CincaiCardMapper {
    public String extractSetName(CincaiQueryResponseDto response) {
        if (Objects.isNull(response) || Objects.isNull(response.getResults()) || response.getResults().isEmpty()) {
            throw new NoCardsFoundException("No/empty response from Cincai API");
        }

        List<CincaiCardDto> results = response.getResults();

        if (Objects.isNull(results) || results.isEmpty()) {
            throw new NoCardsFoundException("Empty data returned from Cincai API");
        }

        CincaiCardDto firstCard = results.get(0);

        return firstCard.getSetName();
    }

    //helper function to map the Card DTO attributes to a new Card object
    public Card toCard(CincaiCardDto dto, CardSet cardSet) {
        if (Objects.isNull(dto) || Objects.isNull(cardSet)) {
            throw new IllegalArgumentException("Invalid input: dto or/and cardSet is null");
        }

        return new Card(
            //using custom logic to create external id for cards queried from Cincai API
            ExternalIdFactory.from(CatalogueSource.CINCAITCG, dto.getSourceUrl()),
            dto.getCardNumber(),
            dto.getName(),
            dto.getRarity(),
            cardSet,
            dto.getImageUrl()
        );
    }

    public List<Card> toCards(CincaiQueryResponseDto response, CardSet cardSet) {
        if (Objects.isNull(response) || Objects.isNull(cardSet)) {
            throw new NoCardsFoundException("Invalid input: response or/and cardSet is null");
        }

        List<Card> cardList = new ArrayList<>();

        if (Objects.isNull(response.getResults()) || response.getResults().isEmpty()) {
            throw new NoCardsFoundException("Empty data returned from Cincai API");
        }

        //loop through the query response that has list of card objects and assign it to CincaiCardDto object
        for (CincaiCardDto dto : response.getResults()) {
            //for every item in the response, is a Card dto, use the toCard function to map attributes to a new Card object
            Card card = toCard(dto, cardSet);
            //then add to the cardList to return as a list
            cardList.add(card);
        }

        return cardList;
    }

}
