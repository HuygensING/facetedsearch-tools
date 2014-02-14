package nl.knaw.huygens.solr;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.FacetParameter;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.HighlightingOptions;
import nl.knaw.huygens.facetedsearch.model.NoSuchFieldInIndexException;
import nl.knaw.huygens.facetedsearch.model.QueryOptimizer;
import nl.knaw.huygens.facetedsearch.model.RangeParameter;
import nl.knaw.huygens.facetedsearch.model.SortParameter;
import nl.knaw.huygens.facetedsearch.model.WrongFacetValueException;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.common.params.HighlightParams;

public class SolrQueryCreator {

  public <T extends FacetedSearchParameters<T>> SolrQuery createSearchQuery(FacetedSearchParameters<T> searchParameters, FacetedSearchParametersValidator validator)
      throws NoSuchFieldInIndexException, WrongFacetValueException {
    SolrQuery query = new SolrQuery();
    query.setQuery(createQuery(searchParameters, validator));
    query.setFields(validateResultFields(searchParameters, validator));
    query.addFacetField(validateFacetFields(searchParameters, validator));
    query = setSort(query, searchParameters, validator);
    query = setHighlighting(query, searchParameters);

    optimizeQuery(query, searchParameters);

    return query;
  }

  private <T extends FacetedSearchParameters<T>> SolrQuery optimizeQuery(SolrQuery query, FacetedSearchParameters<T> searchParameters) {
    QueryOptimizer optimizer = searchParameters.getQueryOptimizer();

    if (optimizer != null) {
      query.setRows(optimizer.getRows());
      query.setFacetLimit(optimizer.getFacetLimit());
      query.setFacetMinCount(optimizer.getFacetMinCount());
    }

    return query;
  }

  private <T extends FacetedSearchParameters<T>> SolrQuery setHighlighting(SolrQuery query, FacetedSearchParameters<T> searchParameters) {
    HighlightingOptions highlightingOptions = searchParameters.getHighlightingOptions();

    if (highlightingOptions != null && searchParameters.getFullTextSearchFields() != null) {
      query.setHighlight(true);
      query.setHighlightFragsize(highlightingOptions.getFragSize());
      query.set(HighlightParams.MAX_CHARS, highlightingOptions.getMaxChars());
      query.set(HighlightParams.MERGE_CONTIGUOUS_FRAGMENTS, highlightingOptions.isMergeContiguous());
      query.set(HighlightParams.FIELDS, searchParameters.getFullTextSearchFields().toArray(new String[0]));
    }

    return query;
  }

  private <T extends FacetedSearchParameters<T>> String[] validateResultFields(FacetedSearchParameters<T> searchParameters, FacetedSearchParametersValidator validator)
      throws NoSuchFieldInIndexException {
    for (String resultField : searchParameters.getResultFields()) {
      if (!validator.resultFieldExists(resultField)) {
        throw new NoSuchFieldInIndexException(resultField);
      }
    }

    return searchParameters.getResultFields().toArray(new String[0]);
  }

  private <T extends FacetedSearchParameters<T>> String[] validateFacetFields(FacetedSearchParameters<T> searchParameters, FacetedSearchParametersValidator validator)
      throws NoSuchFieldInIndexException {

    for (String facetField : searchParameters.getFacetFields()) {
      if (!validator.facetFieldExists(facetField)) {
        throw new NoSuchFieldInIndexException(facetField);
      }
    }

    return searchParameters.getFacetFields().toArray(new String[0]);
  }

  private <T extends FacetedSearchParameters<T>> String createQuery(FacetedSearchParameters<T> searchParameters, FacetedSearchParametersValidator validator) throws NoSuchFieldInIndexException,
      WrongFacetValueException {
    StringBuilder builder = new StringBuilder();
    List<String> fullTextSearchFields = searchParameters.getFullTextSearchFields();
    List<FacetParameter> facetValues = searchParameters.getFacetValues();
    boolean useFacets = facetValues != null && !facetValues.isEmpty();

    String prefix = "";
    String term = searchParameters.getTerm();
    if (fullTextSearchFields != null && !isWildCardQuery(term)) {
      for (String field : fullTextSearchFields) {
        builder.append(prefix).append(useFacets ? "+" : "").append(field).append(":");
        builder.append(formatTerm(term, searchParameters.isFuzzy()));
        prefix = " "; // separate the search field searches
      }
    } else if (!useFacets) {
      builder.append("*:*");
    }

    if (useFacets) {
      for (FacetParameter facetParameter : facetValues) {
        String name = facetParameter.getName();
        if (validator.facetExists(facetParameter)) {
          builder.append(" +").append(name).append(":");
          builder.append(formatFacetValue(facetParameter, validator));
        } else {
          throw new NoSuchFieldInIndexException(name);
        }
      }
    }

    return builder.toString();
  }

  private boolean isWildCardQuery(String term) {
    return StringUtils.isBlank(term) || term.equals("*") || term.equals("*:*");
  }

  private String formatFacetValue(FacetParameter facetParameter, FacetedSearchParametersValidator validator) throws WrongFacetValueException {
    if (facetParameter instanceof RangeParameter) {
      if (!validator.isValidRangeFacet(facetParameter)) {
        throw new WrongFacetValueException(facetParameter.getName(), facetParameter.getQueryValue());
      }
    }

    return facetParameter.getQueryValue();
  }

  private String formatTerm(String term, boolean fuzzy) {
    if (fuzzy) {
      term = SolrUtils.fuzzy(term);
    }

    if (term.trim().contains(" ")) {
      return String.format("(%s)", term);
    }
    return term;
  }

  /**
   * Sets the sort criteria for the query.
   * @param query the {@code SolrQuery} that should be sorted.
   * @param searchParameters that container of the {@code SortParameter}s.
   * @param validator a {@code FacetedSearchParametersValidator} to check if the {@code SortParamater}s exist.
   * @return the query, that went in plus the added {@code SortClause}s.
   * @throws NoSuchFieldInIndexException if one of the {@code SortParameter}s does not exist.
   */
  private <T extends FacetedSearchParameters<T>> SolrQuery setSort(SolrQuery query, FacetedSearchParameters<T> searchParameters, FacetedSearchParametersValidator validator)
      throws NoSuchFieldInIndexException {
    List<SortParameter> sortParameters = searchParameters.getSortParameters();
    for (SortParameter sortParameter : sortParameters) {
      if (validator.sortParameterExists(sortParameter)) {
        query.addSort(createSortClause(sortParameter));
      } else {
        throw new NoSuchFieldInIndexException(sortParameter.getFieldname());
      }
    }
    return query;
  }

  private SortClause createSortClause(SortParameter sortParameter) {
    ORDER order = ORDER.valueOf(sortParameter.getDirection().toString());
    return new SortClause(sortParameter.getFieldname(), order);
  }
}
