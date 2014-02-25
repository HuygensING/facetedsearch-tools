package nl.knaw.huygens.solr.converters;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;

public class FacetListConverter<T extends FacetedSearchParameters<T>> implements QueryResponseConverter {
  private final FacetConverter facetConverter;
  private final T facetedSearchParameters;

  public FacetListConverter(T facetedSearchParameters, FacetConverter facetConveter) {
    this.facetConverter = facetConveter;
    this.facetedSearchParameters = facetedSearchParameters;
  }

  public FacetListConverter(T facetedSearchParameters) {
    this(facetedSearchParameters, new FacetConverter());
  }

  @Override
  public void convert(final FacetedSearchResult result, final QueryResponse queryResponse) {
    for (String facetFieldName : facetedSearchParameters.getFacetFields()) {
      facetConverter.convert(result, queryResponse, facetFieldName, facetedSearchParameters);
    }
  }
}
