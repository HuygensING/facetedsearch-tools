package nl.knaw.huygens.facetedsearch.model;

import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class DefaultFacetMatcher extends TypeSafeMatcher<DefaultFacet> {
  private final String name;
  private final String title;
  private final List<FacetOption> options;

  public DefaultFacetMatcher(String name, String title, List<FacetOption> options) {
    this.name = name;
    this.title = title;
    this.options = options;
  }

  @Override
  public void describeTo(Description description) {
    DefaultFacet facet = new DefaultFacet(name, title);
    facet.setOptions(options);
    description.appendValue(facet);
  }

  @Override
  protected boolean matchesSafely(DefaultFacet item) {

    return name.equals(item.getName()) && title.equals(item.getTitle()) //
        && containsInAnyOrder(options.toArray(new FacetOption[0])).matches(item.getOptions());
  }

  @Factory
  public static Matcher<DefaultFacet> defaultFacethasCharacteristics(String name, String title, List<FacetOption> options) {
    return new DefaultFacetMatcher(name, title, options);
  }
}
