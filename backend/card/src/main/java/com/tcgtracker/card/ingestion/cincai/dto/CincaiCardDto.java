package com.tcgtracker.card.ingestion.cincai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter 
@JsonIgnoreProperties(ignoreUnknown = true)
public class CincaiCardDto{
    String name;
    String cardNumber;
    String rarity;

    @JsonProperty("set")
    String setName;

    @JsonProperty("image")
    String imageUrl;

    @JsonProperty("link")
    String sourceUrl;
}
