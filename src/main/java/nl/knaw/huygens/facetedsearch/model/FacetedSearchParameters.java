package nl.knaw.huygens.facetedsearch.model;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

@SuppressWarnings("unchecked")
@XmlRootElement
public class FacetedSearchParameters<T extends FacetedSearchParameters<T>> {
  private String term = "*";
  private List<String> fullTextSearchFields;
  private List<String> facetFields = Lists.newArrayList();
  private List<FacetParameter> facetParameters = Lists.newArrayList();
  private Map<String, FacetInfo> facetInfoMap;
  private List<String> resultFields = Lists.newArrayList();
  private boolean fuzzy = false;
  private List<SortParameter> sortParameters = Lists.newArrayList();
  private QueryOptimizer queryOptimizer;
  private HighlightingOptions highlightingOptions;

  public T setTerm(final String term) {
    if (StringUtils.isNotBlank(term)) {
      this.term = term;
    }
    return (T) this;
  }

  public String getTerm() {
    return term;
  }

  public List<String> getFullTextSearchFields() {
    return fullTextSearchFields;
  }

  public T setFullTextSearchFields(List<String> fullTextSearchFields) {
    this.fullTextSearchFields = fullTextSearchFields;
    return (T) this;
  }

  /**
   * Set the facets that should be shown in the result.
   * @param facetFields a list with the fields.
   * @return the current instance of the {@code FacetedSearchParameters}, for the builder pattern.
   */
  public T setFacetFields(List<String> facetFields) {
    this.facetFields = facetFields;
    return (T) this;
  }

  public List<String> getFacetFields() {
    return facetFields;
  }

  /**
   * Set the fields that should be shown in the result.
   * @param resultFields a list with the fields.
   * @return the current instance of the {@code FacetedSearchParameters}, for the builder pattern.
   */
  public T setResultFields(List<String> resultFields) {
    this.resultFields = resultFields;
    return (T) this;
  }

  public List<String> getResultFields() {
    return resultFields;
  }

  public boolean isFuzzy() {
    return fuzzy;
  }

  public T setFuzzy(Boolean fuzzy) {
    this.fuzzy = fuzzy;
    return (T) this;
  }

  public List<FacetParameter> getFacetValues() {
    return facetParameters;
  }

  public T setFacetValues(List<FacetParameter> fp) {
    this.facetParameters = fp;
    return (T) this;
  }

  public Map<String, FacetInfo> getFacetInfoMap() {
    return facetInfoMap;
  }

  public T setFacetInfoMap(Map<String, FacetInfo> facetInfoMap) {
    this.facetInfoMap = facetInfoMap;
    return (T) this;
  }

  public List<SortParameter> getSortParameters() {
    return sortParameters;
  }

  public FacetedSearchParameters<T> setSortParameters(List<SortParameter> sortParameters) {
    this.sortParameters = sortParameters;
    return this;
  }

  public QueryOptimizer getQueryOptimizer() {
    return queryOptimizer;
  }

  public void setQueryOptimizer(QueryOptimizer queryOptimizer) {
    this.queryOptimizer = queryOptimizer;
  }

  public HighlightingOptions getHighlightingOptions() {
    return highlightingOptions;
  }

  public void setHighlightingOptions(HighlightingOptions highlightingOptions) {
    this.highlightingOptions = highlightingOptions;
  }

}
