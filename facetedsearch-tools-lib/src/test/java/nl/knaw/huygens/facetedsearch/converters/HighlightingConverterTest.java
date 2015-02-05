package nl.knaw.huygens.facetedsearch.converters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

public class HighlightingConverterTest {
	private HighlightingConverter instance;
	private FacetedSearchResult searchResultMock;
	private QueryResponse queryResponseMock;

	@Before
	public void setUp() {
		instance = new HighlightingConverter();
		searchResultMock = mock(FacetedSearchResult.class);
		queryResponseMock = mock(QueryResponse.class);
	}

	@Test
	public void testConvert() {
		Map<String, Map<String, List<String>>> highlightingMap = Maps.newHashMap();

		when(queryResponseMock.getHighlighting()).thenReturn(highlightingMap);

		// action
		instance.convert(searchResultMock, queryResponseMock);

		//verify
		verify(searchResultMock).setHighlighting(highlightingMap);
	}
}
