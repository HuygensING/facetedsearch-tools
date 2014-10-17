package nl.knaw.huygens.facetedsearch.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.parameters.DefaultFacetParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.DefaultFacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.RangeParameter;

import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class QueryStringBuilderTest {

  private QueryStringBuilder instance;
  private SolrQuery query;
  private DefaultFacetedSearchParameters facetedSearchParameters;

  @Before
  public void setUp() {
    query = new SolrQuery();
    facetedSearchParameters = new DefaultFacetedSearchParameters();

    instance = new QueryStringBuilder();
  }

  @Test
  public void testCreateSearchQueryTerm() {
    facetedSearchParameters.setTerm("test");
    facetedSearchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));

    instance.build(query, facetedSearchParameters);

    assertEquals("testSearchField:test", query.getQuery());
  }

  @Test
  public void testCreateSearchQueryTermMultipleSearchFields() {
    facetedSearchParameters.setTerm("test");
    facetedSearchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField", "testSearchField1"));

    instance.build(query, facetedSearchParameters);

    assertEquals("testSearchField:test testSearchField1:test", query.getQuery());
  }

  @Test
  public void testCreateSearchQueryEmptyTerm() {
    facetedSearchParameters.setTerm("");
    facetedSearchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));

    instance.build(query, facetedSearchParameters);

    assertThat(query.getQuery(), is(equalTo("*:*")));

  }

  @Test
  public void testCreateSearchQueryMultipleTerms() {
    facetedSearchParameters.setTerm("test1 test2");
    facetedSearchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));

    instance.build(query, facetedSearchParameters);

    assertEquals("testSearchField:(test1 test2)", query.getQuery());
  }

  @Test
  public void testCreateSearchQueryTermSpecialCharacter() {
    facetedSearchParameters.setTerm("-test123");
    facetedSearchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    facetedSearchParameters.setFuzzy(true);

    instance.build(query, facetedSearchParameters);

    assertThat(query.getQuery(), startsWith("testSearchField:-test123"));
  }

  @Test
  public void testCreateSearchQueryTermFuzzy() {
    facetedSearchParameters.setTerm("test123");
    facetedSearchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    facetedSearchParameters.setFuzzy(true);

    instance.build(query, facetedSearchParameters);

    assertThat(query.getQuery(), startsWith("testSearchField:test123~"));
  }

  @Test
  public void testCreateSearchQueryMultipleTermsFuzzy() {
    facetedSearchParameters.setTerm("test test2");
    facetedSearchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    facetedSearchParameters.setFuzzy(true);

    instance.build(query, facetedSearchParameters);

    assertThat(query.getQuery(), containsString("test~"));
    assertThat(query.getQuery(), containsString("test2~"));
  }

  @Test
  public void testCreateSearchQueryTermNoFullTextSearchFields() {
    facetedSearchParameters.setTerm("test");

    instance.build(query, facetedSearchParameters);

    assertEquals("*:*", query.getQuery());
  }

  @Test
  public void testCreateSearchQueryTermAndFacet() {
    facetedSearchParameters.setTerm("test1");
    facetedSearchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    facetedSearchParameters.setFacetParameters(Lists.newArrayList(createFacetParameter("facetField", Lists.newArrayList("facetValue"))));

    instance.build(query, facetedSearchParameters);

    assertEquals("+testSearchField:test1 +facetField:facetValue", query.getQuery());
  }

  @Test
  public void testCreateSearchQueryRangeFacet() {
    FacetParameter rangeFacet = createRangeFacetParameter("facetField", 20130101, 20140101);
    facetedSearchParameters.setFacetParameters(Lists.newArrayList(rangeFacet));

    instance.build(query, facetedSearchParameters);

    assertEquals("+facetField:[20130101 TO 20140101]", query.getQuery().trim());
  }

  @Test
  public void testCreateSearchQueryFacet() {
    FacetParameter facetParameter = createFacetParameter("facetField", Lists.newArrayList("facetValue"));
    facetedSearchParameters.setFacetParameters(Lists.newArrayList(facetParameter));

    instance.build(query, facetedSearchParameters);

    assertEquals("+facetField:facetValue", query.getQuery().trim());
  }

  @Test
  public void testCreateSearchQueryMultipleFacets() {
    FacetParameter facet1 = createFacetParameter("facetField", Lists.newArrayList("facetValue"));
    FacetParameter facet2 = createFacetParameter("facetField2", Lists.newArrayList("facetValue2"));
    facetedSearchParameters.setFacetParameters(Lists.newArrayList(facet1, facet2));

    instance.build(query, facetedSearchParameters);

    assertEquals("+facetField:facetValue +facetField2:facetValue2", query.getQuery().trim());
  }

  private FacetParameter createFacetParameter(String name, List<String> values) {
    DefaultFacetParameter facetParameter = new DefaultFacetParameter(name, values);

    return facetParameter;
  }

  private FacetParameter createRangeFacetParameter(String name, int lowerLimit, int upperLimit) {
    RangeParameter rangeFacet = new RangeParameter(name, lowerLimit, upperLimit);

    return rangeFacet;
  }

}
