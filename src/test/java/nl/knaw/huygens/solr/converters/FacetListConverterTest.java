package nl.knaw.huygens.solr.converters;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import nl.knaw.huygens.facetedsearch.model.FacetCount;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

public class FacetListConverterTest {
  private FacetConverter facetConveterMock;
  private FacetListConverter instance;
  private FacetCount facetCountMock;
  private FacetedSearchResult resultMock;
  private QueryResponse queryResponseMock;

  @Before
  public void setUp() {
    facetConveterMock = mock(FacetConverter.class);
    instance = new FacetListConverter(facetConveterMock);
    facetCountMock = mock(FacetCount.class);

    resultMock = mock(FacetedSearchResult.class);
    queryResponseMock = mock(QueryResponse.class);
  }

  @Test
  public void testConvertOneFacet() {
    int numberOfFacets = 1;
    // when
    when(queryResponseMock.getFacetFields()).thenReturn(createFacetFieldList(numberOfFacets));
    when(facetConveterMock.convert(any(FacetField.class))).thenReturn(facetCountMock);

    instance.convert(resultMock, queryResponseMock);

    // verify
    InOrder inOrder = Mockito.inOrder(facetConveterMock, resultMock);
    inOrder.verify(facetConveterMock, times(numberOfFacets)).convert(any(FacetField.class));
    inOrder.verify(resultMock, times(numberOfFacets)).addFacet(facetCountMock);
  }

  @Test
  public void testConvertMultipleFacets() {
    int numberOfFacets = 5;
    // when
    when(queryResponseMock.getFacetFields()).thenReturn(createFacetFieldList(numberOfFacets));
    when(facetConveterMock.convert(any(FacetField.class))).thenReturn(facetCountMock);

    instance.convert(resultMock, queryResponseMock);

    // verify
    verify(facetConveterMock, times(numberOfFacets)).convert(any(FacetField.class));
    verify(resultMock, times(numberOfFacets)).addFacet(facetCountMock);
  }

  @Test
  public void testConvertZeroFacets() {
    int numberOfFacets = 0;
    // when
    when(queryResponseMock.getFacetFields()).thenReturn(createFacetFieldList(numberOfFacets));
    when(facetConveterMock.convert(any(FacetField.class))).thenReturn(facetCountMock);

    instance.convert(resultMock, queryResponseMock);

    // verify
    verify(facetConveterMock, never()).convert(any(FacetField.class));
    verify(resultMock, never()).addFacet(facetCountMock);
  }

  private List<FacetField> createFacetFieldList(int numberOfFacets) {
    ArrayList<FacetField> facetFieldList = Lists.newArrayList();

    for (int i = 0; i < numberOfFacets; i++) {
      facetFieldList.add(mock(FacetField.class));
    }

    return facetFieldList;
  }
}
