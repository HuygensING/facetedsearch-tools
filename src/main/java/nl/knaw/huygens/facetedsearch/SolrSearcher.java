package nl.knaw.huygens.facetedsearch;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.FacetDefinition;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

// Different module / package
public interface SolrSearcher {

  QueryResponse search(SolrQuery query) throws SolrServerException;

  List<FacetDefinition> getFacetDefinitions();

}
