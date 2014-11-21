package nl.knaw.huygens.facetedsearch.model;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.RangeFacet.RangeFacetOption;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.base.Objects;

public class RangeFacetMatcher extends TypeSafeMatcher<RangeFacet> {

  private String name;
  private String title;
  private long lowerLimit;
  private long upperLimit;

  public RangeFacetMatcher(String name, String title, long lowerLimit, long upperLimit) {
    this.name = name;
    this.title = title;
    this.lowerLimit = lowerLimit;
    this.upperLimit = upperLimit;
  }

  @Override
  public void describeTo(Description description) {
    description.appendValue(new RangeFacet(name, title, lowerLimit, upperLimit));
  }

  @Override
  protected boolean matchesSafely(RangeFacet item) {
    boolean matches = Objects.equal(name, item.getName());
    matches &= Objects.equal(title, item.getTitle());
    matches = matchesOptions(item, matches);

    return matches;
  }

  private boolean matchesOptions(RangeFacet item, boolean matches) {
    List<RangeFacetOption> options = item.getOptions();
    if (options != null && !options.isEmpty()) {
      RangeFacetOption option = options.get(0);
      matches &= Objects.equal(lowerLimit, option.getLowerLimit());
      matches &= Objects.equal(upperLimit, option.getUpperLimit());
    }

    return matches;
  }

  @Factory
  public static Matcher<RangeFacet> rangeFacetHasCharacteristics(String name, String title, long lowerLimit, long upperLimit) {
    return new RangeFacetMatcher(name, title, lowerLimit, upperLimit);
  }

}
