package nl.knaw.huygens.solr.converters;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;

public class FacetListConverter implements QueryResponseConverter {
  private final FacetConverter facetConverter;

  public FacetListConverter(FacetConverter facetConveter) {
    this.facetConverter = facetConveter;
  }

  public FacetListConverter() {
    this(new FacetConverter());
  }

  @Override
  public <T extends FacetedSearchResult> void convert(T result, QueryResponse queryResponse) {
    for (FacetField facetField : queryResponse.getFacetFields()) {
      result.addFacet(facetConverter.convert(facetField));
    }
  }

}
