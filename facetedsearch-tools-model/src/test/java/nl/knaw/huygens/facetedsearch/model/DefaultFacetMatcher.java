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
  private final FacetType facetType;

  public DefaultFacetMatcher(String name, String title, List<FacetOption> options, FacetType facetType) {
    this.name = name;
    this.title = title;
    this.options = options;
    this.facetType = facetType;
  }

  @Override
  public void describeTo(Description description) {
    DefaultFacet facet = new DefaultFacet(name, title);
    facet.setOptions(options);
    description.appendValue(facet);
  }

  @Override
  protected boolean matchesSafely(DefaultFacet item) {

    boolean isEqual = name.equals(item.getName());
    isEqual &= title.equals(item.getTitle());
    isEqual &= containsInAnyOrder(options.toArray(new FacetOption[0])).matches(item.getOptions());
    isEqual &= (facetType == item.getType());

    return isEqual;
  }

  @Factory
  public static Matcher<DefaultFacet> defaultFacethasCharacteristics(String name, String title, List<FacetOption> options, FacetType facetType) {
    return new DefaultFacetMatcher(name, title, options, facetType);
  }
}
