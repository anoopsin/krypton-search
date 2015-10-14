
package models.aggregateStrategies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import models.SearchResult;

public class RoundRobin implements AggregateStrategy {
    public static final RoundRobin INSTANCE = new RoundRobin();

    // Singleton
    protected RoundRobin() {}

    @Override
    public Set<SearchResult> prepareAggregatedSearchResults(
            final List<Set<SearchResult>> searchEngineResults) {

        final Set<SearchResult> results = new LinkedHashSet<SearchResult>();

        final List<Iterator<SearchResult>> iterators = new ArrayList<Iterator<SearchResult>>(
                searchEngineResults.size());

        for (final Set<SearchResult> searchEngineResult : searchEngineResults) {
            iterators.add(searchEngineResult.iterator());
        }

        boolean areSearchResultsStillLeft = true;

        while (areSearchResultsStillLeft) {
            areSearchResultsStillLeft = false;

            for (final Iterator<SearchResult> iterator : iterators) {
                if (iterator.hasNext()) results.add(iterator.next());

                areSearchResultsStillLeft = areSearchResultsStillLeft || iterator.hasNext();
            }
        }

        return results;
    }
}
