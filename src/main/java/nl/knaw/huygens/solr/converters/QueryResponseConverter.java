package nl.knaw.huygens.solr.converters;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;

public interface QueryResponseConverter {

  /**
   * An interface for retrieving data from the {@code QueryResponse} and adding it to the {@code FacetedSearchResult}.
   * @param result the result to add to
   * @param queryResponse the {@code QueryResponse} to get the data from.
   */
  <T extends FacetedSearchResult> void convert(T result, QueryResponse queryResponse);

}