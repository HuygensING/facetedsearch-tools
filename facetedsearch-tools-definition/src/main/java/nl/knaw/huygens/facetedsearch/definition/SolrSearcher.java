package nl.knaw.huygens.facetedsearch.definition;

import java.util.List;
import java.util.Map;

import nl.knaw.huygens.facetedsearch.model.FacetDefinition;

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
   * Get all the facet information of this core.
   * @return 
   */
  List<FacetDefinition> getFacetDefinitions();

  /**
   * Get a map with the facet information of this core. The keys are the names of the facets.
   * @return
   */
  Map<String, FacetDefinition> getFacetDefinitionMap();

}
