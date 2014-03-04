package nl.knaw.huygens.facetedsearch.converters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.google.common.collect.Maps;

public class RawResultConverterTest {
  private RawResultConverter instance;
  private FacetedSearchResult searchResultMock;
  private QueryResponse queryResponseMock;

  @Before
  public void setUp() {
    instance = new RawResultConverter();

    searchResultMock = mock(FacetedSearchResult.class);
    queryResponseMock = mock(QueryResponse.class);
  }

  @Test
  public void testConvert() {
    SolrDocumentList documentList = new SolrDocumentList();
    documentList.add(createSolrDocument("1", "2", "3"));
    documentList.add(createSolrDocument("3", "4", "5"));
    documentList.add(createSolrDocument("6", "7", "8"));

    // when
    when(queryResponseMock.getResults()).thenReturn(documentList);

    // action
    instance.convert(searchResultMock, queryResponseMock);

    // verify
    InOrder inOrder = Mockito.inOrder(queryResponseMock, searchResultMock);
    inOrder.verify(queryResponseMock).getResults();
    inOrder.verify(searchResultMock).addRawResult(createRawResultMap("1", "2", "3"));
    inOrder.verify(searchResultMock).addRawResult(createRawResultMap("3", "4", "5"));
    inOrder.verify(searchResultMock).addRawResult(createRawResultMap("6", "7", "8"));
  }

  @Test
  public void testConvertNoResult() {
    // when
    when(queryResponseMock.getResults()).thenReturn(new SolrDocumentList());

    // action
    instance.convert(searchResultMock, queryResponseMock);

    // verify
    verify(queryResponseMock).getResults();
    verify(searchResultMock, never()).addRawResult(Mockito.<Map<String, Object>> any());

  }

  private SolrDocument createSolrDocument(String... fields) {
    SolrDocument doc = new SolrDocument();
    int index = 0;

    for (String field : fields) {
      doc.setField("" + index, field);
      index++;
    }

    return doc;
  }

  private Map<String, Object> createRawResultMap(String... values) {
    Map<String, Object> rawResult = Maps.newHashMap();
    int index = 0;

    for (String value : values) {
      rawResult.put("" + index, value);
      index++;
    }

    return rawResult;
  }
}
