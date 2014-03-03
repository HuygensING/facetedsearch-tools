package nl.knaw.huygens.solr.converters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ResultConverterTest {
  private FacetedSearchResult searchResultMock;
  private QueryResponse queryResponseMock;
  private ResultConverter instance;

  @Before
  public void setUp() {
    searchResultMock = mock(FacetedSearchResult.class);
    queryResponseMock = mock(QueryResponse.class);
    instance = new ResultConverter();
  }

  @Test
  public void testConvert() {
    List<SolrDocument> docs = Lists.newArrayList(//
        createSolrDocument("1", "2", "3"),//
        createSolrDocument("3", "4", "5"),//
        createSolrDocument("6", "7", "8"));
    int numFound = 3;
    float maxScore = 5.12f;
    int start = 0;
    SolrDocumentList documentList = createDocumentList(numFound, maxScore, start, docs);

    // when
    when(queryResponseMock.getResults()).thenReturn(documentList);

    // action
    instance.convert(searchResultMock, queryResponseMock);

    // verify
    verify(queryResponseMock).getResults();
    verify(searchResultMock).setMaxScore(maxScore);
    verify(searchResultMock).setNumFound(numFound);
    verify(searchResultMock).setOffset(start);

    List<Map<String, Object>> expectedRawResults = Lists.newArrayList();
    expectedRawResults.add(createRawResultMap("1", "2", "3"));
    expectedRawResults.add(createRawResultMap("3", "4", "5"));
    expectedRawResults.add(createRawResultMap("6", "7", "8"));

    verify(searchResultMock).setRawResults(expectedRawResults);
  }

  @Test
  public void testConvertWithoutResults() {
    // when
    when(queryResponseMock.getResults()).thenReturn(new SolrDocumentList());

    // action
    instance.convert(searchResultMock, queryResponseMock);

    // verify
    verify(queryResponseMock).getResults();
    verify(searchResultMock).setMaxScore(null);
    verify(searchResultMock).setNumFound(0);
    verify(searchResultMock).setOffset(0);

    List<Map<String, Object>> expectedRawResults = Lists.newArrayList();
    verify(searchResultMock).setRawResults(expectedRawResults);
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

  private SolrDocumentList createDocumentList(long numFound, Float maxScore, long start, List<SolrDocument> docs) {
    SolrDocumentList documentList = new SolrDocumentList();
    documentList.addAll(docs);
    documentList.setNumFound(numFound);
    documentList.setMaxScore(maxScore);
    documentList.setStart(start);

    return documentList;
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

}
