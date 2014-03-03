package nl.knaw.huygens.facetedsearch.converters;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.DefaultFacetDefinition;
import nl.knaw.huygens.facetedsearch.model.FacetDefinition;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;

public class FacetListConverter implements QueryResponseConverter {
  private final List<DefaultFacetDefinition> facetDefinitionList;

  public FacetListConverter(List<DefaultFacetDefinition> facetDefinitionList) {
    this.facetDefinitionList = facetDefinitionList;
  }

  @Override
  public void convert(final FacetedSearchResult result, final QueryResponse queryResponse) {
    for (FacetDefinition facetInfo : facetDefinitionList) {
      facetInfo.addFacetToResult(result, queryResponse);
    }
  }
}
