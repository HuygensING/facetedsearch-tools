package nl.knaw.huygens.facetedsearch.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import org.junit.Before;
import org.junit.Test;

public class RangeFacetFieldTest {

  private static final String FACET_NAME = "facetField";
  private static final String LOWER_FIELD = "lowerrField";
  private static final String UPPER_FIELD = "upperField";

  private FacetField instance;

  @Before
  public void setUp() {

  }

  @Test
  public void testGetFieldsTwoFieldsDefined() {
    instance = new RangeFacetField(FACET_NAME, LOWER_FIELD, UPPER_FIELD);
    assertThat(instance.getFields(), contains(LOWER_FIELD, UPPER_FIELD));
  }

  @Test
  public void testGetFieldsOneFieldDefined() {
    instance = new RangeFacetField(FACET_NAME, UPPER_FIELD);
    assertThat(instance.getFields(), contains(UPPER_FIELD, UPPER_FIELD));
  }

}
