package nl.knaw.huygens.facetedsearch.query;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetedSearchParameters;

import org.apache.solr.client.solrj.SolrQuery;

public interface SolrQueryBuilder {
  /**
   * Adds a particular part to the SolrQuery.
   * @param query the solr query to created query to
   * @param searchParameters the parameters to build the  query from
   * @param <T> the type of the faceted search parameters
   */
  public <T extends FacetedSearchParameters<T>> void build(SolrQuery query, FacetedSearchParameters<T> searchParameters);
}
