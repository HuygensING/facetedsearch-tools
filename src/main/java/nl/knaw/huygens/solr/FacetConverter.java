package nl.knaw.huygens.solr;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;

public class FacetConverter implements QueryResponseConverter {

  @Override
  public <T extends FacetedSearchResult> void convert(T result, QueryResponse queryResponse) {
    // TODO Auto-generated method stub

  }

}
