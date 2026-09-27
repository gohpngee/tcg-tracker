package com.tcgtracker.card.ingestion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.tcgtracker.card.entity.CatalogueSource;

class ExternalIdFactoryTest {

    @Test
    void prefixesCincaiUrlWithCatalogueSource() {
        String externalId = ExternalIdFactory.from(
            CatalogueSource.CINCAITCG,
            "https://example.test/cards/op10-119-regular"
        );

        assertEquals(
            "CINCAITCG:https://example.test/cards/op10-119-regular",
            externalId
        );
    }

    @Test
    void prefixesOpaqueProviderIdWithCatalogueSource() {
        String externalId = ExternalIdFactory.from(
            CatalogueSource.TCGDEX,
            "sv01-245"
        );

        assertEquals("TCGDEX:sv01-245", externalId);
    }

    @Test
    void rejectsNullArguments() {
        assertThrows(
            NullPointerException.class,
            () -> ExternalIdFactory.from(null, "sv01-245")
        );
        assertThrows(
            NullPointerException.class,
            () -> ExternalIdFactory.from(CatalogueSource.TCGDEX, null)
        );
    }

    @Test
    void rejectsBlankSourceValue() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ExternalIdFactory.from(CatalogueSource.TCGDEX, "")
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> ExternalIdFactory.from(CatalogueSource.TCGDEX, "   ")
        );
    }
}
