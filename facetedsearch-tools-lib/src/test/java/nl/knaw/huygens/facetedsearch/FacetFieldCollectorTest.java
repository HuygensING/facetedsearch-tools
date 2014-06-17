package nl.knaw.huygens.facetedsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.FacetDefinition;

import org.junit.Test;

import com.google.common.collect.Lists;

public class FacetFieldCollectorTest {
  @Test
  public void testFindInteractions() {
    // setup    
    @SuppressWarnings("unchecked")
    final List<String> list = mock(List.class);

    FacetDefinition facetDefinition1 = new FacetDefinition().setName("test").setTitle("title");

    // action
    FacetFieldCollector instance = new FacetFieldCollector() {
      @Override
      protected List<String> createFacetFieldCollection() {
        return list;
      }
    };
    instance.find(Lists.newArrayList(facetDefinition1));

    // verify
    verify(list).addAll(facetDefinition1.getFields());
    verify(list).toArray(new String[0]);
  }

  @Test
  public void testFindReturnValue() {
    // setup    
    @SuppressWarnings("unchecked")
    String name1 = "test";
    FacetDefinition facetDefinition1 = new FacetDefinition().setName(name1).setTitle("title");
    String name2 = "test1";
    FacetDefinition facetDefinition2 = new FacetDefinition().setName(name2).setTitle("title1");

    // action
    FacetFieldCollector instance = new FacetFieldCollector();
    String[] facetFields = instance.find(Lists.newArrayList(facetDefinition1, facetDefinition2));

    assertThat(Lists.newArrayList(facetFields), containsInAnyOrder(name1, name2));
  }
}
