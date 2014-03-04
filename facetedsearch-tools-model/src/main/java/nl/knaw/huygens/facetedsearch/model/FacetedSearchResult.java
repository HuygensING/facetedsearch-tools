package nl.knaw.huygens.facetedsearch.model;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

/**
 * This class contains the data of a search result, collected and converted.  
 */
public class FacetedSearchResult {
  private float maxScore;
  private long numFound;
  private long offset;

  private List<Facet> facets;
  private Map<String, List<String>> highlighting;
  private List<Map<String, Object>> rawResults = Lists.newArrayList();

  public float getMaxScore() {
    return maxScore;
  }

  public void setMaxScore(Float maxScore) {
    if (maxScore != null) {
      this.maxScore = maxScore;
    }
  }

  public long getNumFound() {
    return numFound;
  }

  public void setNumFound(long numFound) {
    this.numFound = numFound;
  }

  public long getOffset() {
    return offset;
  }

  public void setOffset(long offset) {
    this.offset = offset;
  }

  public List<Facet> getFacets() {
    return facets;
  }

  public void setFacets(List<Facet> facets) {
    this.facets = facets;
  }

  public Map<String, List<String>> getHighlighting() {
    return highlighting;
  }

  public void setHighlighting(Map<String, List<String>> highlighting) {
    this.highlighting = highlighting;
  }

  public List<Map<String, Object>> getRawResults() {
    return rawResults;
  }

  public void setRawResults(List<Map<String, Object>> rawResults) {
    this.rawResults = rawResults;
  }

  public void addFacet(Facet facet) {
    this.facets.add(facet);

  }

  public void addRawResult(Map<String, Object> rawResult) {
    this.rawResults.add(rawResult);
  }
}
