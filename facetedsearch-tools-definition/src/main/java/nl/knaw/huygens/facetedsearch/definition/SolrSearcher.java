package nl.knaw.huygens.facetedsearch.definition;

import nl.knaw.huygens.facetedsearch.model.parameters.IndexDescription;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

public interface SolrSearcher {

  /**
   * Execute a {@code SolrQuery} on a {@code SolrServer} 
   * @param query the query to execute
   * @return the response from the {@code SolrServer}
   * @throws SolrServerException when the {@code SolrServer} throws it.
   */
  QueryResponse search(SolrQuery query) throws SolrServerException;

  /**
   * Get a description of the index, the searches are executed on.
   * @return the description of the index
   */
  IndexDescription getIndexDescription();

}
