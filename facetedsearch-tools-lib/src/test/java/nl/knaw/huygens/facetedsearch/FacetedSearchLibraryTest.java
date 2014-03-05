package nl.knaw.huygens.facetedsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import nl.knaw.huygens.facetedsearch.definition.SolrSearcher;
import nl.knaw.huygens.facetedsearch.model.FacetDefinition;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;
import nl.knaw.huygens.facetedsearch.model.NoSuchFieldInIndexException;
import nl.knaw.huygens.facetedsearch.model.parameters.DefaultFacetedSearchParameters;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class FacetedSearchLibraryTest {

  private FacetedSearchLibrary instance;

  private SolrQueryCreator queryCreatorMock;
  private SolrQuery queryMock;
  private SolrSearcher solrCoreMock;
  private SearchResultCreator searchResultBuilderMock;
  private QueryResponse queryResponseMock;
  private FacetedSearchResult searchResultMock;
  private DefaultFacetedSearchParameters searchParametersMock;
  @Mock
  private Map<String, FacetDefinition> facectDefinitionMapMock;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    queryCreatorMock = mock(SolrQueryCreator.class);
    queryMock = mock(SolrQuery.class);
    solrCoreMock = mock(SolrSearcher.class);
    searchResultBuilderMock = mock(SearchResultCreator.class);
    queryResponseMock = mock(QueryResponse.class);
    searchResultMock = mock(FacetedSearchResult.class);
    searchParametersMock = mock(DefaultFacetedSearchParameters.class);

    instance = new FacetedSearchLibrary(solrCoreMock, queryCreatorMock, searchResultBuilderMock);

  }

  @Test
  public void testSearch() throws NoSuchFieldInIndexException, FacetedSearchException, SolrServerException {
    // when
    when(queryCreatorMock.createSearchQuery(any(DefaultFacetedSearchParameters.class))).thenReturn(queryMock);
    when(solrCoreMock.getFacetDefinitionMap()).thenReturn(facectDefinitionMapMock);
    when(solrCoreMock.search(queryMock)).thenReturn(queryResponseMock);
    when(searchResultBuilderMock.build(queryResponseMock)).thenReturn(searchResultMock);

    // action
    FacetedSearchResult result = instance.search(searchParametersMock);

    // verify
    InOrder inOrder = Mockito.inOrder(searchParametersMock, queryCreatorMock, solrCoreMock, searchResultBuilderMock);
    inOrder.verify(solrCoreMock).getFacetDefinitionMap();
    inOrder.verify(searchParametersMock).validate(facectDefinitionMapMock);
    inOrder.verify(queryCreatorMock).createSearchQuery(searchParametersMock);
    inOrder.verify(solrCoreMock).search(queryMock);
    inOrder.verify(searchResultBuilderMock).build(queryResponseMock);
    assertThat(result, is(searchResultMock));
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void TestSearchSearchParametersValidateThrowsNoSuchFieldInIndexException() throws NoSuchFieldInIndexException, FacetedSearchException, SolrServerException {
    testSearchSearchParametersValidateThrowsException(NoSuchFieldInIndexException.class);

  }

  private void testSearchSearchParametersValidateThrowsException(Class<? extends Exception> exception) throws NoSuchFieldInIndexException, FacetedSearchException, SolrServerException {
    // when
    when(solrCoreMock.getFacetDefinitionMap()).thenReturn(facectDefinitionMapMock);
    doThrow(exception).when(searchParametersMock).validate(facectDefinitionMapMock);
    // action
    try {
      instance.search(searchParametersMock);
    } finally {

      // verify
      verify(solrCoreMock).getFacetDefinitionMap();
      verify(searchParametersMock).validate(facectDefinitionMapMock);
      verify(queryCreatorMock, never()).createSearchQuery(searchParametersMock);
      verify(solrCoreMock, never()).search(any(SolrQuery.class));
      verify(searchResultBuilderMock, never()).build(any(QueryResponse.class));
    }
  }

  @Test(expected = FacetedSearchException.class)
  public void testSearchSolrCoreWrapperThrowsAnSolrServerException() throws NoSuchFieldInIndexException, SolrServerException, FacetedSearchException {
    // when
    when(queryCreatorMock.createSearchQuery(any(DefaultFacetedSearchParameters.class))).thenReturn(queryMock);
    when(solrCoreMock.getFacetDefinitionMap()).thenReturn(facectDefinitionMapMock);
    doThrow(SolrServerException.class).when(solrCoreMock).search(any(SolrQuery.class));

    try {
      instance.search(searchParametersMock);
    } finally {
      InOrder inOrder = inOrder(searchParametersMock, queryCreatorMock, solrCoreMock);
      inOrder.verify(solrCoreMock).getFacetDefinitionMap();
      inOrder.verify(searchParametersMock).validate(facectDefinitionMapMock);
      inOrder.verify(queryCreatorMock).createSearchQuery(searchParametersMock);
      inOrder.verify(solrCoreMock).search(queryMock);
      verify(searchResultBuilderMock, never()).build(any(QueryResponse.class));
    }

  }
}
