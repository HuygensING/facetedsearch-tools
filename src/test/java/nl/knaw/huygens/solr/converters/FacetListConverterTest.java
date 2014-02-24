package nl.knaw.huygens.solr.converters;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import nl.knaw.huygens.facetedsearch.model.DefaultFacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.FacetCount;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class FacetListConverterTest {
  private FacetConverter facetConveterMock;
  private FacetListConverter<DefaultFacetedSearchParameters> instance;
  private FacetCount facetCountMock;
  private FacetedSearchResult resultMock;
  private QueryResponse queryResponseMock;

  private DefaultFacetedSearchParameters facetedSearchParameters;

  @Before
  public void setUp() {
    facetConveterMock = mock(FacetConverter.class);
    facetedSearchParameters = mock(DefaultFacetedSearchParameters.class);
    instance = new FacetListConverter<DefaultFacetedSearchParameters>(facetConveterMock, facetedSearchParameters);
    facetCountMock = mock(FacetCount.class);

    resultMock = mock(FacetedSearchResult.class);
    queryResponseMock = mock(QueryResponse.class);
  }

  @Test
  public void testConvertOneFacet() {
    int numberOfFacets = 1;
    // when
    when(facetedSearchParameters.getFacetFields()).thenReturn(createFacetFieldList(numberOfFacets));

    instance.convert(resultMock, queryResponseMock);

    // verify
    verify(facetConveterMock, times(numberOfFacets)).convert(any(FacetedSearchResult.class), any(QueryResponse.class), anyString(), any(DefaultFacetedSearchParameters.class));
  }

  private List<String> createFacetFieldList(int numberOfFacets) {
    ArrayList<String> facetFieldList = Lists.newArrayList();

    for (int i = 0; i < numberOfFacets; i++) {
      facetFieldList.add("" + i);
    }

    return facetFieldList;
  }

}
