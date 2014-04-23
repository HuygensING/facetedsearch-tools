package nl.knaw.huygens.facetedsearch;

import java.util.List;

import nl.knaw.huygens.facetedsearch.converters.FacetListConverter;
import nl.knaw.huygens.facetedsearch.converters.QueryResponseConverter;
import nl.knaw.huygens.facetedsearch.converters.ResultConverter;
import nl.knaw.huygens.facetedsearch.model.FacetDefinition;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetedSearchParameters;

import org.apache.solr.client.solrj.response.QueryResponse;

public class SearchResultCreator {

  private final QueryResponseConverter[] converters;

  public SearchResultCreator(List<FacetDefinition> facetDefinitions) {
    this(new FacetListConverter(facetDefinitions), new ResultConverter());
  }

  public SearchResultCreator(QueryResponseConverter... converters) {
    this.converters = converters;
  }

  public <T extends FacetedSearchParameters<T>> FacetedSearchResult build(QueryResponse queryResponse, FacetedSearchParameters<T> searchParameters) {
    final FacetedSearchResult result = createFacetedSearchResult();

    for (QueryResponseConverter converter : converters) {
      converter.convert(result, queryResponse);
    }

    result.addSearchParameters(searchParameters);

    return result;
  }

  protected FacetedSearchResult createFacetedSearchResult() {
    return new FacetedSearchResult();
  }

}
