package nl.knaw.huygens.facetedsearch;

import nl.knaw.huygens.facetedsearch.converters.FacetListConverter;
import nl.knaw.huygens.facetedsearch.converters.HighlightingConverter;
import nl.knaw.huygens.facetedsearch.converters.QueryResponseConverter;
import nl.knaw.huygens.facetedsearch.converters.ResultConverter;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.parameters.IndexDescription;

import org.apache.solr.client.solrj.response.QueryResponse;

public class SearchResultCreator {

	private final QueryResponseConverter[] converters;

	public SearchResultCreator(IndexDescription indexDescription) {
		this(new FacetListConverter(indexDescription), new ResultConverter(), new HighlightingConverter());
	}

	public SearchResultCreator(QueryResponseConverter... converters) {
		this.converters = converters;
	}

	public <T extends FacetedSearchParameters<T>> FacetedSearchResult build(QueryResponse queryResponse, FacetedSearchParameters<T> searchParameters) {
		final FacetedSearchResult result = createFacetedSearchResult();

		for (QueryResponseConverter converter : converters) {
			converter.convert(result, queryResponse);
		}

		result.addSearchParameters(searchParameters);

		return result;
	}

	protected FacetedSearchResult createFacetedSearchResult() {
		return new FacetedSearchResult();
	}

}
