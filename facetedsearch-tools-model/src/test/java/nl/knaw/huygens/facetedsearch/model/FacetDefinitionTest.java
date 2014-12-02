package nl.knaw.huygens.facetedsearch.model;

import static nl.knaw.huygens.facetedsearch.model.DefaultFacetMatcher.defaultFacethasCharacteristics;
import static nl.knaw.huygens.facetedsearch.model.parameters.FacetFieldMatcher.facetFieldLike;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class FacetDefinitionTest {
  private String facetName = "name";
  private String facetTitle = "title";
  private QueryResponse queryResponseMock;
  private FacetedSearchResult searchResultMock;

  @Before
  public void setUp() {
    queryResponseMock = mock(QueryResponse.class);
    searchResultMock = mock(FacetedSearchResult.class);
  }

  @Test
  // Currently the default implementation.
  public void addFacetToResultWithListFacet() {
    // setup instance
    FacetType facetType = FacetType.LIST;
    FacetDefinition instance = createFacetDefinition(facetType);

    String nameFirstOption = "option1";
    long countFirstOption = 123L;
    String nameSecondOption = "option2";
    long countSecondOption = 12L;
    List<FacetOption> expectedOptions = createExpecetedOptions(nameFirstOption, countFirstOption, nameSecondOption, countSecondOption);

    // when
    setupSolrFacetField(nameFirstOption, countFirstOption, nameSecondOption, countSecondOption, queryResponseMock);

    // action
    instance.addFacetToResult(searchResultMock, queryResponseMock);

    // verify
    verify(searchResultMock).addFacet(argThat(defaultFacethasCharacteristics(facetName, facetTitle, expectedOptions, facetType)));
  }

  @Test
  public void addFacetToResultWithNonListFacet() {
    // setup instance
    FacetType facetType = FacetType.BOOLEAN;
    FacetDefinition instance = createFacetDefinition(facetType);

    String nameFirstOption = "true";
    long countFirstOption = 123L;
    String nameSecondOption = "false";
    long countSecondOption = 12L;
    List<FacetOption> expectedOptions = createExpecetedOptions(nameFirstOption, countFirstOption, nameSecondOption, countSecondOption);

    setupSolrFacetField(nameFirstOption, countFirstOption, nameSecondOption, countSecondOption, queryResponseMock);

    // action
    instance.addFacetToResult(searchResultMock, queryResponseMock);

    // verify
    verify(searchResultMock).addFacet(argThat(defaultFacethasCharacteristics(facetName, facetTitle, expectedOptions, facetType)));
  }

  private FacetDefinition createFacetDefinition(FacetType facetType) {
    FacetDefinition instance = new FacetDefinition()//
        .setName(facetName)//
        .setTitle(facetTitle)//
        .setType(facetType);
    return instance;
  }

  @Test
  public void addFacetDoesNotAddTheFacetWhenTheQueryResponseDoesNotContainTheField() {
    // setup
    FacetDefinition instance = createFacetDefinition(FacetType.LIST);

    // action
    instance.addFacetToResult(searchResultMock, queryResponseMock);

    // verify
    verify(searchResultMock, never()).addFacet(any(Facet.class));
  }

  @Test
  public void addFacetDoesNotAddTheFacetIfTheSolrFacetFieldDoesNotContainAValue() {
    // setup instance
    FacetType facetType = FacetType.LIST;
    FacetDefinition instance = createFacetDefinition(facetType);

    // when
    setupSolrFacetFieldWithoutValues();

    // action
    instance.addFacetToResult(searchResultMock, queryResponseMock);

    // verify
    verify(searchResultMock, never()).addFacet(any(Facet.class));

  }

  private List<FacetOption> createExpecetedOptions(String nameFirstOption, long countFirstOption, String nameSecondOption, long countSecondOption) {
    List<FacetOption> expectedOptions = Lists.newArrayList(//
        new FacetOption(nameFirstOption, countFirstOption), //
        new FacetOption(nameSecondOption, countSecondOption));
    return expectedOptions;
  }

  private void setupSolrFacetField(String nameFirstOption, long countFirstOption, String nameSecondOption, long countSecondOption, QueryResponse queryResponseMock) {
    FacetField solrFacet = new FacetField(facetName);
    solrFacet.add(nameFirstOption, countFirstOption);
    solrFacet.add(nameSecondOption, countSecondOption);
    when(queryResponseMock.getFacetField(facetName)).thenReturn(solrFacet);
  }

  private void setupSolrFacetFieldWithoutValues() {
    FacetField solrFacet = new FacetField(facetName);
    when(queryResponseMock.getFacetField(facetName)).thenReturn(solrFacet);
  }

  @Test
  public void testGetFields() {
    FacetDefinition facetDefinition = new FacetDefinition()//
        .setName(facetName)//
        .setTitle(facetTitle);

    Collection<String> actualFields = facetDefinition.getFields();

    assertThat(actualFields, containsInAnyOrder(facetName));
  }

  @Test
  public void toFacetFieldConvertsTheFacetDefinitionToFacetField() {
    FacetDefinition facetDefinition = new FacetDefinition()//
        .setName(facetName)//
        .setTitle(facetTitle);

    nl.knaw.huygens.facetedsearch.model.parameters.FacetField field = facetDefinition.toFacetField();

    assertThat(field, facetFieldLike(facetName));
  }

}
