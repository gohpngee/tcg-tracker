package com.tcgtracker.card.ingestion.cincai.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CincaiQueryResponseDto {
    String searchWord;
    int count;
    List<CincaiCardDto> results;
}
