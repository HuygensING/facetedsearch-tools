package nl.knaw.huygens.facetedsearch.model;

import java.util.List;
import java.util.Map;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.parameters.SortParameter;

import com.google.common.collect.Lists;

/**
 * This class contains the data of a search result, collected and converted.  
 */
public class FacetedSearchResult {
  private float maxScore;
  private long numFound;
  private long offset;

  private List<Facet> facets = Lists.newArrayList();
  private Map<String, List<String>> highlighting;
  private List<Map<String, Object>> rawResults = Lists.newArrayList();
  private List<SortParameter> sort;
  private String term;

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

  /**
   * Returns a list of a maps. The map has as key the field name. The value is the original value.
   * @return
   */
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

  public void setTerm(String term) {
    this.term = term;
  }

  public String getTerm() {
    return term;
  }

  public void setSort(List<SortParameter> sort) {
    this.sort = sort;
  }

  public List<SortParameter> getSort() {
    return this.sort;
  }

  public <T extends FacetedSearchParameters<T>> void addSearchParameters(FacetedSearchParameters<T> facetedSearchParameters) {
    this.term = facetedSearchParameters.getTerm();
    this.sort = facetedSearchParameters.getSortParameters();

  }
}
