package nl.knaw.huygens.facetedsearch.query;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetedSearchParameters;

import org.apache.solr.client.solrj.SolrQuery;

public class FacetFieldBuilder implements SolrQueryBuilder {

  @Override
  public <T extends FacetedSearchParameters<T>> void build(SolrQuery query, FacetedSearchParameters<T> searchParameters) {
    List<String> facetFields = searchParameters.getFacetFields();
    if (facetFields != null) {
      query.addFacetField(facetFields.toArray(new String[] {}));
    }
  }

}
