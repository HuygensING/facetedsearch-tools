package nl.knaw.huygens.facetedsearch.converters;

import java.util.Map;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

import com.google.common.collect.Maps;

/**
 * Adds the real data from the {@code QueryResponse} to the {@code FacetedSearchResult}.
 */
public class RawResultConverter implements QueryResponseConverter {

  @Override
  public void convert(FacetedSearchResult result, QueryResponse queryResponse) {
    for (SolrDocument document : queryResponse.getResults()) {
      result.addRawResult(convertDocument(document));
    }
  }

  private Map<String, Object> convertDocument(SolrDocument document) {
    Map<String, Object> rawResult = Maps.newHashMap();

    for (String fieldName : document.getFieldNames()) {
      rawResult.put(fieldName, document.getFieldValue(fieldName));
    }

    return rawResult;
  }

}
