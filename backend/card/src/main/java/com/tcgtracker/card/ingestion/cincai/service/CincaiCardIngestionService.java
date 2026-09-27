package com.tcgtracker.card.ingestion.cincai.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tcgtracker.card.entity.Card;
import com.tcgtracker.card.entity.CardSet;
import com.tcgtracker.card.entity.CatalogueSource;
import com.tcgtracker.card.ingestion.cincai.client.CincaiCatalogueClient;
import com.tcgtracker.card.ingestion.cincai.dto.CincaiQueryResponseDto;
import com.tcgtracker.card.repository.CardRepository;
import com.tcgtracker.card.repository.CardSetRepository;

import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Transactional 
@Builder 
@RequiredArgsConstructor 
@Service
public class CincaiCardIngestionService {

    private final CardRepository cardRepository;
    private final CardSetRepository cardSetRepository;
    private final CincaiCatalogueClient cincaiCatalogueClient;
    private final CincaiCardMapper cincaiCardMapper;

    public int ingestIntoExistingSets(String sourceSetCode, String internalSetCode, CatalogueSource source) {
        Optional<CardSet> cardSet = cardSetRepository.findBySetCode(internalSetCode);
        CincaiQueryResponseDto response =cincaiCatalogueClient.fetchBySetCode(sourceSetCode);

        System.out.println("Searchword " + response.getSearchWord() + " retrieved " + response.getCount() + " cards");
        
        List<Card> cards = cincaiCardMapper.toCards(response, cardSet.get());
        int ingestedCount = 0;

        for (Card card : cards) {
            Optional<Card> existingCard = cardRepository.findByExternalId(card.getExternalId());

            if (!cardRepository.findByExternalId(card.getExternalId()).isEmpty()) {
                System.out.println("Card already exists: " + existingCard.get().getName());
            } else {
                cardRepository.persist(card);
                ingestedCount++;
            }
        }

        System.out.println("Ingested " + ingestedCount + " cards");

        return ingestedCount;
    }

}
