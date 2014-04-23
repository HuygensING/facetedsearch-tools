package nl.knaw.huygens.facetedsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import nl.knaw.huygens.facetedsearch.converters.FacetListConverter;
import nl.knaw.huygens.facetedsearch.converters.ResultConverter;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;
import nl.knaw.huygens.facetedsearch.model.parameters.DefaultFacetedSearchParameters;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;

public class SearchResultCreatorTest {

  @Test
  public void testCreate() {
    //mock
    QueryResponse queryResponseMock = mock(QueryResponse.class);
    FacetListConverter facetConverterMock = mock(FacetListConverter.class);
    ResultConverter resultConverterMock = mock(ResultConverter.class);
    DefaultFacetedSearchParameters searchParameters = new DefaultFacetedSearchParameters();

    final FacetedSearchResult searchResultMock = mock(FacetedSearchResult.class);
    SearchResultCreator instance = new SearchResultCreator(facetConverterMock, resultConverterMock) {
      protected FacetedSearchResult createFacetedSearchResult() {
        return searchResultMock;
      }
    };

    //action
    FacetedSearchResult result = instance.build(queryResponseMock, searchParameters);

    // verify
    verify(facetConverterMock).convert(searchResultMock, queryResponseMock);
    verify(resultConverterMock).convert(searchResultMock, queryResponseMock);
    verify(result).addSearchParameters(searchParameters);

    assertThat(result, is(searchResultMock));
  }
}
