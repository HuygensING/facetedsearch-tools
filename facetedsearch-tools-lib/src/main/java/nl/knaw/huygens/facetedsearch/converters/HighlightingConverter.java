package nl.knaw.huygens.facetedsearch.converters;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;

/**
 * Adds the highlights from the {@code QueryResponse} to the {@code FacetedSearchResult}.
 */
public class HighlightingConverter implements QueryResponseConverter {

	@Override
	public void convert(FacetedSearchResult result, QueryResponse queryResponse) {
		result.setHighlighting(queryResponse.getHighlighting());
	}

}
