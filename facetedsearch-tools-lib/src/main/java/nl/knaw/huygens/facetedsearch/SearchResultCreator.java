package nl.knaw.huygens.facetedsearch;

import nl.knaw.huygens.facetedsearch.converters.QueryResponseConverter;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;

public class SearchResultCreator {

  private final QueryResponseConverter[] converters;

  // TODO: create default constructor

  public SearchResultCreator(QueryResponseConverter... converters) {
    this.converters = converters;
  }

  public FacetedSearchResult build(QueryResponse queryResponse) {
    final FacetedSearchResult result = createFacetedSearchResult();

    for (QueryResponseConverter converter : converters) {
      converter.convert(result, queryResponse);
    }

    return result;
  }

  protected FacetedSearchResult createFacetedSearchResult() {
    return new FacetedSearchResult();
  }

}
