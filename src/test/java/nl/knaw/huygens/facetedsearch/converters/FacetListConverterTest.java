package nl.knaw.huygens.facetedsearch.converters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import nl.knaw.huygens.facetedsearch.converters.FacetListConverter;
import nl.knaw.huygens.facetedsearch.model.DefaultFacetDefinition;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;

import com.google.common.collect.Lists;

public class FacetListConverterTest {

  private FacetListConverter instance;
  private FacetedSearchResult resultMock;
  private QueryResponse queryResponseMock;

  public void setUp(DefaultFacetDefinition... facetInfos) {

    instance = new FacetListConverter(Lists.newArrayList(facetInfos));

    resultMock = mock(FacetedSearchResult.class);
    queryResponseMock = mock(QueryResponse.class);
  }

  @Test
  public void testConvertOneFacet() {
    // mock
    DefaultFacetDefinition facetInfo = mock(DefaultFacetDefinition.class);

    // setup
    setUp(facetInfo);

    // action
    instance.convert(resultMock, queryResponseMock);

    //verify
    verify(facetInfo).addFacetToResult(resultMock, queryResponseMock);
  }

  @Test
  public void testConvertMultipleFacets() {

    // mock
    DefaultFacetDefinition facetInfo1 = mock(DefaultFacetDefinition.class);
    DefaultFacetDefinition facetInfo2 = mock(DefaultFacetDefinition.class);
    DefaultFacetDefinition facetInfo3 = mock(DefaultFacetDefinition.class);

    // setup
    setUp(facetInfo1, facetInfo2, facetInfo3);

    // action
    instance.convert(resultMock, queryResponseMock);

    //verify
    verify(facetInfo1).addFacetToResult(resultMock, queryResponseMock);
    verify(facetInfo2).addFacetToResult(resultMock, queryResponseMock);
    verify(facetInfo3).addFacetToResult(resultMock, queryResponseMock);
  }

}
