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
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;
import nl.knaw.huygens.facetedsearch.model.NoSuchFieldInIndexException;
import nl.knaw.huygens.facetedsearch.model.WrongFacetValueException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class FacetedSearchLibraryTest {

  private FacetedSearchLibrary instance;

  private SolrQueryCreator queryCreatorMock;
  private SolrQuery queryMock;
  private SolrSearcher solrCoreMock;
  private SearchResultBuilder searchResultBuilderMock;
  private QueryResponse queryResponseMock;
  private FacetedSearchResult searchResultMock;

  private DefaultFacetedSearchParameters searchParametersMock;

  @Before
  public void setUp() {
    queryCreatorMock = mock(SolrQueryCreator.class);
    queryMock = mock(SolrQuery.class);
    solrCoreMock = mock(SolrSearcher.class);
    searchResultBuilderMock = mock(SearchResultBuilder.class);
    queryResponseMock = mock(QueryResponse.class);
    searchResultMock = mock(FacetedSearchResult.class);
    searchParametersMock = mock(DefaultFacetedSearchParameters.class);

    instance = new FacetedSearchLibrary(solrCoreMock, queryCreatorMock, searchResultBuilderMock);

  }

  @Test
  public void testSearch() throws NoSuchFieldInIndexException, WrongFacetValueException, FacetedSearchException, SolrServerException {
    // when
    when(queryCreatorMock.createSearchQuery(any(DefaultFacetedSearchParameters.class))).thenReturn(queryMock);
    when(solrCoreMock.search(queryMock)).thenReturn(queryResponseMock);
    when(searchResultBuilderMock.build(queryResponseMock)).thenReturn(searchResultMock);

    // action
    FacetedSearchResult result = instance.search(searchParametersMock);

    // verify
    InOrder inOrder = Mockito.inOrder(searchParametersMock, queryCreatorMock, solrCoreMock, searchResultBuilderMock);
    inOrder.verify(searchParametersMock).validate();
    inOrder.verify(queryCreatorMock).createSearchQuery(searchParametersMock);
    inOrder.verify(solrCoreMock).search(queryMock);
    inOrder.verify(searchResultBuilderMock).build(queryResponseMock);
    assertThat(result, is(searchResultMock));
  }

  @Test(expected = FacetedSearchException.class)
  public void testSearchSolrCoreWrapperThrowsAnSolrServerException() throws NoSuchFieldInIndexException, WrongFacetValueException, SolrServerException, FacetedSearchException {
    // when
    when(queryCreatorMock.createSearchQuery(any(DefaultFacetedSearchParameters.class))).thenReturn(queryMock);
    doThrow(SolrServerException.class).when(solrCoreMock).search(any(SolrQuery.class));

    try {
      instance.search(searchParametersMock);
    } finally {
      InOrder inOrder = inOrder(searchParametersMock, queryCreatorMock, solrCoreMock);
      inOrder.verify(searchParametersMock).validate();
      inOrder.verify(queryCreatorMock).createSearchQuery(searchParametersMock);
      inOrder.verify(solrCoreMock).search(queryMock);
      verify(searchResultBuilderMock, never()).build(any(QueryResponse.class));
    }

  }
}
