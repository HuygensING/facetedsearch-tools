package nl.knaw.huygens.solr.converters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;
import nl.knaw.huygens.facetedsearch.model.SolrFields;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

public class ResultConverterTest {
  @Test
  public void testConvert() {
    // mock
    FacetedSearchResult searchResultMock = mock(FacetedSearchResult.class);
    QueryResponse queryResponseMock = mock(QueryResponse.class);

    List<String> ids = Lists.newArrayList("id1", "id2", "id2");
    SolrDocumentList documents = createDocumentList(ids);

    ResultConverter instance = new ResultConverter();
    // when
    when(queryResponseMock.getResults()).thenReturn(documents);

    // action
    instance.convert(searchResultMock, queryResponseMock);

    // verify
    InOrder inOrder = Mockito.inOrder(queryResponseMock, searchResultMock);
    inOrder.verify(queryResponseMock).getResults();
    inOrder.verify(searchResultMock).setIds(ids);

  }

  private SolrDocumentList createDocumentList(List<String> ids) {
    SolrDocumentList documentList = new SolrDocumentList();
    for (String id : ids) {
      SolrDocument doc = new SolrDocument();
      doc.setField(SolrFields.DOC_ID, id);

      documentList.add(doc);
    }

    return documentList;
  }

}
