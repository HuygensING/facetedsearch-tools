package nl.knaw.huygens.facetedsearch.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RangeFacetMatcherTest {
  private String name = "name";
  private String title = "title";
  private long lowerLimit = 200l;
  private long upperLimit = 1000l;

  @Test
  public void testMatchTypeSafelyMatches() {
    RangeFacetMatcher instance = new RangeFacetMatcher(name, title, lowerLimit, upperLimit);

    assertTrue(instance.matchesSafely(new RangeFacet(name, title, lowerLimit, upperLimit)));

  }

  @Test
  public void testMatchTypeSafelyDifferentName() {
    RangeFacetMatcher instance = new RangeFacetMatcher(name, title, lowerLimit, upperLimit);

    assertFalse(instance.matchesSafely(new RangeFacet("differentName", title, lowerLimit, upperLimit)));
  }

  @Test
  public void testMatchTypeSafelyDifferentTitle() {
    RangeFacetMatcher instance = new RangeFacetMatcher(name, title, lowerLimit, upperLimit);

    assertFalse(instance.matchesSafely(new RangeFacet(name, "differentTitle", lowerLimit, upperLimit)));
  }

  @Test
  public void testMatchTypeSafelyDifferentLowerLimit() {
    RangeFacetMatcher instance = new RangeFacetMatcher(name, title, lowerLimit, upperLimit);

    long differentLowerLimit = 0L;
    assertFalse(instance.matchesSafely(new RangeFacet(name, title, differentLowerLimit, upperLimit)));
  }

  @Test
  public void testMatchTypeSafelyDifferentUpperLimit() {
    RangeFacetMatcher instance = new RangeFacetMatcher(name, title, lowerLimit, upperLimit);

    long differentUpperLimit = 90000l;
    assertFalse(instance.matchesSafely(new RangeFacet(name, title, lowerLimit, differentUpperLimit)));
  }

}
