package nl.knaw.huygens.solr;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import nl.knaw.huygens.facetedsearch.model.DefaultFacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.NoSuchFieldInIndexException;
import nl.knaw.huygens.facetedsearch.model.WrongFacetValueException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class FacetedSearchLibraryTest {

  @Test
  public void testDoFacetedSearch() throws NoSuchFieldInIndexException, WrongFacetValueException {
    // mocks
    SolrQueryCreator queryCreatorMock = mock(SolrQueryCreator.class);
    SolrQuery queryMock = mock(SolrQuery.class);
    SolrCoreWrapper solrCoreMock = mock(SolrCoreWrapper.class);
    SearchResultBuilder searchResultBuilderMock = mock(SearchResultBuilder.class);
    QueryResponse queryResponseMock = mock(QueryResponse.class);
    SearchResult searchResultMock = mock(SearchResult.class);

    DefaultFacetedSearchParameters parameters = new DefaultFacetedSearchParameters();

    FacetSearchLibrary library = new FacetSearchLibrary(solrCoreMock, queryCreatorMock);

    library.setSearchResultBuilder(searchResultBuilderMock);

    // when
    when(queryCreatorMock.createSearchQuery(any(DefaultFacetedSearchParameters.class), any(FacetedSearchParametersValidator.class))).thenReturn(queryMock);
    when(solrCoreMock.query(queryMock)).thenReturn(queryResponseMock);
    when(searchResultBuilderMock.build(queryResponseMock)).thenReturn(searchResultMock);

    // action
    SearchResult result = library.search(parameters);

    // verify
    InOrder inOrder = Mockito.inOrder(queryCreatorMock, solrCoreMock, searchResultBuilderMock);
    inOrder.verify(queryCreatorMock).createSearchQuery(parameters, null);
    inOrder.verify(solrCoreMock).query(queryMock);
    inOrder.verify(searchResultBuilderMock).build(queryResponseMock);
    assertThat(result, is(searchResultMock));
  }
}
