package nl.knaw.huygens.facetedsearch.model.parameters;

import static org.mockito.Matchers.argThat;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.base.Objects;

public class FacetParameterNameMatcher extends TypeSafeMatcher<FacetParameter> {

  private String name;

  public FacetParameterNameMatcher(String name) {
    this.name = name;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("FacetParameter with name").appendValue(name);
  }

  @Override
  protected boolean matchesSafely(FacetParameter item) {
    return Objects.equal(name, item.getName());
  }

  public static FacetParameter facetParameterWithName(String name) {
    return argThat(new FacetParameterNameMatcher(name));
  }

}
