package nl.knaw.huygens.facetedsearch.model;

import static nl.knaw.huygens.facetedsearch.model.RangeFacetMatcher.rangeFacetHasCharacteristics;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import nl.knaw.huygens.facetedsearch.definition.FacetDefinition;
import nl.knaw.huygens.facetedsearch.definition.RangeFacetDefinition;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;

public class RangeFacetDefinitionTest {
  private String facetName = "name";
  private String facetTitle = "title";

  @Test
  public void testAddFacetToResultWithRangeFacet() {
    // setup instance
    long lowerLimit = 20l;
    long upperLimit = 100l;
    String upperLimitField = "upperLimitField";
    String lowerLimitField = "lowerLimitField";
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
}
