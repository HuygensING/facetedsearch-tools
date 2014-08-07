package nl.knaw.huygens.facetedsearch.converters;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

/**
 *  Adds the meta data of the {@code QueryResponse} to the {@code FacetedSearchResult}. 
 */
public class ResultMetaDataConverter implements QueryResponseConverter {

  @Override
  public void convert(FacetedSearchResult result, QueryResponse queryResponse) {
    SolrDocumentList documentList = queryResponse.getResults();

    result.setMaxScore(documentList.getMaxScore());
    result.setNumFound(documentList.getNumFound());
    result.setOffset(documentList.getStart());
  }

}
