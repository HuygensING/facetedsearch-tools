package nl.knaw.huygens.facetedsearch.converters;

import java.util.List;
import java.util.Map;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ResultConverter implements QueryResponseConverter {

  @Override
  public void convert(final FacetedSearchResult result, final QueryResponse queryResponse) {

    SolrDocumentList solrDocList = queryResponse.getResults();
    result.setMaxScore(solrDocList.getMaxScore());
    result.setNumFound(solrDocList.getNumFound());
    result.setOffset(solrDocList.getStart());

    List<Map<String, Object>> rawResults = Lists.newArrayList();

    for (SolrDocument doc : solrDocList) {
      rawResults.add(getRawResultFromSolrDoc(doc));
    }

    result.setRawResults(rawResults);
  }

  private Map<String, Object> getRawResultFromSolrDoc(SolrDocument doc) {
    Map<String, Object> rawResult = Maps.newHashMap();

    for (String fieldName : doc.getFieldNames()) {
      rawResult.put(fieldName, doc.getFieldValue(fieldName));
    }

    return rawResult;
  }
}
