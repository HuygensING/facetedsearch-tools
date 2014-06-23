package nl.knaw.huygens.facetedsearch.model.parameters;

import static org.mockito.Matchers.argThat;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.base.Objects;

public class FacetFieldNameMatcher extends TypeSafeMatcher<FacetField> {

  private String name;

  public FacetFieldNameMatcher(String name) {
    this.name = name;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("FacetField with name").appendValue(name);
  }

  @Override
  protected boolean matchesSafely(FacetField item) {
    return Objects.equal(name, item.getName());
  }

  public static FacetField facetFieldWithName(String name) {
    return argThat(new FacetFieldNameMatcher(name));
  }

}
