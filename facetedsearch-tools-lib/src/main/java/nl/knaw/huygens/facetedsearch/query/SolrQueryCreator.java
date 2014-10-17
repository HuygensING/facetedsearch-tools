package nl.knaw.huygens.facetedsearch.query;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.NoSuchFieldInIndexException;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.parameters.HighlightingOptions;
import nl.knaw.huygens.facetedsearch.model.parameters.QueryOptimizer;
import nl.knaw.huygens.facetedsearch.model.parameters.SortParameter;
import nl.knaw.huygens.facetedsearch.services.SolrUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.common.params.HighlightParams;

public class SolrQueryCreator {

  public <T extends FacetedSearchParameters<T>> SolrQuery createSearchQuery(FacetedSearchParameters<T> searchParameters) {
    SolrQuery query = new SolrQuery();
    query.setQuery(createQuery(searchParameters));
    query.setFields(getResultFields(searchParameters));
    addFacetFields(searchParameters, query);
    query = setSort(query, searchParameters);
    query = setHighlighting(query, searchParameters);

    optimizeQuery(query, searchParameters);

    return query;
  }

  private <T extends FacetedSearchParameters<T>> void addFacetFields(FacetedSearchParameters<T> searchParameters, SolrQuery query) {
    List<String> facetFields = searchParameters.getFacetFields();
    if (facetFields != null) {
      query.addFacetField(facetFields.toArray(new String[] {}));
    }
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

  private <T extends FacetedSearchParameters<T>> String[] getResultFields(FacetedSearchParameters<T> searchParameters) {

    return searchParameters.getResultFields().toArray(new String[0]);
  }

  private <T extends FacetedSearchParameters<T>> String createQuery(FacetedSearchParameters<T> searchParameters) {
    StringBuilder builder = new StringBuilder();
    List<String> fullTextSearchFields = searchParameters.getFullTextSearchFields();
    List<FacetParameter> facetValues = searchParameters.getFacetValues();
    boolean useFacets = facetValues != null && !facetValues.isEmpty();

    String prefix = "";
    String term = searchParameters.getTerm();
    if (areFullTextSearchFieldsDefined(fullTextSearchFields) && !isWildCardQuery(term)) {
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

        builder.append(" +").append(name).append(":");
        builder.append(facetParameter.getQueryValue());
      }
    }

    return builder.toString();
  }

  private boolean areFullTextSearchFieldsDefined(List<String> fullTextSearchFields) {
    return fullTextSearchFields != null && !fullTextSearchFields.isEmpty();
  }

  private boolean isWildCardQuery(String term) {
    return StringUtils.isBlank(term) || term.equals("*") || term.equals("*:*");
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
   * @return the query, that went in plus the added {@code SortClause}s.
   * @throws NoSuchFieldInIndexException if one of the {@code SortParameter}s does not exist.
   */
  private <T extends FacetedSearchParameters<T>> SolrQuery setSort(SolrQuery query, FacetedSearchParameters<T> searchParameters) {
    List<SortParameter> sortParameters = searchParameters.getSortParameters();
    for (SortParameter sortParameter : sortParameters) {
      query.addSort(createSortClause(sortParameter));
    }
    return query;
  }

  private SortClause createSortClause(SortParameter sortParameter) {
    ORDER order = ORDER.valueOf(sortParameter.getDirection().toString());
    return new SortClause(sortParameter.getFieldName(), order);
  }
}
