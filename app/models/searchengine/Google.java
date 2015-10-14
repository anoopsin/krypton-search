
package models.searchengine;

import java.net.URL;
import java.util.ArrayList;
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

public class Google implements SearchEngine {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "responseData"
    })
    @Data
    public static class GoogleResponse {
        @JsonProperty("responseData")
        private ResponseData responseData;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "results"
    })
    @Data
    public static class ResponseData {
        @JsonProperty("results")
        private List<Result> results = new ArrayList<Result>();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "url",
            "titleNoFormatting"
    })
    @Data
    public static class Result {
        @JsonProperty("url")
        private String url;

        @JsonProperty("titleNoFormatting")
        private String titleNoFormatting;
    }

    public static final Google INSTANCE = new Google();

    private static final String BASE_URL = "http://ajax.googleapis.com/ajax/services/search/web?start=0&rsz=large&v=1.0&q=";
    private static final ObjectMapper JSON_OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<GoogleResponse> GOOGLE_RESPONSE_TYPE_REFERENCE = new TypeReference<GoogleResponse>() {};

    // Singleton
    protected Google() {}

    @Override
    public Set<SearchResult> search(final String query) {
        final Set<SearchResult> results = new LinkedHashSet<SearchResult>();

        try {
            final WSRequest wsRequest = WS.url(BASE_URL + query);

            final WSResponse wsResponse = wsRequest.get().get(1000);
            final GoogleResponse googleResponse = JSON_OBJECT_MAPPER.readValue(wsResponse.getBody(),
                    GOOGLE_RESPONSE_TYPE_REFERENCE);

            for (final Result result : googleResponse.getResponseData().getResults()) {
                final SearchResult searchResult = new SearchResult(new URL(result.getUrl()),
                        result.getTitleNoFormatting());

                results.add(searchResult);
            }
        } catch (final Exception e) {
            // Swallow all exceptions as we still want to serve the client's API request
            Logger.error("Unable to search Google", e);
        }

        return results;
    }
}
