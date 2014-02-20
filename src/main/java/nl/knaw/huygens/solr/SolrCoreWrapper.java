package nl.knaw.huygens.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

public interface SolrCoreWrapper {

  QueryResponse search(SolrQuery query) throws FacetedSearchException;

}
