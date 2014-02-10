package nl.knaw.huygens.solr;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.FacetParameter;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.NoSuchFieldInIndexException;
import nl.knaw.huygens.facetedsearch.model.SortParameter;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;

public class SolrQueryCreator {

  public <T extends FacetedSearchParameters<T>> SolrQuery createSearchQuery(FacetedSearchParameters<T> searchParameters, FacetedSearchParametersValidator validator) throws NoSuchFieldInIndexException {
    SolrQuery query = new SolrQuery();
    query.setQuery(createQuery(searchParameters, validator));
    query.setFields(searchParameters.getResultFields().toArray(new String[0]));
    query.addFacetField(searchParameters.getFacetFields().toArray(new String[0]));
    query = setSort(query, searchParameters, validator);

    return query;
  }

  private <T extends FacetedSearchParameters<T>> String createQuery(FacetedSearchParameters<T> searchParameters, FacetedSearchParametersValidator validator) throws NoSuchFieldInIndexException {
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
          builder.append(formatFacetValue(facetParameter));
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

  private String formatFacetValue(FacetParameter facetParameter) {
    if (facetParameter.isRangeFacetParameter()) {
      return formatRangeFacetValue(facetParameter);
    }
    return formatListFacetValue(facetParameter);
  }

  private String formatRangeFacetValue(FacetParameter facetParameter) {
    return String.format("[%s TO %s]", facetParameter.getLowerLimit(), facetParameter.getUpperLimit());
  }

  private String formatListFacetValue(FacetParameter facetParameter) {
    List<String> values = facetParameter.getValues();

    if (values.size() > 1) {
      StringBuilder builder = new StringBuilder();
      builder.append("(");
      String prefix = "";
      for (String value : values) {
        builder.append(prefix).append(SolrUtils.escapeFacetValue(value));
        prefix = " ";
      }
      builder.append(")");
      return builder.toString();
    }
    return SolrUtils.escapeFacetValue(values.get(0));
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
   * @param searchParameters that container of the {@code SortParameter}s
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
