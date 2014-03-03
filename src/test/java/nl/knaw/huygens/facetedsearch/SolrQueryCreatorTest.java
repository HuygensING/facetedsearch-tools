package nl.knaw.huygens.facetedsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.DefaultFacetParameter;
import nl.knaw.huygens.facetedsearch.model.DefaultFacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.FacetField;
import nl.knaw.huygens.facetedsearch.model.FacetParameter;
import nl.knaw.huygens.facetedsearch.model.HighlightingOptions;
import nl.knaw.huygens.facetedsearch.model.QueryOptimizer;
import nl.knaw.huygens.facetedsearch.model.RangeFacetField;
import nl.knaw.huygens.facetedsearch.model.RangeParameter;
import nl.knaw.huygens.facetedsearch.model.SortDirection;
import nl.knaw.huygens.facetedsearch.model.SortParameter;

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
    instance = new SolrQueryCreator();
  }

  @Test
  public void testCreateSearchQueryTerm() {
    searchParameters.setTerm("test");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));

    SolrQuery query = instance.createSearchQuery(searchParameters);

    assertEquals("testSearchField:test", query.getQuery());
  }

  @Test
  public void testCreateSearchQueryTermMultipleSearchFields() {
    searchParameters.setTerm("test");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField", "testSearchField1"));

    SolrQuery query = instance.createSearchQuery(searchParameters);

    assertEquals("testSearchField:test testSearchField1:test", query.getQuery());
  }

  @Test
  public void testCreateSearchQueryEmptyTerm() {
    searchParameters.setTerm("");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));

    SolrQuery query = instance.createSearchQuery(searchParameters);

    assertEquals("*:*", query.getQuery());

  }

  @Test
  public void testCreateSearchQueryMultipleTerms() {
    searchParameters.setTerm("test1 test2");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));

    SolrQuery query = instance.createSearchQuery(searchParameters);

    assertEquals("testSearchField:(test1 test2)", query.getQuery());
  }

  @Test
  public void testCreateSearchQueryTermSpecialCharacter() {
    searchParameters.setTerm("-test123");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    searchParameters.setFuzzy(true);

    SolrQuery query = instance.createSearchQuery(searchParameters);

    assertThat(query.getQuery(), startsWith("testSearchField:-test123"));
  }

  @Test
  public void testCreateSearchQueryTermFuzzy() {
    searchParameters.setTerm("test123");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    searchParameters.setFuzzy(true);

    SolrQuery query = instance.createSearchQuery(searchParameters);

    assertThat(query.getQuery(), startsWith("testSearchField:test123~"));
  }

  @Test
  public void testCreateSearchQueryMultipleTermsFuzzy() {
    searchParameters.setTerm("test test2");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    searchParameters.setFuzzy(true);

    SolrQuery query = instance.createSearchQuery(searchParameters);

    assertThat(query.getQuery(), containsString("test~"));
    assertThat(query.getQuery(), containsString("test2~"));
  }

  @Test
  public void testCreateSearchQueryTermNoFullTextSearchFields() {
    searchParameters.setTerm("test");

    SolrQuery query = instance.createSearchQuery(searchParameters);

    assertEquals("*:*", query.getQuery());
  }

  @Test
  public void testCreateSearchQueryTermAndFacet() {
    searchParameters.setTerm("test1");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    searchParameters.setFacetParameters(Lists.newArrayList(createFacetParameter("facetField", Lists.newArrayList("facetValue"))));

    SolrQuery query = instance.createSearchQuery(searchParameters);

    assertEquals("+testSearchField:test1 +facetField:facetValue", query.getQuery());
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
  public void testCreateSearchQueryFacet() {
    searchParameters.setFacetParameters(Lists.newArrayList(createFacetParameter("facetField", Lists.newArrayList("facetValue"))));

    SolrQuery query = instance.createSearchQuery(searchParameters);

    assertEquals("+facetField:facetValue", query.getQuery().trim());
  }

  @Test
  public void testCreateSearchQueryRangeFacet() {
    FacetParameter rangeFacet = createRangeFacetParameter("facetField", 20130101, 20140101);
    searchParameters.setFacetParameters(Lists.newArrayList(rangeFacet));

    SolrQuery query = instance.createSearchQuery(searchParameters);

    assertEquals("+facetField:[20130101 TO 20140101]", query.getQuery().trim());
  }

  @Test
  public void testCreateSearchQueryMultipleFacets() {
    FacetParameter facet1 = createFacetParameter("facetField", Lists.newArrayList("facetValue"));
    FacetParameter facet2 = createFacetParameter("facetField2", Lists.newArrayList("facetValue2"));
    searchParameters.setFacetParameters(Lists.newArrayList(facet1, facet2));

    SolrQuery query = instance.createSearchQuery(searchParameters);

    assertEquals("+facetField:facetValue +facetField2:facetValue2", query.getQuery().trim());
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
    searchParameters.setFacetFields(Lists.newArrayList(new FacetField("facetField")));

    SolrQuery query = instance.createSearchQuery(searchParameters);

    assertArrayEquals(new String[] { "facetField" }, query.getFacetFields());
    assertEquals(true, query.getBool(FacetParams.FACET)); // this boolean is set automagically
  }

  @Test
  public void testCreateSearchQueryRangeFacetFields() {
    List<FacetField> facetFields = Lists.<FacetField> newArrayList();
    facetFields.add(new RangeFacetField("rangeField", "lowerField", "upperField"));
    searchParameters.setFacetFields(facetFields);

    SolrQuery query = instance.createSearchQuery(searchParameters);

    assertArrayEquals(new String[] { "lowerField", "upperField" }, query.getFacetFields());
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
  public void testCreateSearchQueryNoQueryOptimizer() {
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
    assertEquals(null, query.get(HighlightParams.Q));// when it is filled it overrides the query.

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
    assertEquals(null, query.get(HighlightParams.Q)); // when it is filled it overrides the query.
  }

  @Test
  public void testCreateSearchQueryNoHightlightOptions() {
    SolrQuery query = instance.createSearchQuery(searchParameters);

    assertEquals(false, query.getHighlight());
  }

  private FacetParameter createFacetParameter(String name, List<String> values) {
    DefaultFacetParameter facetParameter = new DefaultFacetParameter(name, values);

    return facetParameter;
  }

  private FacetParameter createRangeFacetParameter(String name, int lowerLimit, int upperLimit) {
    RangeParameter rangeFacet = new RangeParameter(name, lowerLimit, upperLimit);

    return rangeFacet;
  }

  private SortParameter createSortParameter(String name, SortDirection direction) {
    SortParameter sortParameter = new SortParameter();
    sortParameter.setFieldname(name);
    sortParameter.setDirection(direction);

    return sortParameter;
  }
}
