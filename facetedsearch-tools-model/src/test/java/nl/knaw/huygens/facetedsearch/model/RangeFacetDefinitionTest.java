package nl.knaw.huygens.facetedsearch.model;

import static nl.knaw.huygens.facetedsearch.model.RangeFacetMatcher.rangeFacetHasCharacteristics;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;

public class RangeFacetDefinitionTest {
  private String facetName = "name";
  private String facetTitle = "title";
  private String upperLimitField = "upperLimitField";
  private String lowerLimitField = "lowerLimitField";

  @Test
  public void testAddFacetToResultWithRangeFacet() {
    // setup instance
    long lowerLimit = 20l;
    long upperLimit = 100l;
    FacetDefinition instance = new RangeFacetDefinition()//
        .setUpperLimitField(upperLimitField)//
        .setLowerLimitField(lowerLimitField)//
        .setName(facetName)//
        .setTitle(facetTitle)//
        .setType(FacetType.RANGE);

    // mocks
    QueryResponse queryResponseMock = mock(QueryResponse.class);
    FacetedSearchResult searchResultMock = mock(FacetedSearchResult.class);

    // when
    FacetField lowerFacetField = createFacetField(facetName, 30, 50, lowerLimit);
    when(queryResponseMock.getFacetField(lowerLimitField)).thenReturn(lowerFacetField);
    FacetField upperFacetField = createFacetField(facetName, 60, upperLimit, 80);
    when(queryResponseMock.getFacetField(upperLimitField)).thenReturn(upperFacetField);

    // action
    instance.addFacetToResult(searchResultMock, queryResponseMock);

    // verify
    verify(queryResponseMock).getFacetField(lowerLimitField);
    verify(queryResponseMock).getFacetField(upperLimitField);
    verify(searchResultMock).addFacet(argThat(rangeFacetHasCharacteristics(facetName, facetTitle, lowerLimit, upperLimit)));

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
        .setLowerLimitField(lowerLimitField) //
        .setUpperLimitField(upperLimitField);

    Collection<String> actualFields = instance.getFields();

    assertThat(actualFields, containsInAnyOrder(lowerLimitField, upperLimitField));
  }
}
