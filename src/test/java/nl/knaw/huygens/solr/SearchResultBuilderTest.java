package nl.knaw.huygens.solr;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import nl.knaw.huygens.facetedsearch.model.FacetCount;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mock;

import com.google.common.collect.Lists;

public class SearchResultBuilderTest {

  @Mock
  public Map<String, Map<String, List<String>>> solrHighlighting;

  @Mock
  public Map<String, List<String>> highlighting;

  @Mock
  public List<Map<String, Object>> rawResults;

  @Test
  public void testSearchResultBuilder() {
    //mock
    QueryResponse queryResponseMock = mock(QueryResponse.class);
    FacetConverter facetConverterMock = mock(FacetConverter.class);
    HighlightingConverter highlightingConverterMock = mock(HighlightingConverter.class);
    ResultConverter resultConverterMock = mock(ResultConverter.class);

    final FacetedSearchResult searchResultMock = mock(FacetedSearchResult.class);
    SearchResultBuilder instance = new SearchResultBuilder(facetConverterMock, highlightingConverterMock, resultConverterMock) {
      protected FacetedSearchResult createFacetedSearchResult() {
        return searchResultMock;
      }
    };

    // when
    List<FacetField> facetFields = (List<FacetField>) Lists.<FacetField> newArrayList();
    when(queryResponseMock.getFacetFields()).thenReturn(facetFields);
    SolrDocumentList solrDocumentList = mock(SolrDocumentList.class);
    when(queryResponseMock.getResults()).thenReturn(solrDocumentList);
    when(queryResponseMock.getHighlighting()).thenReturn(solrHighlighting);

    List<FacetCount> facets = Lists.<FacetCount> newArrayList();
    when(facetConverterMock.convert(Matchers.<List<FacetField>> any())).thenReturn(facets);

    when(highlightingConverterMock.convert(Matchers.<Map<String, Map<String, List<String>>>> any())).thenReturn(highlighting);

    List<String> idList = Lists.<String> newArrayList();
    float maxScore = 5.6f;
    long numFound = 5000;
    long offset = 0;
    when(resultConverterMock.getIdList()).thenReturn(idList);
    when(resultConverterMock.getRawResults()).thenReturn(rawResults);
    when(resultConverterMock.getMaxScore()).thenReturn(maxScore);
    when(resultConverterMock.getNumFound()).thenReturn(numFound);
    when(resultConverterMock.getOffset()).thenReturn(offset);

    //action
    FacetedSearchResult result = instance.build(queryResponseMock);

    // verify
    InOrder inOrder = inOrder(facetConverterMock, searchResultMock);
    inOrder.verify(facetConverterMock).convert(facetFields);
    inOrder.verify(searchResultMock).setFacets(facets);

    InOrder highlightingOrder = inOrder(highlightingConverterMock, searchResultMock);
    highlightingOrder.verify(highlightingConverterMock).convert(solrHighlighting);
    highlightingOrder.verify(searchResultMock).setHighlighting(highlighting);

    InOrder idOrder = inOrder(resultConverterMock, searchResultMock);
    idOrder.verify(resultConverterMock).setResult(solrDocumentList);
    idOrder.verify(resultConverterMock).getIdList();
    idOrder.verify(searchResultMock).setIds(idList);

    InOrder maxScoreOrder = inOrder(resultConverterMock, searchResultMock);
    maxScoreOrder.verify(resultConverterMock).setResult(solrDocumentList);
    maxScoreOrder.verify(resultConverterMock).getMaxScore();
    maxScoreOrder.verify(searchResultMock).setMaxScore(maxScore);

    InOrder numFoundOrder = inOrder(resultConverterMock, searchResultMock);
    numFoundOrder.verify(resultConverterMock).setResult(solrDocumentList);
    numFoundOrder.verify(resultConverterMock).getNumFound();
    numFoundOrder.verify(searchResultMock).setNumFound(numFound);

    InOrder offsetOrder = inOrder(resultConverterMock, searchResultMock);
    offsetOrder.verify(resultConverterMock).setResult(solrDocumentList);
    offsetOrder.verify(resultConverterMock).getOffset();
    offsetOrder.verify(searchResultMock).setOffset(offset);

    InOrder rawResultOrder = inOrder(resultConverterMock, searchResultMock);
    rawResultOrder.verify(resultConverterMock).setResult(solrDocumentList);
    rawResultOrder.verify(resultConverterMock).getRawResults();
    rawResultOrder.verify(searchResultMock).setRawResults(rawResults);

    assertThat(result, is(searchResultMock));
  }
}
