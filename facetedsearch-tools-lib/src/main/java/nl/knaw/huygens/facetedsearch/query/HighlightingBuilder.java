package nl.knaw.huygens.facetedsearch.query;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.parameters.HighlightingOptions;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.HighlightParams;

public class HighlightingBuilder implements SolrQueryBuilder {

  @Override
  public <T extends FacetedSearchParameters<T>> void build(SolrQuery query, FacetedSearchParameters<T> searchParameters) {
    HighlightingOptions highlightingOptions = searchParameters.getHighlightingOptions();

    if (highlightingOptions != null && searchParameters.getFullTextSearchFields() != null) {
      query.setHighlight(true);
      query.setHighlightFragsize(highlightingOptions.getFragSize());
      query.set(HighlightParams.MAX_CHARS, highlightingOptions.getMaxChars());
      query.set(HighlightParams.MERGE_CONTIGUOUS_FRAGMENTS, highlightingOptions.isMergeContiguous());
      query.set(HighlightParams.FIELDS, searchParameters.getFullTextSearchFields().toArray(new String[0]));
    }

  }

}
