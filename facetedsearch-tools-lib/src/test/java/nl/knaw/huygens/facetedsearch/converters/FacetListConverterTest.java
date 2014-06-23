package nl.knaw.huygens.facetedsearch.converters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;
import nl.knaw.huygens.facetedsearch.model.parameters.IndexDescription;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;

public class FacetListConverterTest {

  private FacetListConverter instance;
  private FacetedSearchResult resultMock;
  private QueryResponse queryResponseMock;
  private IndexDescription indexDescription;

  @Before
  public void setUp() {

    indexDescription = mock(IndexDescription.class);

    instance = new FacetListConverter(indexDescription);

    resultMock = mock(FacetedSearchResult.class);
    queryResponseMock = mock(QueryResponse.class);
  }

  @Test
  public void testConvertOneFacet() {
    // action
    instance.convert(resultMock, queryResponseMock);

    //verify
    verify(indexDescription).addFacetDataToSearchResult(resultMock, queryResponseMock);
  }
}
