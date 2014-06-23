package nl.knaw.huygens.facetedsearch.converters;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;
import nl.knaw.huygens.facetedsearch.model.parameters.IndexDescription;

import org.apache.solr.client.solrj.response.QueryResponse;

public class FacetListConverter implements QueryResponseConverter {
  private final IndexDescription indexDescription;

  public FacetListConverter(IndexDescription indexDescription) {
    this.indexDescription = indexDescription;
  }

  @Override
  public void convert(final FacetedSearchResult result, final QueryResponse queryResponse) {
    indexDescription.addFacetDataToSearchResult(result, queryResponse);
  }
}
