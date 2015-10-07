package nl.knaw.huygens.facetedsearch.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import nl.knaw.huygens.facetedsearch.model.parameters.DefaultFacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetField;
import nl.knaw.huygens.facetedsearch.model.parameters.HighlightingOptions;
import nl.knaw.huygens.facetedsearch.model.parameters.IndexDescription;
import nl.knaw.huygens.facetedsearch.model.parameters.QueryOptimizer;
import nl.knaw.huygens.facetedsearch.model.parameters.SortDirection;
import nl.knaw.huygens.facetedsearch.model.parameters.SortParameter;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.common.params.FacetParams;
import org.apache.solr.common.params.HighlightParams;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class SolrQueryCreatorTest {
	private DefaultFacetedSearchParameters searchParameters;
	private SolrQueryCreator instance;

	@Before
	public void setUp() {
		searchParameters = new DefaultFacetedSearchParameters();
		instance = new SolrQueryCreator(mock(IndexDescription.class));
	}

	@Test
	public void testCreateSearchQuerySortField() {
		String sortFieldName = "name";
		searchParameters.setSortParameters(Lists.newArrayList(createSortParameter(sortFieldName, SortDirection.ASCENDING)));

		SolrQuery query = instance.createSearchQuery(searchParameters);

		SortClause sortClause = query.getSorts().get(0);

		assertEquals(ORDER.asc, sortClause.getOrder());
		assertEquals(sortFieldName, sortClause.getItem());
	}

	@Test
	public void testCreateSearchQueryMultipleSortFields() {
		String sortFieldName1 = "name";
		String sortFieldName2 = "name2";
		searchParameters.setSortParameters(Lists.newArrayList(createSortParameter(sortFieldName1, SortDirection.ASCENDING), createSortParameter(sortFieldName2, SortDirection.DESCENDING)));

		SolrQuery query = instance.createSearchQuery(searchParameters);

		SortClause sortClause1 = query.getSorts().get(0);
		SortClause sortClause2 = query.getSorts().get(1);

		assertEquals(ORDER.asc, sortClause1.getOrder());
		assertEquals(sortFieldName1, sortClause1.getItem());

		assertEquals(ORDER.desc, sortClause2.getOrder());
		assertEquals(sortFieldName2, sortClause2.getItem());
	}

	@Test
	public void testCreateSearchQueryResultField() {
		searchParameters.setResultFields(Lists.newArrayList("resultField"));

		SolrQuery query = instance.createSearchQuery(searchParameters);

		assertEquals("resultField", query.getFields());
	}

	@Test
	public void testCreateSearchQueryMultipleResultFields() {
		searchParameters.setResultFields(Lists.newArrayList("resultField", "resultField1"));

		SolrQuery query = instance.createSearchQuery(searchParameters);

		// to make the test independent of format changed ignore all the white spaces.
		assertEquals("resultField,resultField1", query.getFields().replaceAll(" ", ""));
	}

	@Test
	public void testCreateSearchQueryFacetFields() {
		String facetName = "facetField";
		FacetField facetField = new FacetField(facetName);

		ArrayList<FacetField> facetFields = Lists.newArrayList(facetField);
		searchParameters.setFacetFields(facetFields);

		SolrQuery query = instance.createSearchQuery(searchParameters);

		assertThat(query.getFacetFields(), hasItemInArray(facetName));
		assertThat(query.getBool(FacetParams.FACET), is(true));
	}

	@Test
	public void testCreateSearchQueryDefaultQueryOptimizer() {
		searchParameters.setQueryOptimizer(new QueryOptimizer());

		SolrQuery query = instance.createSearchQuery(searchParameters);

		assertEquals(50000, (int) query.getRows());
		assertEquals(10000, query.getFacetLimit());
		assertEquals(1, query.getFacetMinCount());
	}

	@Test
	public void testCreateSearchQueryCustomQueryOptimizer() {
		QueryOptimizer queryOptimizer = new QueryOptimizer();
		queryOptimizer.setFacetLimit(60);
		queryOptimizer.setFacetMinCount(10);
		queryOptimizer.setRows(5000);
		searchParameters.setQueryOptimizer(queryOptimizer);

		SolrQuery query = instance.createSearchQuery(searchParameters);

		assertEquals(5000, (int) query.getRows());
		assertEquals(60, query.getFacetLimit());
		assertEquals(10, query.getFacetMinCount());
	}

	@Test
	public void testCreateSearchQueryNoQueryOptimizerSpecified() {
		SolrQuery query = instance.createSearchQuery(searchParameters);

		// Default QueryOptimizer defaults
		assertEquals(new Integer(50000), query.getRows());
		assertEquals(10000, query.getFacetLimit());
		assertEquals(1, query.getFacetMinCount());
	}

	@Test
	public void testCreateSearchQueryExplicitlySetToNull() {
		searchParameters.setQueryOptimizer(null);
		SolrQuery query = instance.createSearchQuery(searchParameters);

		//Solr defaults
		assertEquals(null, query.getRows());
		assertEquals(25, query.getFacetLimit());
		assertEquals(1, query.getFacetMinCount());
	}

	@Test
	public void testCreateSearchQueryDefaultHightlightOptions() {
		searchParameters.setHighlightingOptions(new HighlightingOptions());
		searchParameters.setTerm("test");
		searchParameters.setFullTextSearchFields(Lists.newArrayList("fullTextSearchField"));

		SolrQuery query = instance.createSearchQuery(searchParameters);

		assertEquals(true, query.getHighlight());
		assertEquals(100, query.getHighlightFragsize());
		assertArrayEquals(new String[] { "fullTextSearchField" }, query.getHighlightFields());
		assertEquals(-1, (int) query.getInt(HighlightParams.MAX_CHARS));
		assertEquals(false, query.getBool(HighlightParams.MERGE_CONTIGUOUS_FRAGMENTS));
		assertEquals("+(fullTextSearchField:test)", query.get(HighlightParams.Q));

	}

	@Test
	public void testCreateSearchQueryCustomHightlightOptions() {
		searchParameters.setHighlightingOptions(new HighlightingOptions());
		searchParameters.setTerm("test");
		searchParameters.setFullTextSearchFields(Lists.newArrayList("fullTextSearchField"));

		SolrQuery query = instance.createSearchQuery(searchParameters);

		assertEquals(true, query.getHighlight());
		assertEquals(100, query.getHighlightFragsize());
		assertArrayEquals(new String[] { "fullTextSearchField" }, query.getHighlightFields());
		assertEquals(-1, (int) query.getInt(HighlightParams.MAX_CHARS));
		assertEquals(false, query.getBool(HighlightParams.MERGE_CONTIGUOUS_FRAGMENTS));
		assertEquals("+(fullTextSearchField:test)", query.get(HighlightParams.Q));
	}

	@Test
	public void testCreateSearchQueryNoHightlightOptions() {
		SolrQuery query = instance.createSearchQuery(searchParameters);

		assertEquals(false, query.getHighlight());
	}

	private SortParameter createSortParameter(String name, SortDirection direction) {
		SortParameter sortParameter = new SortParameter();
		sortParameter.setFieldname(name);
		sortParameter.setDirection(direction);

		return sortParameter;
	}
}
