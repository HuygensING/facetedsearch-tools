package nl.knaw.huygens.facetedsearch.model;

import static nl.knaw.huygens.facetedsearch.model.DefaultFacetMatcher.defaultFacethasCharacteristics;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;
import org.mockito.InOrder;

import com.google.common.collect.Lists;

public class FacetDefinitionTest {
  private String facetName = "name";
  private String facetTitle = "title";

  @Test
  // Currently the default implementation.
  public void testAddFacetToResultWithListFacet() {
    // setup instance
    FacetType facetType = FacetType.LIST;
    FacetDefinition instance = new FacetDefinition().setName(facetName).setTitle(facetTitle).setType(facetType);

    String nameFirstOption = "option1";
    long countFirstOption = 123L;
    String nameSecondOption = "option2";
    long countSecondOption = 12L;
    List<FacetOption> expectedOptions = createExpecetedOptions(nameFirstOption, countFirstOption, nameSecondOption, countSecondOption);

    // mocks
    QueryResponse queryResponseMock = mock(QueryResponse.class);
    FacetedSearchResult searchResultMock = mock(FacetedSearchResult.class);

    // when
    setupSolrFacetField(nameFirstOption, countFirstOption, nameSecondOption, countSecondOption, queryResponseMock);

    // action
    instance.addFacetToResult(searchResultMock, queryResponseMock);

    // verify
    InOrder inOrder = inOrder(queryResponseMock, searchResultMock);
    inOrder.verify(queryResponseMock).getFacetField(facetName);
    inOrder.verify(searchResultMock).addFacet(argThat(defaultFacethasCharacteristics(facetName, facetTitle, expectedOptions, facetType)));
  }

  @Test
  public void testAddFacetToResultWithNonListFacet() {
    // setup instance
    FacetType facetType = FacetType.BOOLEAN;
    FacetDefinition instance = new FacetDefinition().setName(facetName).setTitle(facetTitle).setType(facetType);

    String nameFirstOption = "true";
    long countFirstOption = 123L;
    String nameSecondOption = "false";
    long countSecondOption = 12L;
    List<FacetOption> expectedOptions = createExpecetedOptions(nameFirstOption, countFirstOption, nameSecondOption, countSecondOption);

    // mocks
    QueryResponse queryResponseMock = mock(QueryResponse.class);
    FacetedSearchResult searchResultMock = mock(FacetedSearchResult.class);

    setupSolrFacetField(nameFirstOption, countFirstOption, nameSecondOption, countSecondOption, queryResponseMock);

    // action
    instance.addFacetToResult(searchResultMock, queryResponseMock);

    // verify
    verify(queryResponseMock).getFacetField(facetName);
    verify(searchResultMock).addFacet(argThat(defaultFacethasCharacteristics(facetName, facetTitle, expectedOptions, facetType)));
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

  @Test
  public void testGetFields() {
    FacetDefinition facetDefinition = new FacetDefinition().setName(facetName).setTitle(facetTitle);

    Collection<String> actualFields = facetDefinition.getFields();

    assertThat(actualFields, containsInAnyOrder(facetName));
  }

}
