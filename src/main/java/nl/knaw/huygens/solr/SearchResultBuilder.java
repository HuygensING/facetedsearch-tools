package nl.knaw.huygens.solr;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;

public class SearchResultBuilder {
  private final FacetConverter facetConverter;
  private final HighlightingConverter highlightingConverter;
  private final ResultConverter resultConverter;

  public SearchResultBuilder(FacetConverter facetConverter, HighlightingConverter highlightingConverter, ResultConverter resultConverter) {
    this.facetConverter = facetConverter;
    this.highlightingConverter = highlightingConverter;
    this.resultConverter = resultConverter;
  }

  public FacetedSearchResult build(QueryResponse queryResponse) {
    FacetedSearchResult result = createFacetedSearchResult();

    result.setFacets(facetConverter.convert(queryResponse.getFacetFields()));
    result.setHighlighting(highlightingConverter.convert(queryResponse.getHighlighting()));

    resultConverter.setResult(queryResponse.getResults());
    result.setIds(resultConverter.getIdList());
    result.setMaxScore(resultConverter.getMaxScore());
    result.setNumFound(resultConverter.getNumFound());
    result.setOffset(resultConverter.getOffset());
    result.setRawResults(resultConverter.getRawResults());

    return result;
  }

  protected FacetedSearchResult createFacetedSearchResult() {
    return new FacetedSearchResult();
  }

}
