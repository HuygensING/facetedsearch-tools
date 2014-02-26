package nl.knaw.huygens.solr.converters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import nl.knaw.huygens.facetedsearch.model.DefaultFacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.FacetInfo;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;

import com.google.common.collect.Lists;

public class FacetListConverterTest {

  private FacetListConverter instance;
  private FacetedSearchResult resultMock;
  private QueryResponse queryResponseMock;

  public void setUp(FacetInfo... facetInfos) {

    instance = new FacetListConverter<DefaultFacetedSearchParameters>(Lists.newArrayList(facetInfos));

    resultMock = mock(FacetedSearchResult.class);
    queryResponseMock = mock(QueryResponse.class);
  }

  @Test
  public void testConvertOneFacet() {
    // mock
    FacetInfo facetInfo = mock(FacetInfo.class);

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
    FacetInfo facetInfo1 = mock(FacetInfo.class);
    FacetInfo facetInfo2 = mock(FacetInfo.class);
    FacetInfo facetInfo3 = mock(FacetInfo.class);

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
