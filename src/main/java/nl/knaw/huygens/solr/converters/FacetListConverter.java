package nl.knaw.huygens.solr.converters;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.FacetInfo;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;

public class FacetListConverter implements QueryResponseConverter {
  private final List<FacetInfo> facetInfoList;

  public FacetListConverter(List<FacetInfo> facetInfoList) {
    this.facetInfoList = facetInfoList;
  }

  @Override
  public void convert(final FacetedSearchResult result, final QueryResponse queryResponse) {
    for (FacetInfo facetInfo : facetInfoList) {
      facetInfo.addFacetToResult(result, queryResponse);
    }
  }
}
