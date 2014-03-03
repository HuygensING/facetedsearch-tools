package nl.knaw.huygens.facetedsearch.converters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import nl.knaw.huygens.facetedsearch.converters.FacetListConverter;
import nl.knaw.huygens.facetedsearch.definition.FacetDefinition;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;

import com.google.common.collect.Lists;

public class FacetListConverterTest {

  private FacetListConverter instance;
  private FacetedSearchResult resultMock;
  private QueryResponse queryResponseMock;

  public void setUp(FacetDefinition... facetInfos) {

    instance = new FacetListConverter(Lists.newArrayList(facetInfos));

    resultMock = mock(FacetedSearchResult.class);
    queryResponseMock = mock(QueryResponse.class);
  }

  @Test
  public void testConvertOneFacet() {
    // mock
    FacetDefinition facetInfo = mock(FacetDefinition.class);

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
    FacetDefinition facetInfo1 = mock(FacetDefinition.class);
    FacetDefinition facetInfo2 = mock(FacetDefinition.class);
    FacetDefinition facetInfo3 = mock(FacetDefinition.class);

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
