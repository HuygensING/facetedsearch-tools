package nl.knaw.huygens.facetedsearch.model;

import java.util.List;
import java.util.Map;

/**
 * This class contains the data of a search result, collected and converted.  
 */
public class FacetedSearchResult {
  private float maxScore;
  private long numFound;
  private long offset;

  private List<Facet<?>> facets;
  private Map<String, List<String>> highlighting;
  private List<String> ids;
  private List<Map<String, Object>> rawResults;

  public float getMaxScore() {
    return maxScore;
  }

  public void setMaxScore(float maxScore) {
    this.maxScore = maxScore;
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

  public List<Facet<?>> getFacets() {
    return facets;
  }

  public void setFacets(List<Facet<?>> facets) {
    this.facets = facets;
  }

  public Map<String, List<String>> getHighlighting() {
    return highlighting;
  }

  public void setHighlighting(Map<String, List<String>> highlighting) {
    this.highlighting = highlighting;
  }

  public List<String> getIds() {
    return ids;
  }

  public void setIds(List<String> ids) {
    this.ids = ids;
  }

  public List<Map<String, Object>> getRawResults() {
    return rawResults;
  }

  public void setRawResults(List<Map<String, Object>> rawResults) {
    this.rawResults = rawResults;
  }

  public void addFacet(Facet facet) {
    // TODO Auto-generated method stub

  }

}
