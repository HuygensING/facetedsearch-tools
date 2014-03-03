package nl.knaw.huygens.facetedsearch.definition;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

// Different module / package
public interface SolrSearcher {

  QueryResponse search(SolrQuery query) throws SolrServerException;

  List<FacetDefinition> getFacetDefinitions();

}
