package nl.knaw.huygens.solr;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;
import nl.knaw.huygens.solr.converters.QueryResponseConverter;

import org.apache.solr.client.solrj.response.QueryResponse;

public class SearchResultBuilder {

  private final QueryResponseConverter[] converters;

  public SearchResultBuilder(QueryResponseConverter... converters) {
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
