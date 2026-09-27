package com.tcgtracker.card.ingestion;

import java.util.Objects;

import com.tcgtracker.card.entity.CatalogueSource;

public class ExternalIdFactory {
    private ExternalIdFactory() {}
    
    //reusable method for future ingestion sources to create external Id with dynamic source enum
    public static String from(CatalogueSource source, String sourceValue) {
        Objects.requireNonNull(source, "source cannot be null");
        Objects.requireNonNull(sourceValue, "sourceValue cannot be null");

        if (sourceValue.isBlank()) {
            throw new IllegalArgumentException("sourceValue cannot be blank");
        }

        return source.name() + ":" + sourceValue;
    }
}
