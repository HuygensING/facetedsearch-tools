package nl.knaw.huygens.solr;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import nl.knaw.huygens.facetedsearch.model.DefaultFacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;
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
  private FacetedSearchResult searchResultMock;

  private FacetedSearchParameters<DefaultFacetedSearchParameters> searchParameters;

  @Before
  public void setUp() {
    queryCreatorMock = mock(SolrQueryCreator.class);
    queryMock = mock(SolrQuery.class);
    solrCoreMock = mock(SolrCoreWrapper.class);
    searchResultBuilderMock = mock(SearchResultBuilder.class);
    queryResponseMock = mock(QueryResponse.class);
    searchResultMock = mock(FacetedSearchResult.class);

    instance = new FacetedSearchLibrary(solrCoreMock, queryCreatorMock, searchResultBuilderMock);

    searchParameters = new DefaultFacetedSearchParameters();
  }

  @Test
  public void testSearch() throws NoSuchFieldInIndexException, WrongFacetValueException, FacetedSearchException {
    // when
    when(queryCreatorMock.createSearchQuery(any(DefaultFacetedSearchParameters.class), any(FacetedSearchParametersValidator.class))).thenReturn(queryMock);
    when(solrCoreMock.search(queryMock)).thenReturn(queryResponseMock);
    when(searchResultBuilderMock.build(queryResponseMock)).thenReturn(searchResultMock);

    // action
    FacetedSearchResult result = instance.search(searchParameters);

    // verify
    InOrder inOrder = Mockito.inOrder(queryCreatorMock, solrCoreMock, searchResultBuilderMock);
    inOrder.verify(queryCreatorMock).createSearchQuery(searchParameters, null);
    inOrder.verify(solrCoreMock).search(queryMock);
    inOrder.verify(searchResultBuilderMock).build(queryResponseMock);
    assertThat(result, is(searchResultMock));
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void TestSearchQueryCreatorThrowsNoSuchFieldInIndexException() throws NoSuchFieldInIndexException, WrongFacetValueException, FacetedSearchException {
    testSearchQueryCreatorThrowsException(NoSuchFieldInIndexException.class);

  }

  @Test(expected = WrongFacetValueException.class)
  public void TestSearchQueryCreatorThrowsWrongFacetValueException() throws NoSuchFieldInIndexException, WrongFacetValueException, FacetedSearchException {
    testSearchQueryCreatorThrowsException(WrongFacetValueException.class);
  }

  private void testSearchQueryCreatorThrowsException(Class<? extends Exception> exception) throws NoSuchFieldInIndexException, WrongFacetValueException, FacetedSearchException {
    // when
    doThrow(exception).when(queryCreatorMock).createSearchQuery(any(DefaultFacetedSearchParameters.class), any(FacetedSearchParametersValidator.class));
    // action
    try {
      instance.search(searchParameters);
    } finally {

      // verify
      verify(queryCreatorMock).createSearchQuery(searchParameters, null);
      verify(solrCoreMock, never()).search(any(SolrQuery.class));
      verify(searchResultBuilderMock, never()).build(any(QueryResponse.class));
    }
  }

  @Test(expected = FacetedSearchException.class)
  public void testSearchSolrCoreWrapperThrowsAnIndexException() throws FacetedSearchException, NoSuchFieldInIndexException, WrongFacetValueException {
    // when
    when(queryCreatorMock.createSearchQuery(any(DefaultFacetedSearchParameters.class), any(FacetedSearchParametersValidator.class))).thenReturn(queryMock);
    doThrow(FacetedSearchException.class).when(solrCoreMock).search(any(SolrQuery.class));

    try {
      instance.search(searchParameters);
    } finally {
      InOrder inOrder = inOrder(queryCreatorMock, solrCoreMock);
      inOrder.verify(queryCreatorMock).createSearchQuery(searchParameters, null);
      inOrder.verify(solrCoreMock).search(queryMock);
      verify(searchResultBuilderMock, never()).build(any(QueryResponse.class));
    }

  }
}
