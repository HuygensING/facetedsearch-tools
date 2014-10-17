package nl.knaw.huygens.facetedsearch.query;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.parameters.QueryOptimizer;

import org.apache.solr.client.solrj.SolrQuery;

public class QueryOptimizerBuilder implements SolrQueryBuilder {

  @Override
  public <T extends FacetedSearchParameters<T>> void build(SolrQuery query, FacetedSearchParameters<T> searchParameters) {
    QueryOptimizer optimizer = searchParameters.getQueryOptimizer();

    if (optimizer != null) {
      query.setRows(optimizer.getRows());
      query.setFacetLimit(optimizer.getFacetLimit());
      query.setFacetMinCount(optimizer.getFacetMinCount());
    }
  }

}
