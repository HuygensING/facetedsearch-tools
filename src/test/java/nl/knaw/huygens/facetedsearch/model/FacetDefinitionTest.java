package nl.knaw.huygens.facetedsearch.model;

import static nl.knaw.huygens.facetedsearch.model.DefaultFacetMatcher.defaultFacethasCharacteristics;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    FacetDefinition instance = new DefaultFacetDefinition().setName(facetName).setTitle(facetTitle).setType(FacetType.LIST);

    // mocks
    QueryResponse queryResponseMock = mock(QueryResponse.class);
    FacetedSearchResult searchResultMock = mock(FacetedSearchResult.class);

    // when
    FacetField solrFacet = new FacetField(facetName);
    String nameFirstOption = "option1";
    long countFirstOption = 123L;
    solrFacet.add(nameFirstOption, countFirstOption);
    String nameSecondOption = "option2";
    long countSecondOption = 12L;
    solrFacet.add(nameSecondOption, countSecondOption);
    when(queryResponseMock.getFacetField(facetName)).thenReturn(solrFacet);

    // action
    instance.addFacetToResult(searchResultMock, queryResponseMock);

    // verify
    InOrder inOrder = inOrder(queryResponseMock, searchResultMock);
    inOrder.verify(queryResponseMock).getFacetField(facetName);

    List<FacetOption> expectedOptions = Lists.newArrayList(//
        new FacetOption(nameFirstOption, countFirstOption), //
        new FacetOption(nameSecondOption, countSecondOption));

    inOrder.verify(searchResultMock).addFacet(argThat(defaultFacethasCharacteristics(facetName, facetTitle, expectedOptions)));
  }

}
