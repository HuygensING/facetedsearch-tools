package nl.knaw.huygens.facetedsearch.converters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;

public class ResultConverterTest {
  private FacetedSearchResult searchResultMock;
  private QueryResponse queryResponseMock;
  private ResultConverter instance;
  private ResultMetaDataConverter resultMetaDataConverter;
  private RawResultConverter rawResultConverter;

  @Before
  public void setUp() {
    searchResultMock = mock(FacetedSearchResult.class);
    queryResponseMock = mock(QueryResponse.class);
    resultMetaDataConverter = mock(ResultMetaDataConverter.class);
    rawResultConverter = mock(RawResultConverter.class);
    instance = new ResultConverter(resultMetaDataConverter, rawResultConverter);
  }

  @Test
  public void testConvert() {
    // action
    instance.convert(searchResultMock, queryResponseMock);

    // verify
    verify(rawResultConverter).convert(searchResultMock, queryResponseMock);
    verify(resultMetaDataConverter).convert(searchResultMock, queryResponseMock);
  }

}
