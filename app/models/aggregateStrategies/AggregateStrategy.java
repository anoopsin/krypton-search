
package models.aggregateStrategies;

import java.util.List;
import java.util.Set;

import models.SearchResult;

public interface AggregateStrategy {
    public Set<SearchResult> prepareAggregatedSearchResults(
            List<Set<SearchResult>> searchEngineResults);
}
