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
  private boolean caseSensitive = false;
  private String[] facetFields = new String[] {};
  private List<FacetParameter> facetParameters = Lists.newArrayList();
  private Map<String, FacetInfo> facetInfoMap;
  private List<String> resultFields = Lists.newArrayList();
  private boolean fuzzy = false;
  private List<SortParameter> sortParameters = Lists.newArrayList();

  public T setTerm(final String term) {
    if (StringUtils.isNotBlank(term)) {
      this.term = term;
    }
    return (T) this;
  }

  public String getTerm() {
    return term;
  }

  public T setCaseSensitive(boolean matchCase) {
    this.caseSensitive = matchCase;
    return (T) this;
  }

  public boolean isCaseSensitive() {
    return caseSensitive;
  }

  public T setFacetFields(String[] _facetFields) {
    this.facetFields = _facetFields;
    return (T) this;
  }

  public String[] getFacetFields() {
    return facetFields;
  }

  public T setResultFields(List<String> orderLevels) {
    this.resultFields = orderLevels;
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

}
