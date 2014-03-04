package nl.knaw.huygens.facetedsearch.converters;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;
import org.mockito.InOrder;

public class ResultMetaDataConverterTest {

  @Test
  public void testConvert() {
    ResultMetaDataConverter instance = new ResultMetaDataConverter();

    // mocks
    FacetedSearchResult resultMock = mock(FacetedSearchResult.class);
    QueryResponse queryResponseMock = mock(QueryResponse.class);
    SolrDocumentList documentListMock = mock(SolrDocumentList.class);

    // when
    long numFound = 3;
    float maxScore = 5.12f;
    long start = 0;
    when(documentListMock.getMaxScore()).thenReturn(maxScore);
    when(documentListMock.getNumFound()).thenReturn(numFound);
    when(documentListMock.getStart()).thenReturn(start);

    when(queryResponseMock.getResults()).thenReturn(documentListMock);

    // action
    instance.convert(resultMock, queryResponseMock);

    // verify
    InOrder maxScoreOrder = inOrder(documentListMock, resultMock);
    maxScoreOrder.verify(documentListMock).getMaxScore();
    maxScoreOrder.verify(resultMock).setMaxScore(maxScore);

    InOrder numFoundOrder = inOrder(documentListMock, resultMock);
    numFoundOrder.verify(documentListMock).getNumFound();
    numFoundOrder.verify(resultMock).setNumFound(numFound);

    InOrder offsetOrder = inOrder(documentListMock, resultMock);
    offsetOrder.verify(documentListMock).getStart();
    offsetOrder.verify(resultMock).setOffset(start);
  }

}
