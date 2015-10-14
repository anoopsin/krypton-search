
package models.aggregateStrategies;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import models.SearchResult;

/**
 * Only returns the results found on multiple, i.e. > 1 search engines.
 * @author anoopsin
 */
public class BestOnly implements AggregateStrategy {
    public static final BestOnly INSTANCE = new BestOnly();

    // Singleton
    protected BestOnly() {}

    @Override
    public Set<SearchResult> prepareAggregatedSearchResults(
            final List<Set<SearchResult>> searchEngineResults) {

        final Set<SearchResult> results = new HashSet<SearchResult>();

        final Multiset<SearchResult> searchResultBag = HashMultiset.create();

        for (final Set<SearchResult> searchEngineResult : searchEngineResults) {
            for (final SearchResult searchResult : searchEngineResult) {
                searchResultBag.add(searchResult);
            }
        }

        for (final SearchResult searchResult : searchResultBag.elementSet()) {
            if (searchResultBag.count(searchResult) > 1) results.add(searchResult);
        }

        return results;
    }
}
