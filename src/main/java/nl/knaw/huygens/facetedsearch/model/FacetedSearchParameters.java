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
  private List<FacetField> facetFields = Lists.newArrayList();
  private List<FacetParameter> facetParameters = Lists.newArrayList();
  private Map<String, FacetDefinition> facetInfoMap;
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
  public T setFacetFields(List<FacetField> facetFields) {
    this.facetFields = facetFields;
    return (T) this;
  }

  public List<String> getFacetFields() {
    List<String> fields = Lists.newArrayList();

    for (FacetField field : facetFields) {
      fields.addAll(field.getFields());
    }

    return fields;
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

  public Map<String, FacetDefinition> getFacetInfoMap() {
    return facetInfoMap;
  }

  public T setFacetInfoMap(Map<String, FacetDefinition> facetInfoMap) {
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

  /**
   * Get a single {@code FacetInfo} of corresponding with the {@code facetFieldName}.
   * @param facetFieldName the name of the facet to get the {@code FacetInfo} for.
   * @return the {@code FacetInfo} if exists else null. 
   */
  public FacetDefinition getFacetInfo(String facetFieldName) {
    return facetInfoMap.get(facetFieldName);
  }

  public List<FacetDefinition> getFacetInfoForRequest() {
    List<FacetDefinition> facetInfos = Lists.newArrayList();
    for (String facetFieldName : getFacetFields()) {
      facetInfos.add(getFacetInfo(facetFieldName));
    }

    return facetInfos;
  }

  /**
   * Validates the model if all the fields defined by the user exist and have the right value. 
   * @throws NoSuchFieldInIndexException when a field is found that does not exist.
   * @throws WrongFacetValueException when a facet has the wrong value.
   */
  public void validate() throws NoSuchFieldInIndexException, WrongFacetValueException {
    // TODO Auto-generated method stub

  }

}
