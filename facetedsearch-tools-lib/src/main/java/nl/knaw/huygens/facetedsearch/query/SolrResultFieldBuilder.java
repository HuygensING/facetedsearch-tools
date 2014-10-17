package nl.knaw.huygens.facetedsearch.query;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetedSearchParameters;

import org.apache.solr.client.solrj.SolrQuery;

public class SolrResultFieldBuilder implements SolrQueryBuilder {

  @Override
  public <T extends FacetedSearchParameters<T>> void build(SolrQuery query, FacetedSearchParameters<T> searchParameters) {
    query.setFields(searchParameters.getResultFields().toArray(new String[0]));
  }
}
