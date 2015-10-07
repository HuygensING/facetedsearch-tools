package nl.knaw.huygens.facetedsearch.model.parameters;

import static org.mockito.Matchers.argThat;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.base.Objects;

public class SortParameterNameMatcher extends TypeSafeMatcher<SortParameter> {

  private String name;

  public SortParameterNameMatcher(String name) {
    this.name = name;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("FacetParameter with name").appendValue(name);
  }

  @Override
  protected boolean matchesSafely(SortParameter item) {
    return Objects.equal(name, item.getFieldname());
  }

  public static SortParameter sortParameterWithName(String name) {
    return argThat(new SortParameterNameMatcher(name));
  }

}
