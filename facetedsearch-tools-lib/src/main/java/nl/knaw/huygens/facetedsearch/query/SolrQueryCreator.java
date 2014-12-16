package nl.knaw.huygens.facetedsearch.query;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.parameters.IndexDescription;

import org.apache.solr.client.solrj.SolrQuery;

public class SolrQueryCreator {
  private SolrQueryBuilder[] builders;

  public SolrQueryCreator(IndexDescription indexDescription) {
    this(new QueryStringBuilder(indexDescription), new ResultFieldBuilder(), new FacetFieldBuilder(), //
        new SortBuilder(), new HighlightingBuilder(), new QueryOptimizerBuilder());
  }

  public SolrQueryCreator(SolrQueryBuilder... builders) {
    this.builders = builders;

  }

  public <T extends FacetedSearchParameters<T>> SolrQuery createSearchQuery(FacetedSearchParameters<T> searchParameters) {
    SolrQuery query = new SolrQuery();
    for (SolrQueryBuilder builder : builders) {
      builder.build(query, searchParameters);
    }

    return query;
  }

}
