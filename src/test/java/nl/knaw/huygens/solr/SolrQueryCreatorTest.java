package nl.knaw.huygens.solr;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.DefaultFacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.FacetParameter;
import nl.knaw.huygens.facetedsearch.model.NoSuchFieldInIndexException;
import nl.knaw.huygens.facetedsearch.model.QueryOptimizer;
import nl.knaw.huygens.facetedsearch.model.SortDirection;
import nl.knaw.huygens.facetedsearch.model.SortParameter;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;

public class SolrQueryCreatorTest {
  private DefaultFacetedSearchParameters searchParameters;
  private FacetedSearchParametersValidator validator;
  private SolrQueryCreator instance;

  @Before
  public void setUp() {
    searchParameters = new DefaultFacetedSearchParameters();
    instance = new SolrQueryCreator();
    validator = mock(FacetedSearchParametersValidator.class);
    when(validator.facetExists(any(FacetParameter.class))).thenReturn(true);
    when(validator.facetFieldExists(anyString())).thenReturn(true);
    when(validator.sortParameterExists(any(SortParameter.class))).thenReturn(true);
    when(validator.resultFieldExists(anyString())).thenReturn(true);
  }

  @Test
  public void testCreateSearchQueryTerm() throws NoSuchFieldInIndexException {
    searchParameters.setTerm("test");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertEquals("testSearchField:test", query.getQuery());
  }

  @Test
  public void testCreateSearchQueryTermMultipleSearchFields() throws NoSuchFieldInIndexException {
    searchParameters.setTerm("test");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField", "testSearchField1"));

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertEquals("testSearchField:test testSearchField1:test", query.getQuery());
  }

  @Test
  public void testCreateSearchQueryEmptyTerm() throws NoSuchFieldInIndexException {
    searchParameters.setTerm("");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertEquals("*:*", query.getQuery());

  }

  @Test
  public void testCreateSearchQueryMultipleTerms() throws NoSuchFieldInIndexException {
    searchParameters.setTerm("test1 test2");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertEquals("testSearchField:(test1 test2)", query.getQuery());
  }

  @Test
  public void testCreateSearchQueryTermSpecialCharacter() throws NoSuchFieldInIndexException {
    searchParameters.setTerm("-test123");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    searchParameters.setFuzzy(true);

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertThat(query.getQuery(), startsWith("testSearchField:-test123"));
  }

  @Test
  public void testCreateSearchQueryTermFuzzy() throws NoSuchFieldInIndexException {
    searchParameters.setTerm("test123");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    searchParameters.setFuzzy(true);

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertThat(query.getQuery(), startsWith("testSearchField:test123~"));
  }

  @Test
  public void testCreateSearchQueryMultipleTermsFuzzy() throws NoSuchFieldInIndexException {
    searchParameters.setTerm("test test2");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    searchParameters.setFuzzy(true);

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertThat(query.getQuery(), containsString("test~"));
    assertThat(query.getQuery(), containsString("test2~"));
  }

  @Test
  public void testCreateSearchQueryTermNoFullTextSearchFields() throws NoSuchFieldInIndexException {
    searchParameters.setTerm("test");

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertEquals("*:*", query.getQuery());
  }

  @Test
  public void testCreateSearchQueryTermAndFacet() throws NoSuchFieldInIndexException {
    searchParameters.setTerm("test1");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    searchParameters.setFacetValues(Lists.newArrayList(createFacetParameter("facetField", Lists.newArrayList("facetValue"))));

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertEquals("+testSearchField:test1 +facetField:facetValue", query.getQuery());
  }

  @Test
  public void testCreateSearchQuerySortField() throws NoSuchFieldInIndexException {
    String sortFieldName = "name";
    searchParameters.setSortParameters(Lists.newArrayList(createSortParameter(sortFieldName, SortDirection.ASCENDING)));

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    SortClause sortClause = query.getSorts().get(0);

    assertEquals(ORDER.asc, sortClause.getOrder());
    assertEquals(sortFieldName, sortClause.getItem());
  }

  @Test
  public void testCreateSearchQueryMultipleSortFields() throws NoSuchFieldInIndexException {
    String sortFieldName1 = "name";
    String sortFieldName2 = "name2";
    searchParameters.setSortParameters(Lists.newArrayList(createSortParameter(sortFieldName1, SortDirection.ASCENDING), createSortParameter(sortFieldName2, SortDirection.DESCENDING)));

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    SortClause sortClause1 = query.getSorts().get(0);
    SortClause sortClause2 = query.getSorts().get(1);

    assertEquals(ORDER.asc, sortClause1.getOrder());
    assertEquals(sortFieldName1, sortClause1.getItem());

    assertEquals(ORDER.desc, sortClause2.getOrder());
    assertEquals(sortFieldName2, sortClause2.getItem());
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testCreateSearchQuerySortFieldDoesNotExist() throws NoSuchFieldInIndexException {
    String sortFieldName = "name";
    searchParameters.setSortParameters(Lists.newArrayList(createSortParameter(sortFieldName, SortDirection.ASCENDING)));
    when(validator.sortParameterExists(any(SortParameter.class))).thenReturn(false);

    instance.createSearchQuery(searchParameters, validator);
  }

  @Test
  public void testCreateSearchQueryFacet() throws NoSuchFieldInIndexException {
    searchParameters.setFacetValues(Lists.newArrayList(createFacetParameter("facetField", Lists.newArrayList("facetValue"))));

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertEquals("+facetField:facetValue", query.getQuery().trim());
  }

  @Test
  public void testCreateSearchQueryRangeFacet() throws NoSuchFieldInIndexException {
    FacetParameter rangeFacet = new FacetParameter();
    rangeFacet.setName("facetField");
    rangeFacet.setLowerLimit(20130101);
    rangeFacet.setUpperLimit(20140101);
    searchParameters.setFacetValues(Lists.newArrayList(rangeFacet));

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertEquals("+facetField:[20130101 TO 20140101]", query.getQuery().trim());
  }

  @Test
  public void testCreateSearchQueryMultipleFacets() throws NoSuchFieldInIndexException {
    FacetParameter facet1 = createFacetParameter("facetField", Lists.newArrayList("facetValue"));
    FacetParameter facet2 = createFacetParameter("facetField2", Lists.newArrayList("facetValue2"));
    searchParameters.setFacetValues(Lists.newArrayList(facet1, facet2));

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertEquals("+facetField:facetValue +facetField2:facetValue2", query.getQuery().trim());
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testCreateSearchQueryFacetDoesNotExist() throws NoSuchFieldInIndexException {
    when(validator.facetExists(any(FacetParameter.class))).thenReturn(false);
    searchParameters.setFacetValues(Lists.newArrayList(createFacetParameter("test", Lists.newArrayList("value"))));

    instance.createSearchQuery(searchParameters, validator);

  }

  @Ignore
  @Test
  public void testCreateSearchQueryRangeFacetHasWrongValue() {
    fail("Yet to be implemented.");
  }

  @Test
  public void testCreateSearchQueryResultField() throws NoSuchFieldInIndexException {
    searchParameters.setResultFields(Lists.newArrayList("resultField"));

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertEquals("resultField", query.getFields());
  }

  @Test
  public void testCreateSearchQueryMultipleResultFields() throws NoSuchFieldInIndexException {
    searchParameters.setResultFields(Lists.newArrayList("resultField", "resultField1"));

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    // to make the test independent of format changed ignore all the white spaces.
    assertEquals("resultField,resultField1", query.getFields().replaceAll(" ", ""));
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testCreateSearchQueryResultFieldDoesNotExist() throws NoSuchFieldInIndexException {
    searchParameters.setResultFields(Lists.newArrayList("resultField"));
    when(validator.resultFieldExists(anyString())).thenReturn(false);

    instance.createSearchQuery(searchParameters, validator);
  }

  @Test
  public void testCreateSearchQueryFacetFields() throws NoSuchFieldInIndexException {
    searchParameters.setFacetFields(Lists.newArrayList("facetField"));

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertArrayEquals(new String[] { "facetField" }, query.getFacetFields());
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testCreateSearchQueryFacetFieldDoesNotExist() throws NoSuchFieldInIndexException {
    searchParameters.setFacetFields(Lists.newArrayList("facetField"));
    when(validator.facetFieldExists(anyString())).thenReturn(false);

    instance.createSearchQuery(searchParameters, validator);
  }

  @Test
  public void testCreateSearchQueryDefaultQueryOptimizer() throws NoSuchFieldInIndexException {
    searchParameters.setQueryOptimizer(new QueryOptimizer());

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertEquals(50000, (int) query.getRows());
    assertEquals(10000, query.getFacetLimit());
    assertEquals(1, query.getFacetMinCount());
  }

  @Test
  public void testCreateSearchQueryCustomQueryOptimizer() throws NoSuchFieldInIndexException {
    QueryOptimizer queryOptimizer = new QueryOptimizer();
    queryOptimizer.setFacetLimit(60);
    queryOptimizer.setFacetMinCount(10);
    queryOptimizer.setRows(5000);
    searchParameters.setQueryOptimizer(queryOptimizer);

    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    assertEquals(5000, (int) query.getRows());
    assertEquals(60, query.getFacetLimit());
    assertEquals(10, query.getFacetMinCount());
  }

  @Test
  public void testCreateSearchQueryNoQueryOptimizer() throws NoSuchFieldInIndexException {
    SolrQuery query = instance.createSearchQuery(searchParameters, validator);

    //Solr defaults
    assertEquals(null, query.getRows());
    assertEquals(25, query.getFacetLimit());
    assertEquals(1, query.getFacetMinCount());
  }

  private FacetParameter createFacetParameter(String name, List<String> values) {
    FacetParameter facetParameter = new FacetParameter();
    facetParameter.setName(name);
    facetParameter.setValues(values);
    return facetParameter;
  }

  private SortParameter createSortParameter(String name, SortDirection direction) {
    SortParameter sortParameter = new SortParameter();
    sortParameter.setFieldname(name);
    sortParameter.setDirection(direction);

    return sortParameter;
  }
}
