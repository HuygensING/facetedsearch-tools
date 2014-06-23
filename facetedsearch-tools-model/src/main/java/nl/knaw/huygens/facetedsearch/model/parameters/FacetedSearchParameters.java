package nl.knaw.huygens.facetedsearch.model.parameters;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import nl.knaw.huygens.facetedsearch.model.NoSuchFieldInIndexException;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

@SuppressWarnings("unchecked")
@XmlRootElement
public class FacetedSearchParameters<T extends FacetedSearchParameters<T>> {
  private String term = "*"; // set in front-end
  private List<String> fullTextSearchFields; // set in back-end
  private List<FacetField> facetFields = Lists.newArrayList(); // set in back-end
  private List<FacetParameter> facetParameters = Lists.newArrayList(); // set in front-end
  private List<String> resultFields = Lists.newArrayList(); // set in back-end
  private boolean fuzzy = false; // set in front-end
  private List<SortParameter> sortParameters = Lists.newArrayList(); // set in front-end
  private QueryOptimizer queryOptimizer; // set in back-end
  private HighlightingOptions highlightingOptions; // set in back-end

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

  public T setFacetParameters(List<FacetParameter> fp) {
    this.facetParameters = fp;
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
   * Validates the model if all the fields defined by the user exist and have the right value. 
   * @param indexDescription a map that contains information about the facets.
   * @throws NoSuchFieldInIndexException when a field is found that does not exist.
   */
  public void validate(IndexDescription indexDescription) throws NoSuchFieldInIndexException {
    for (FacetField facetField : facetFields) {
      if (!indexDescription.doesFacetFieldExist(facetField)) {
        throw new NoSuchFieldInIndexException(facetField.getName());
      }
    }

    for (FacetParameter facetParameter : facetParameters) {
      if (!indexDescription.doesFacetParameterExist(facetParameter)) {
        throw new NoSuchFieldInIndexException(facetParameter.getName());
      }
    }

    for (String resultField : resultFields) {
      if (!indexDescription.doesResultFieldExist(resultField)) {
        throw new NoSuchFieldInIndexException(resultField);
      }
    }

    for (SortParameter sortParameter : sortParameters) {
      if (!indexDescription.doesSortParameterExist(sortParameter)) {
        throw new NoSuchFieldInIndexException(sortParameter.getFieldname());
      }
    }

  }
}
