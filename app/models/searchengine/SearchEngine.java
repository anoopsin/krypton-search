
package models.searchengine;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import models.SearchResult;

public interface SearchEngine {
    public static final List<SearchEngine> INSTANCES = Arrays.asList(
            Google.INSTANCE,
            Bing.INSTANCE);

    public Set<SearchResult> search(String query);
}
