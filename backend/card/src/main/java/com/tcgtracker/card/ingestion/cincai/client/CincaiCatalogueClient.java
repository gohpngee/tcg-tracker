package com.tcgtracker.card.ingestion.cincai.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import com.tcgtracker.card.ingestion.cincai.dto.CincaiQueryResponseDto;

@RequiredArgsConstructor
@Component 
public class CincaiCatalogueClient {
    private final RestClient restClient;

    public CincaiCatalogueClient(
            RestClient.Builder restClientBuilder,
            @Value("${catalogue.cincai.url}") String baseUrl) {
        
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
        }

    //this method used to fetch from the cincai endpoint by set code, using a restClient
    public CincaiQueryResponseDto fetchBySetCode(String sourceSetCode) {
        if (Objects.isNull(sourceSetCode) || sourceSetCode.isBlank()) {
            throw new IllegalArgumentException("Source set code cannot be null/blank");
        }

        return restClient.get()
        //build the uri for the request
            .uri(UriBuilder -> UriBuilder
                //on top of baseUrl, follow the path for the query endpoint
                .path("/api/search")
                //param for query which is setCode
                .queryParam("search_word", sourceSetCode)
                //build the uri
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            //we expect the body of the json response to be a CincaiQueryResponseDto class
            .body(CincaiQueryResponseDto.class);
    }

}
