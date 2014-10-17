package nl.knaw.huygens.facetedsearch.query;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.parameters.SortParameter;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;

public class SortBuilder implements SolrQueryBuilder {

  @Override
  /**
   * Sets the sort criteria for the query.
   * @param query the {@code SolrQuery} that should be sorted.
   * @param searchParameters that container of the {@code SortParameter}s.
   */
  public <T extends FacetedSearchParameters<T>> void build(SolrQuery query, FacetedSearchParameters<T> searchParameters) {
    List<SortParameter> sortParameters = searchParameters.getSortParameters();
    for (SortParameter sortParameter : sortParameters) {
      query.addSort(createSortClause(sortParameter));
    }
  }

  private SortClause createSortClause(SortParameter sortParameter) {
    ORDER order = ORDER.valueOf(sortParameter.getDirection().toString());
    return new SortClause(sortParameter.getFieldName(), order);
  }

}
