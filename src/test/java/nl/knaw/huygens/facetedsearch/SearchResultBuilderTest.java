package nl.knaw.huygens.facetedsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import nl.knaw.huygens.facetedsearch.SearchResultCreator;
import nl.knaw.huygens.facetedsearch.converters.FacetListConverter;
import nl.knaw.huygens.facetedsearch.converters.HighlightingConverter;
import nl.knaw.huygens.facetedsearch.converters.ResultConverter;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;

public class SearchResultBuilderTest {

  @Test
  public void testSearchResultBuilder() {
    //mock
    QueryResponse queryResponseMock = mock(QueryResponse.class);
    FacetListConverter facetConverterMock = mock(FacetListConverter.class);
    HighlightingConverter highlightingConverterMock = mock(HighlightingConverter.class);
    ResultConverter resultConverterMock = mock(ResultConverter.class);

    final FacetedSearchResult searchResultMock = mock(FacetedSearchResult.class);
    SearchResultCreator instance = new SearchResultCreator(facetConverterMock, highlightingConverterMock, resultConverterMock) {
      protected FacetedSearchResult createFacetedSearchResult() {
        return searchResultMock;
      }
    };

    //action
    FacetedSearchResult result = instance.build(queryResponseMock);

    // verify
    verify(facetConverterMock).convert(result, queryResponseMock);
    verify(highlightingConverterMock).convert(result, queryResponseMock);
    verify(resultConverterMock).convert(result, queryResponseMock);

    assertThat(result, is(searchResultMock));
  }
}
