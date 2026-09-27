package com.tcgtracker.card.ingestion.cincai.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.tcgtracker.card.ingestion.cincai.dto.CincaiQueryResponseDto;

class CincaiCatalogueClientTest {
    private static final String BASE_URL = "https://cincai.example.test";

    private MockRestServiceServer mockServer;
    private CincaiCatalogueClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        client = new CincaiCatalogueClient(restClientBuilder, BASE_URL);
    }

    @Test
    void fetchBySetCodeSendsGetRequestAndDeserializesResponse() {
        mockServer.expect(requestTo(BASE_URL + "/api/search?search_word=OP10"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andRespond(withSuccess("""
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
                          "link": "https://example.test/cards/op10-119"
                        }
                      ]
                    }
                    """, MediaType.APPLICATION_JSON));

        CincaiQueryResponseDto response = client.fetchBySetCode("OP10");

        assertEquals("OP10", response.getSearchWord());
        assertEquals(1, response.getCount());
        assertEquals(1, response.getResults().size());
        assertEquals("OP10-119", response.getResults().get(0).getCardNumber());
        assertEquals("Royal Blood", response.getResults().get(0).getSetName());
        mockServer.verify();
    }

    @Test
    void fetchBySetCodeRejectsNullOrBlankSetCode() {
        assertThrows(IllegalArgumentException.class, () -> client.fetchBySetCode(null));
        assertThrows(IllegalArgumentException.class, () -> client.fetchBySetCode(""));
        assertThrows(IllegalArgumentException.class, () -> client.fetchBySetCode("   "));
        mockServer.verify();
    }
}
