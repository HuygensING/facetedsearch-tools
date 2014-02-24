package nl.knaw.huygens.solr.converters;

import nl.knaw.huygens.facetedsearch.model.FacetCount;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;

public class FacetConverter {

  public <T extends FacetedSearchParameters<T>> FacetCount convert(FacetedSearchResult result, QueryResponse queryResponseMock, String facetFieldName, FacetedSearchParameters<T> facetedSearchParameters) {
    // TODO Auto-generated method stub
    return null;
  }

}
