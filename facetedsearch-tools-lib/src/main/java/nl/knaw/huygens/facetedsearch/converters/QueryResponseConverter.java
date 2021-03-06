package nl.knaw.huygens.facetedsearch.converters;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;

public interface QueryResponseConverter {

  /**
   * An interface for retrieving data from the {@code QueryResponse} and adding it to the {@code FacetedSearchResult}.
   * @param result the result to add to
   * @param queryResponse the {@code QueryResponse} to get the data from.
   */
  void convert(final FacetedSearchResult result, final QueryResponse queryResponse);

}