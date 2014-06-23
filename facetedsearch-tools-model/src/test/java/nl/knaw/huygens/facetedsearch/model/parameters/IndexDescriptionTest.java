package nl.knaw.huygens.facetedsearch.model.parameters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import nl.knaw.huygens.facetedsearch.model.FacetDefinition;
import nl.knaw.huygens.facetedsearch.model.RangeFacetDefinition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

public class IndexDescriptionTest {

  @Mock
  private List<String> sortFieldListMock;
  @Mock
  List<String> allIndexedFieldsMock;

  @Mock
  private Map<String, FacetDefinition> facetDefinitionMapMock;

  private IndexDescription instance;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    instance = new IndexDescription(facetDefinitionMapMock, sortFieldListMock, allIndexedFieldsMock);
  }

  @Test
  public void testDoesFacetFieldExist() {
    // setup
    final String facetName = "testFacet";
    final FacetField facetField = new FacetField(facetName);

    // action
    instance.doesFacetFieldExist(facetField);

    // verify
    verify(facetDefinitionMapMock).containsKey(facetName);
  }

  @Test
  public void testDoesFacetParameterExist() {
    // setup
    final String facetName = "testFacet";
    final FacetParameter facetParameter = new DefaultFacetParameter(facetName, null);

    // action
    instance.doesFacetParameterExist(facetParameter);

    // verify
    facetDefinitionMapMock.containsKey(facetName);
  }

  @Test
  public void testDoesResultFieldFacetExist() {
    // setup
    String resultFieldName = "testField";

    // action
    instance.doesResultFieldExist(resultFieldName);

    // verify
    verify(allIndexedFieldsMock).contains(resultFieldName);
  }

  @Test
  public void testDoesSortParameterExist() {
    // setup
    final String fieldName = "test";
    final SortParameter sortParameter = new SortParameter(fieldName, SortDirection.ASCENDING);

    // action
    instance.doesSortParameterExist(sortParameter);

    // verify
    verify(sortFieldListMock).contains(fieldName);
  }

  @Test
  public void testFindFacetFields() {
    // setup
    final String facetName = "test";
    FacetDefinition facetDefinition = new FacetDefinition().setName(facetName);
    final String lowerLimitField = "lowerLimitField";
    final String upperLimitField = "upperLimitField";
    RangeFacetDefinition rangeDefinition = new RangeFacetDefinition() //
        .setLowerLimitField(lowerLimitField) //
        .setUpperLimitField(upperLimitField);

    when(facetDefinitionMapMock.values()).thenReturn(Lists.newArrayList(facetDefinition, rangeDefinition));

    // action
    String[] facetFields = instance.findFacetFields();

    // verify
    verify(facetDefinitionMapMock).values();
    assertThat(Lists.newArrayList(facetFields), containsInAnyOrder(facetName, lowerLimitField, upperLimitField));
  }
}
