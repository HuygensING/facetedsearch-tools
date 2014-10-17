package nl.knaw.huygens.facetedsearch.query;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetedSearchParameters;
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
    query.setQuery(builder.toString());
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
