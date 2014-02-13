package nl.knaw.huygens.solr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import nl.knaw.huygens.facetedsearch.model.FacetCount;
import nl.knaw.huygens.facetedsearch.model.SolrFields;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;

public class SolrQueryResponseHelperTest {

  @Test
  public void testGetIds() {
    QueryResponse response = createDocumentsFilledQueryReponse(0L, 0.0F, 0L, "id1", "id2", "id3");

    SolrQueryResponseHelper helper = new SolrQueryResponseHelper(response);

    assertEquals(Lists.newArrayList("id1", "id2", "id3"), helper.getIds());
  }

  @Test
  public void testGetIdsNoneFound() {
    QueryResponse response = createDocumentsFilledQueryReponse(0L, 0.0F, 0L);

    SolrQueryResponseHelper helper = new SolrQueryResponseHelper(response);

    assertEquals(Lists.newArrayList(), helper.getIds());
  }

  @Ignore
  @Test
  public void testGetFacetCounts() {
    QueryResponse response = createQueryResponse();
    String facetName = "facet";
    String facetValue = "facetValue";
    int count = 99;
    when(response.getFacetFields()).thenReturn(Lists.newArrayList(createFacetField(facetName, facetValue, count)));

    FacetCount facetCount = new FacetCount();
    facetCount.setName(facetName);
    facetCount.addOption(new FacetCount.Option().setName(facetValue).setCount(count));

    fail("Yet to be implemented");
  }

  @Ignore
  @Test
  public void testGetRangeFacetCounts() {

    fail("Yet to be implemented");
  }

  @Ignore
  @Test
  public void testGetFacetCountsNoneFound() {
    fail("Yet to be implemented");
  }

  @Ignore
  @Test
  public void testGetFacetCountsFacetInfoNotFound() {
    fail("Yet to be implemented");
  }

  @Ignore
  @Test
  public void testGetDocuments() {
    QueryResponse response = createDocumentsFilledQueryReponse(0L, 0.0F, 0L, "id1", "id2", "id3");

    SolrQueryResponseHelper helper = new SolrQueryResponseHelper(response);

    assertEquals(3, helper.getDocuments().size());
  }

  @Test
  public void testGetDocumentsNoneFound() {
    QueryResponse response = createDocumentsFilledQueryReponse(0L, 0.0F, 0L);

    SolrQueryResponseHelper helper = new SolrQueryResponseHelper(response);

    assertEquals(0, helper.getDocuments().size());
  }

  @Test
  public void testGetNumFound() {
    long numFound = 1000L;
    QueryResponse response = createDocumentsFilledQueryReponse(numFound, 0.0F, 0L, "id1", "id2", "id3");

    SolrQueryResponseHelper helper = new SolrQueryResponseHelper(response);

    assertEquals(numFound, helper.getNumFound());
  }

  @Test
  public void testGetMaxScore() {
    float maxScore = 5.5f;
    QueryResponse response = createDocumentsFilledQueryReponse(0L, maxScore, 0L, "id1", "id2", "id3");

    SolrQueryResponseHelper helper = new SolrQueryResponseHelper(response);

    assertEquals(maxScore, helper.getMaxScore(), 0.0);
  }

  @Test
  public void testGetStart() {
    long start = 1000L;
    QueryResponse response = createDocumentsFilledQueryReponse(0L, 0.0F, start, "id1", "id2", "id3");

    SolrQueryResponseHelper helper = new SolrQueryResponseHelper(response);

    assertEquals(start, helper.getStart());
  }

  private QueryResponse createDocumentsFilledQueryReponse(long numFound, float maxScore, long start, String... ids) {
    QueryResponse response = createQueryResponse();
    SolrDocumentList documents = new SolrDocumentList();
    documents.setMaxScore(maxScore);
    documents.setNumFound(numFound);
    documents.setStart(start);

    for (String id : ids) {
      documents.add(createSolrDocument(id));
    }

    when(response.getResults()).thenReturn(documents);

    return response;
  }

  private QueryResponse createQueryResponse() {
    QueryResponse response = mock(QueryResponse.class);
    return response;
  }

  private SolrDocument createSolrDocument(String id) {
    SolrDocument doc = new SolrDocument();
    doc.setField(SolrFields.DOC_ID, id);
    return doc;
  }

  private FacetField createFacetField(String name, String value, long count) {
    FacetField facetField = new FacetField(name);
    facetField.add(value, count);

    return facetField;
  }
}
