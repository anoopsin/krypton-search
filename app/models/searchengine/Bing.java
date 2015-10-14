
package models.searchengine;

import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import models.SearchResult;
import play.Logger;
import play.libs.ws.WS;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

public class Bing implements SearchEngine {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "d"
    })
    @Data
    public static class BingResponse {
        @JsonProperty("d")
        private D d;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "results"
    })
    @Data
    public static class D {
        @JsonProperty("results")
        private List<Result> results = new ArrayList<Result>();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "Title",
            "Url"
    })
    @Data
    public static class Result {
        @JsonProperty("Title")
        private String Title;

        @JsonProperty("Url")
        private String Url;
    }

    public static final Bing INSTANCE = new Bing();

    private static final String API_KEY = "dZHx2LeVZb9BHorIDiwiIPTetFDaeqOgBHBJAScDzFE";
    private static final String URL = "https://api.datamarket.azure.com/Bing/Search/Web?$top=10&$skip=0&$format=JSON&Query=%%27%s%%27";
    private static final ObjectMapper JSON_OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<BingResponse> BING_RESPONSE_TYPE_REFERENCE = new TypeReference<BingResponse>() {};

    // Singleton
    protected Bing() {}

    @Override
    public Set<SearchResult> search(final String query) {
        final Set<SearchResult> results = new LinkedHashSet<SearchResult>();

        try {
            final String bingUrl = String.format(URL, query);
            final String accountKeyEncoded = Base64.getEncoder()
                    .encodeToString((API_KEY + ":" + API_KEY).getBytes());

            final WSRequest wsRequest = WS.url(bingUrl);
            wsRequest.setAuth("", API_KEY);

            final WSResponse wsResponse = wsRequest.get().get(1000);

            final BingResponse bingResponse = JSON_OBJECT_MAPPER.readValue(wsResponse.getBody(),
                    BING_RESPONSE_TYPE_REFERENCE);

            for (final Result result : bingResponse.getD().getResults()) {
                final SearchResult searchResult = new SearchResult(new URL(result.getUrl()),
                        result.getTitle());

                results.add(searchResult);
            }

        } catch (final Exception e) {
            // Swallow all exceptions as we still want to serve the client's API request
            Logger.error("Unable to search Bing", e);
        }

        return results;
    }
}
