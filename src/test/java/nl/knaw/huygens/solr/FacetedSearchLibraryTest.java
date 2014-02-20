package nl.knaw.huygens.solr;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import nl.knaw.huygens.facetedsearch.model.DefaultFacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.NoSuchFieldInIndexException;
import nl.knaw.huygens.facetedsearch.model.WrongFacetValueException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class FacetedSearchLibraryTest {

  private FacetedSearchLibrary instance;

  private SolrQueryCreator queryCreatorMock;
  private SolrQuery queryMock;
  private SolrCoreWrapper solrCoreMock;
  private SearchResultBuilder searchResultBuilderMock;
  private QueryResponse queryResponseMock;
  private SearchResult searchResultMock;

  private DefaultFacetedSearchParameters searchParameters;

  @Before
  public void setUp() {
    queryCreatorMock = mock(SolrQueryCreator.class);
    queryMock = mock(SolrQuery.class);
    solrCoreMock = mock(SolrCoreWrapper.class);
    searchResultBuilderMock = mock(SearchResultBuilder.class);
    queryResponseMock = mock(QueryResponse.class);
    searchResultMock = mock(SearchResult.class);

    instance = new FacetedSearchLibrary(solrCoreMock, queryCreatorMock);
    instance.setSearchResultBuilder(searchResultBuilderMock);

    searchParameters = new DefaultFacetedSearchParameters();
  }

  @Test
  public void testSearch() throws NoSuchFieldInIndexException, WrongFacetValueException {
    // when
    when(queryCreatorMock.createSearchQuery(any(DefaultFacetedSearchParameters.class), any(FacetedSearchParametersValidator.class))).thenReturn(queryMock);
    when(solrCoreMock.query(queryMock)).thenReturn(queryResponseMock);
    when(searchResultBuilderMock.build(queryResponseMock)).thenReturn(searchResultMock);

    // action
    SearchResult result = instance.search(searchParameters);

    // verify
    InOrder inOrder = Mockito.inOrder(queryCreatorMock, solrCoreMock, searchResultBuilderMock);
    inOrder.verify(queryCreatorMock).createSearchQuery(searchParameters, null);
    inOrder.verify(solrCoreMock).query(queryMock);
    inOrder.verify(searchResultBuilderMock).build(queryResponseMock);
    assertThat(result, is(searchResultMock));
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void TestSearchQueryCreatorThrowsNoSuchFieldInIndexException() throws NoSuchFieldInIndexException, WrongFacetValueException {

    testSearchQueryCreatorThrowsException(NoSuchFieldInIndexException.class);

  }

  @Test(expected = WrongFacetValueException.class)
  public void TestSearchQueryCreatorThrowsWrongFacetValueException() throws NoSuchFieldInIndexException, WrongFacetValueException {
    testSearchQueryCreatorThrowsException(WrongFacetValueException.class);
  }

  private void testSearchQueryCreatorThrowsException(Class<? extends Exception> exception) throws NoSuchFieldInIndexException, WrongFacetValueException {
    // when
    doThrow(exception).when(queryCreatorMock).createSearchQuery(any(DefaultFacetedSearchParameters.class), any(FacetedSearchParametersValidator.class));
    // action
    try {
      instance.search(searchParameters);
    } finally {

      // verify
      verify(queryCreatorMock).createSearchQuery(searchParameters, null);
      verify(solrCoreMock, never()).query(any(SolrQuery.class));
      verify(searchResultBuilderMock, never()).build(any(QueryResponse.class));
    }
  }

}
