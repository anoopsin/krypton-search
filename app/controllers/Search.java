
package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.SearchResult;
import models.aggregateStrategies.BestOnly;
import models.aggregateStrategies.RoundRobin;
import models.searchengine.SearchEngine;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Search extends Controller {
    /**
     * Search aggregator that searches across multiple search engines.
     * <p>
     * Uses Round Robin aggregate strategy by default.
     * </p>
     * @param query ASCII encoded search query
     * @param bestResultsOnly if true, only returns the results found on multiple,
     *     i.e. > 1 search engines. Defaults to false
     * @return List<{@link SearchResult}>
     * @throws UnsupportedEncodingException
     */
    public Result search(final String query, final boolean bestResultsOnly)
            throws UnsupportedEncodingException {

        final String encodedQuery = URLEncoder.encode(query, "UTF-8");

        final List<SearchEngine> searchEngines = SearchEngine.INSTANCES;

        final List<Set<SearchResult>> searchEngineResults = new ArrayList<>(searchEngines.size());

        for (final SearchEngine searchEngine : searchEngines) {
            searchEngineResults.add(searchEngine.search(encodedQuery));
        }

        final Set<SearchResult> aggregatedSearchResults = bestResultsOnly
                ? BestOnly.INSTANCE.prepareAggregatedSearchResults(searchEngineResults)
                : RoundRobin.INSTANCE.prepareAggregatedSearchResults(searchEngineResults);

        return ok(Json.toJson(aggregatedSearchResults));
    }
}
