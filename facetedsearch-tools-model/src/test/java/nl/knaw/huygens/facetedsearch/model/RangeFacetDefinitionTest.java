package nl.knaw.huygens.facetedsearch.model;

import static nl.knaw.huygens.facetedsearch.model.RangeFacetMatcher.rangeFacetHasCharacteristics;
import static nl.knaw.huygens.facetedsearch.model.parameters.FacetFieldMatcher.rangeFacetFieldLike;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;

public class RangeFacetDefinitionTest {
  private static final long UPPER_LIMIT = 100l;
  private static final long LOWER_LIMIT = 20l;
  private static final String FACET_NAME = "name";
  private static final String FACET_TITLE = "title";
  private static final String UPPER_LIMIT_FIELD = "upperLimitField";
  private static final String LOWER_LIMIT_FIELD = "lowerLimitField";
  private QueryResponse queryResponseMock;
  private FacetedSearchResult searchResultMock;
  private FacetDefinition instance;

  @Before
  public void setUp() {
    queryResponseMock = mock(QueryResponse.class);
    searchResultMock = mock(FacetedSearchResult.class);
    instance = new RangeFacetDefinition()//
        .setUpperLimitField(UPPER_LIMIT_FIELD)//
        .setLowerLimitField(LOWER_LIMIT_FIELD)//
        .setName(FACET_NAME)//
        .setTitle(FACET_TITLE)//
        .setType(FacetType.RANGE);
  }

  @Test
  public void testAddFacetToResultWithRangeFacet() {
    // setup
    addFacetFieldToQueryResponse(FACET_NAME, LOWER_LIMIT_FIELD, queryResponseMock, 30, 50, LOWER_LIMIT);
    addFacetFieldToQueryResponse(FACET_NAME, UPPER_LIMIT_FIELD, queryResponseMock, 60, UPPER_LIMIT, 80);

    // action
    instance.addFacetToResult(searchResultMock, queryResponseMock);

    // verify
    verify(searchResultMock).addFacet(argThat(rangeFacetHasCharacteristics(FACET_NAME, FACET_TITLE, LOWER_LIMIT, UPPER_LIMIT)));
  }

  @Test
  public void testAddFacetToResultWillNotTheFacetIfTheQueryResponseDoesNotContainTheUpperLimitField() {
    // setup
    addFacetFieldToQueryResponse(FACET_NAME, LOWER_LIMIT_FIELD, queryResponseMock, 30, 50, LOWER_LIMIT);

    // action
    instance.addFacetToResult(searchResultMock, queryResponseMock);

    // verify
    verify(searchResultMock, never()).addFacet(any(Facet.class));
  }

  @Test
  public void testAddFacetToResultWillNotTheFacetIfTheQueryResponseDoesNotContainTheLowerLimitField() {
    // setup
    addFacetFieldToQueryResponse(FACET_NAME, UPPER_LIMIT_FIELD, queryResponseMock, 60, UPPER_LIMIT, 80);

    // action
    instance.addFacetToResult(searchResultMock, queryResponseMock);

    // verify
    verify(searchResultMock, never()).addFacet(any(Facet.class));
  }

  private void addFacetFieldToQueryResponse(String facetName, String facetFieldName, QueryResponse queryResponseMock, long... facetValues) {
    FacetField lowerFacetField = createFacetField(facetName, facetValues);
    when(queryResponseMock.getFacetField(facetFieldName)).thenReturn(lowerFacetField);
  }

  private FacetField createFacetField(String name, long... countValues) {
    FacetField facetField = new FacetField(name);

    for (long countValue : countValues) {
      facetField.add("" + countValue, countValue);
    }

    return facetField;
  }

  @Test
  public void testGetFields() {
    RangeFacetDefinition instance = new RangeFacetDefinition() //
        .setLowerLimitField(LOWER_LIMIT_FIELD) //
        .setUpperLimitField(UPPER_LIMIT_FIELD);

    Collection<String> actualFields = instance.getFields();

    assertThat(actualFields, containsInAnyOrder(LOWER_LIMIT_FIELD, UPPER_LIMIT_FIELD));
  }

  @Test
  public void toFacetFieldConvertsTheRangeDefinitionToRangeFacetField() {
    // setup
    FacetDefinition instance = new RangeFacetDefinition() //
        .setLowerLimitField(LOWER_LIMIT_FIELD) //
        .setUpperLimitField(UPPER_LIMIT_FIELD) //
        .setName(FACET_NAME);

    assertThat(instance.toFacetField(), rangeFacetFieldLike(FACET_NAME, LOWER_LIMIT_FIELD, UPPER_LIMIT_FIELD));
  }
}
