package nl.knaw.huygens.solr.converters;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.FacetDefinition;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;

public class FacetListConverter implements QueryResponseConverter {
  private final List<FacetDefinition> facetInfoList;

  public FacetListConverter(List<FacetDefinition> facetInfoList) {
    this.facetInfoList = facetInfoList;
  }

  @Override
  public void convert(final FacetedSearchResult result, final QueryResponse queryResponse) {
    for (FacetDefinition facetInfo : facetInfoList) {
      facetInfo.addFacetToResult(result, queryResponse);
    }
  }
}
