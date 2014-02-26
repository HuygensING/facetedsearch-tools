package nl.knaw.huygens.facetedsearch.model;

import static nl.knaw.huygens.facetedsearch.model.DefaultFacetMatcher.defaultFacethasCharacteristics;
import static nl.knaw.huygens.facetedsearch.model.RangeFacetMatcher.rangeFacetHasCharacteristics;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

public class FacetDefinitionTest {
  private String facetName = "name";
  private String facetTitle = "title";

  @Mock
  private RangeFacet<Long, Long> rangeFacetMock;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  // Currently the default implementation.
  public void testAddFacetToResultWithListFacet() {
    // setup instance
    FacetDefinition instance = new FacetDefinition().setName(facetName).setTitle(facetTitle).setType(FacetType.LIST);

    // mocks
    QueryResponse queryResponseMock = mock(QueryResponse.class);
    FacetedSearchResult searchResultMock = mock(FacetedSearchResult.class);

    // when
    FacetField solrFacet = new FacetField(facetName);
    String nameFirstOption = "option1";
    long countFirstOption = 123L;
    solrFacet.add(nameFirstOption, countFirstOption);
    String nameSecondOption = "option2";
    long countSecondOption = 12L;
    solrFacet.add(nameSecondOption, countSecondOption);
    when(queryResponseMock.getFacetField(facetName)).thenReturn(solrFacet);

    // action
    instance.addFacetToResult(searchResultMock, queryResponseMock);

    // verify
    InOrder inOrder = inOrder(queryResponseMock, searchResultMock);
    inOrder.verify(queryResponseMock).getFacetField(facetName);

    List<FacetOption> expectedOptions = Lists.newArrayList(//
        new FacetOption(nameFirstOption, countFirstOption), //
        new FacetOption(nameSecondOption, countSecondOption));

    inOrder.verify(searchResultMock).addFacet(argThat(defaultFacethasCharacteristics(facetName, facetTitle, expectedOptions)));
  }

  @Test
  public void testAddFacetToResultWithRangeFacet() {
    // setup instance
    long lowerLimit = 20l;
    long upperLimit = 100l;
    FacetDefinition instance = new FacetDefinition().setName(facetName).setTitle(facetTitle).setType(FacetType.RANGE);

    // mocks
    QueryResponse queryResponseMock = mock(QueryResponse.class);
    FacetedSearchResult searchResultMock = mock(FacetedSearchResult.class);

    // when 
    when(rangeFacetMock.getName()).thenReturn(facetName);
    when(rangeFacetMock.getStart()).thenReturn(lowerLimit);
    when(rangeFacetMock.getEnd()).thenReturn(upperLimit);

    @SuppressWarnings("rawtypes")
    List<RangeFacet> rangeFacetList = Lists.newArrayList();
    rangeFacetList.add(rangeFacetMock);
    when(queryResponseMock.getFacetRanges()).thenReturn(rangeFacetList);

    // action
    instance.addFacetToResult(searchResultMock, queryResponseMock);

    // verify
    verify(queryResponseMock).getFacetRanges();
    verify(searchResultMock).addFacet(argThat(rangeFacetHasCharacteristics(facetName, facetTitle, lowerLimit, upperLimit)));

  }
}
