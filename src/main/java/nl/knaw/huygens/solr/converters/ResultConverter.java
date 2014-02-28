package nl.knaw.huygens.solr.converters;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;
import nl.knaw.huygens.facetedsearch.model.SolrFields;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

import com.google.common.collect.Lists;

public class ResultConverter implements QueryResponseConverter {

  @Override
  public void convert(final FacetedSearchResult result, final QueryResponse queryResponse) {
    List<String> ids = Lists.newArrayList();
    for (SolrDocument doc : queryResponse.getResults()) {
      ids.add(String.valueOf(doc.getFieldValue(SolrFields.DOC_ID))); //TODO add raw result instead of ids.
    }

    //TODO add maxScore, numFound, start

    result.setIds(ids);
  }
}
