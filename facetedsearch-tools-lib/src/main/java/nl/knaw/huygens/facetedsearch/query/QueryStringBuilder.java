package nl.knaw.huygens.facetedsearch.query;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.parameters.FullTextSearchParameter;
import nl.knaw.huygens.facetedsearch.services.SolrUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;

/**
 * Adds the query string to the SolrQuery. 
 *
 */
public class QueryStringBuilder implements SolrQueryBuilder {
  @Override
  public <T extends FacetedSearchParameters<T>> void build(SolrQuery query, FacetedSearchParameters<T> searchParameters) {
    StringBuilder builder = new StringBuilder();
    List<String> fullTextSearchFields = searchParameters.getFullTextSearchFields();
    List<FacetParameter> facetValues = searchParameters.getFacetValues();
    boolean useFacets = facetValues != null && !facetValues.isEmpty();
    List<FullTextSearchParameter> fullTextSearchParameters = searchParameters.getFullTextSearchParameters();
    boolean useFullTextParameters = fullTextSearchParameters != null && !fullTextSearchParameters.isEmpty();

    String prefix = "";
    String term = searchParameters.getTerm();

    if ((isWildCardQuery(term) || !areFullTextSearchFieldsDefined(fullTextSearchFields)) //
        && !useFacets && !useFullTextParameters) {
      builder.append("*:*");
    } else if (areFullTextSearchFieldsDefined(fullTextSearchFields)) {

      if (useFullTextParameters) {
        filterFullTextSearchFields(fullTextSearchParameters, fullTextSearchFields);
        for (FullTextSearchParameter fullTextSearchParameter : fullTextSearchParameters) {
          appendPrefix(builder, useFacets, prefix).//
              append(fullTextSearchParameter.getQueryValue(term));
          prefix = " ";
        }
      }

      for (String field : fullTextSearchFields) {
        appendPrefix(builder, useFacets, prefix).append(field).append(":");
        builder.append(formatTerm(term, searchParameters.isFuzzy()));
        prefix = " "; // separate the search field searches
      }
    }

    if (useFacets) {
      for (FacetParameter facetParameter : facetValues) {
        String name = facetParameter.getName();

        builder.append(" +").append(name).append(":");
        builder.append(facetParameter.getQueryValue());
      }
    }
    query.setQuery(builder.toString());
  }

  private StringBuilder appendPrefix(StringBuilder builder, boolean useFacets, String prefix) {
    return builder.append(prefix).append(useFacets ? "+" : "");
  }

  private void filterFullTextSearchFields(List<FullTextSearchParameter> fullTextSearchParameters, List<String> fullTextSearchFields) {
    for (FullTextSearchParameter fullTextSearchParameter : fullTextSearchParameters) {
      fullTextSearchFields.remove(fullTextSearchParameter.getName());
    }
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
}
