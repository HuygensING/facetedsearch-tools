package nl.knaw.huygens.facetedsearch.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetField;

import org.junit.Before;
import org.junit.Test;

public class FaceFieldTest {
  private static final String FACET_NAME = "facetField";
  private FacetField instance;

  @Before
  public void setUp() {
    instance = new FacetField(FACET_NAME);
  }

  @Test
  public void testGetFields() {
    assertThat(instance.getFields(), contains(FACET_NAME));
  }
}
