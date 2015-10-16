package nl.knaw.huygens.facetedsearch.query;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.parameters.FullTextSearchParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.IndexDescription;
import nl.knaw.huygens.facetedsearch.services.SolrUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Adds the query string to the SolrQuery. 
 *
 */
public class QueryStringBuilder implements SolrQueryBuilder {
  private Logger LOG = LoggerFactory.getLogger(QueryStringBuilder.class);
  private IndexDescription indexDescription;

  public QueryStringBuilder(IndexDescription indexDescription) {
    this.indexDescription = indexDescription;
  }

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

    boolean areFullTextSearchFieldsDefined = areFullTextSearchFieldsDefined(fullTextSearchFields);
    if (useWildcardQuery(useFacets, useFullTextParameters, term, areFullTextSearchFieldsDefined)) {
      builder.append("*:*");
    } else if (areFullTextSearchFieldsDefined && !isWildCardQuery(term)) {
      builder.append("+(");

      for (String field : fullTextSearchFields) {
        if (!isWildCardQuery(term)) {
          builder.append(prefix)//
              .append(field)//
              .append(":")//
              .append(formatTerm(term, searchParameters.isFuzzy()));
          prefix = " "; // separate the search field searches
        }
      }

      builder.append(")");
    }

    if (useFullTextParameters) {
      for (FullTextSearchParameter fullTextSearchParameter : fullTextSearchParameters) {
        if (!isWildCardQuery(fullTextSearchParameter.getTerm())) {
          builder.append(prefix)//
              .append("+")//
              .append(fullTextSearchParameter.getName())//
              .append(":")//
              .append(formatTerm(fullTextSearchParameter.getTerm(), searchParameters.isFuzzy()));
          prefix = " "; // separate the search field searches
        }
      }
    }

    if (useFacets) {
      for (FacetParameter facetParameter : facetValues) {
        try {
          builder.append(" ");
          indexDescription.appendFacetQueryValue(builder, facetParameter);
        } catch (NoSuchFieldException e) {
          throw new RuntimeException(e);
        }
      }
    }

    LOG.info("Query: {}", builder.toString());

    query.setQuery(builder.toString().trim());
  }

  private boolean useWildcardQuery(boolean useFacets, boolean useFullTextParameters, String term, boolean areFullTextSearchFieldsDefined) {
    return (isWildCardQuery(term) || !areFullTextSearchFieldsDefined) //
        && !useFacets && !useFullTextParameters;
  }

  private boolean areFullTextSearchFieldsDefined(List<String> fullTextSearchFields) {
    return fullTextSearchFields != null && !fullTextSearchFields.isEmpty();
  }

  private boolean isWildCardQuery(String term) {
    return StringUtils.isBlank(term) || term.equals("*") || term.equals("*:*");
  }

  private String formatTerm(String term, boolean fuzzy) {
    return SolrUtils.escapeFulltextValue(term, fuzzy);
  }
}
