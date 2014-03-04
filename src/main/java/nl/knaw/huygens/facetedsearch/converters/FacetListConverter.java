package nl.knaw.huygens.facetedsearch.converters;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.FacetDefinition;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;

public class FacetListConverter implements QueryResponseConverter {
  private final List<FacetDefinition> facetDefinitionList;

  public FacetListConverter(List<FacetDefinition> facetDefinitionList) {
    this.facetDefinitionList = facetDefinitionList;
  }

  @Override
  public void convert(final FacetedSearchResult result, final QueryResponse queryResponse) {
    for (FacetDefinition facetInfo : facetDefinitionList) {
      facetInfo.addFacetToResult(result, queryResponse);
    }
  }
}
