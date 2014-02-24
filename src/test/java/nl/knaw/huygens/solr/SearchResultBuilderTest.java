package nl.knaw.huygens.solr;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;
import nl.knaw.huygens.solr.converters.FacetConverter;
import nl.knaw.huygens.solr.converters.HighlightingConverter;
import nl.knaw.huygens.solr.converters.ResultConverter;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;

public class SearchResultBuilderTest {

  @Test
  public void testSearchResultBuilder() {
    //mock
    QueryResponse queryResponseMock = mock(QueryResponse.class);
    FacetConverter facetConverterMock = mock(FacetConverter.class);
    HighlightingConverter highlightingConverterMock = mock(HighlightingConverter.class);
    ResultConverter resultConverterMock = mock(ResultConverter.class);

    final FacetedSearchResult searchResultMock = mock(FacetedSearchResult.class);
    SearchResultBuilder instance = new SearchResultBuilder(facetConverterMock, highlightingConverterMock, resultConverterMock) {
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
