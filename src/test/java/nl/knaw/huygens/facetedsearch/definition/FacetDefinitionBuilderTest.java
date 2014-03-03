package nl.knaw.huygens.facetedsearch.definition;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import nl.knaw.huygens.facetedsearch.definition.FacetDefinition;
import nl.knaw.huygens.facetedsearch.definition.FacetDefinition;
import nl.knaw.huygens.facetedsearch.definition.FacetDefinitionBuilder;
import nl.knaw.huygens.facetedsearch.definition.RangeFacetDefinition;
import nl.knaw.huygens.facetedsearch.model.FacetType;

import org.junit.Test;

public class FacetDefinitionBuilderTest {
  private String name = "name";
  private String title = "title";

  @Test
  public void testBuildListFacetDefinition() {
    FacetDefinitionBuilder instance = new FacetDefinitionBuilder(name, title, FacetType.LIST);

    FacetDefinition facetDefinition = instance.build();

    assertDefaultFacetDefinition(facetDefinition, FacetType.LIST);
    assertThat(facetDefinition, is(instanceOf(FacetDefinition.class)));

  }

  protected void assertDefaultFacetDefinition(FacetDefinition facetDefinition, FacetType facetType) {
    assertThat(facetDefinition.getName(), is(name));
    assertThat(facetDefinition.getTitle(), is(title));
    assertThat(facetDefinition.getType(), is(facetType));
  }

  @Test
  public void testBuildRangeFacetDefinition() {
    FacetDefinitionBuilder instance = new FacetDefinitionBuilder(name, title, FacetType.RANGE);
    String lowerLimitField = "lowerField";
    instance.setLowerLimitField(lowerLimitField);
    String upperLimitField = "upperField";
    instance.setUpperLimitField(upperLimitField);

    FacetDefinition facetDefinition = instance.build();

    assertDefaultFacetDefinition(facetDefinition, FacetType.RANGE);
    assertThat(facetDefinition, is(instanceOf(RangeFacetDefinition.class)));

    RangeFacetDefinition rangeDefinition = (RangeFacetDefinition) facetDefinition;

    assertThat(rangeDefinition.getLowerLimitField(), is(lowerLimitField));
    assertThat(rangeDefinition.getUpperLimitField(), is(upperLimitField));
  }

  @Test(expected = NullPointerException.class)
  public void testBuildRangeFacetDefinitionWithoutLowerLimitField() {
    FacetDefinitionBuilder instance = new FacetDefinitionBuilder(name, title, FacetType.RANGE);
    String upperLimitField = "upperField";
    instance.setUpperLimitField(upperLimitField);

    instance.build();
  }

  @Test(expected = NullPointerException.class)
  public void testBuildRangeFacetDefinitionWithoutUpperLimitField() {
    FacetDefinitionBuilder instance = new FacetDefinitionBuilder(name, title, FacetType.RANGE);
    String lowerLimitField = "lowerField";
    instance.setLowerLimitField(lowerLimitField);

    instance.build();
  }
}
