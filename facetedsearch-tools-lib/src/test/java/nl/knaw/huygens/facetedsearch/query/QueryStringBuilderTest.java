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
import nl.knaw.huygens.facetedsearch.model.parameters.FullTextSearchParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.RangeParameter;

import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class QueryStringBuilderTest {

  private QueryStringBuilder instance;
  private SolrQuery query;
  private DefaultFacetedSearchParameters searchParameters;

  @Before
  public void setUp() {
    query = new SolrQuery();
    searchParameters = new DefaultFacetedSearchParameters();

    instance = new QueryStringBuilder();
  }

  @Test
  public void testBuildWithTerm() {
    searchParameters.setTerm("test");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));

    instance.build(query, searchParameters);

    assertEquals("testSearchField:test", query.getQuery());
  }

  @Test
  public void testBuildWithTermMultipleSearchFields() {
    searchParameters.setTerm("test");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField", "testSearchField1"));

    instance.build(query, searchParameters);

    assertEquals("testSearchField:test testSearchField1:test", query.getQuery());
  }

  @Test
  public void testBuildWithTermMultipleSearchFieldsAndFacet() {
    FacetParameter facet1 = createFacetParameter("facetField", Lists.newArrayList("facetValue"));
    searchParameters.setTerm("test");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField", "testSearchField1"));
    searchParameters.setFacetParameters(Lists.newArrayList(facet1));

    instance.build(query, searchParameters);

    assertEquals("+(testSearchField:test testSearchField1:test) +facetField:facetValue", query.getQuery());
  }

  @Test
  public void testBuildWithEmptyTerm() {
    searchParameters.setTerm("");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));

    instance.build(query, searchParameters);

    assertThat(query.getQuery(), is(equalTo("*:*")));

  }

  @Test
  public void testBuildWithMultipleTerms() {
    searchParameters.setTerm("test1 test2");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));

    instance.build(query, searchParameters);

    assertEquals("testSearchField:(test1 test2)", query.getQuery());
  }

  @Test
  public void testBuildWithTermSpecialCharacter() {
    searchParameters.setTerm("-test123");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    searchParameters.setFuzzy(true);

    instance.build(query, searchParameters);

    assertThat(query.getQuery(), startsWith("testSearchField:-test123"));
  }

  @Test
  public void testBuildWithTermFuzzy() {
    searchParameters.setTerm("test123");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    searchParameters.setFuzzy(true);

    instance.build(query, searchParameters);

    assertThat(query.getQuery(), startsWith("testSearchField:test123~"));
  }

  @Test
  public void testBuildWithMultipleTermsFuzzy() {
    searchParameters.setTerm("test test2");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    searchParameters.setFuzzy(true);

    instance.build(query, searchParameters);

    assertThat(query.getQuery(), containsString("test~"));
    assertThat(query.getQuery(), containsString("test2~"));
  }

  @Test
  public void testBuildWithTermNoFullTextSearchFields() {
    searchParameters.setTerm("test");

    instance.build(query, searchParameters);

    assertEquals("*:*", query.getQuery());
  }

  @Test
  public void testBuildWithTermAndFacet() {
    searchParameters.setTerm("test1");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("testSearchField"));
    searchParameters.setFacetParameters(Lists.newArrayList(createFacetParameter("facetField", Lists.newArrayList("facetValue"))));

    instance.build(query, searchParameters);

    assertEquals("+(testSearchField:test1) +facetField:facetValue", query.getQuery());
  }

  @Test
  public void testBuildWithRangeFacet() {
    FacetParameter rangeFacet = createRangeFacetParameter("facetField", 20130101, 20140101);
    searchParameters.setFacetParameters(Lists.newArrayList(rangeFacet));

    instance.build(query, searchParameters);

    assertEquals("+facetField:[20130101 TO 20140101]", query.getQuery().trim());
  }

  @Test
  public void testBuildWithFacet() {
    FacetParameter facetParameter = createFacetParameter("facetField", Lists.newArrayList("facetValue"));
    searchParameters.setFacetParameters(Lists.newArrayList(facetParameter));

    instance.build(query, searchParameters);

    assertEquals("+facetField:facetValue", query.getQuery().trim());
  }

  @Test
  public void testBuildWithMultipleFacets() {
    FacetParameter facet1 = createFacetParameter("facetField", Lists.newArrayList("facetValue"));
    FacetParameter facet2 = createFacetParameter("facetField2", Lists.newArrayList("facetValue2"));
    searchParameters.setFacetParameters(Lists.newArrayList(facet1, facet2));

    instance.build(query, searchParameters);

    assertEquals("+facetField:facetValue +facetField2:facetValue2", query.getQuery().trim());
  }

  @Test
  public void testBuildWithFullTextParameter() {
    FullTextSearchParameter ftsp = new FullTextSearchParameter("fullTextField1", "test");
    searchParameters.setFullTextSearchParameters(Lists.newArrayList(ftsp));
    searchParameters.setFullTextSearchFields(Lists.newArrayList("fullTextField1"));

    instance.build(query, searchParameters);

    assertThat(query.getQuery(), is(equalTo("fullTextField1:test")));
  }

  @Test
  public void testBuildWithMultipleFullTextParameters() {
    FullTextSearchParameter ftsp = new FullTextSearchParameter("fullTextField1", "test");
    FullTextSearchParameter ftsp2 = new FullTextSearchParameter("fullTextField2", "other");
    searchParameters.setFullTextSearchFields(Lists.newArrayList("fullTextField1", "fullTextField2"));
    searchParameters.setFullTextSearchParameters(Lists.newArrayList(ftsp, ftsp2));

    instance.build(query, searchParameters);

    assertThat(query.getQuery(), is(equalTo("fullTextField1:test fullTextField2:other")));
  }

  @Test
  public void testBuildWithFullTextParametersAndTerm() {
    FullTextSearchParameter ftsp = new FullTextSearchParameter("fullTextField1", "test");
    searchParameters.setFullTextSearchParameters(Lists.newArrayList(ftsp));
    searchParameters.setFullTextSearchFields(Lists.newArrayList("fullTextField1"));
    searchParameters.setTerm("term");

    instance.build(query, searchParameters);

    assertThat(query.getQuery(), is(equalTo("fullTextField1:test term")));
  }

  @Test
  public void testBuildWithFullTextParametersAndFacet() {
    FullTextSearchParameter ftsp = new FullTextSearchParameter("fullTextField1", "test");
    searchParameters.setFullTextSearchParameters(Lists.newArrayList(ftsp));
    searchParameters.setFullTextSearchFields(Lists.newArrayList("fullTextField1"));
    searchParameters.setFacetParameters(Lists.newArrayList(createFacetParameter("facetField", Lists.newArrayList("facetValue"))));

    instance.build(query, searchParameters);

    assertEquals("+(fullTextField1:test) +facetField:facetValue", query.getQuery());

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
