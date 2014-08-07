package nl.knaw.huygens.facetedsearch.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class DefaultFacetMatcherTest {
  @Test
  public void testMatchesSafely() {
    FacetOption option1 = new FacetOption("option1", 100L);
    FacetOption option2 = new FacetOption("option2", 20L);

    List<FacetOption> options = Lists.newArrayList(option1, option2);
    String facetTitle = "title";
    String facetName = "name";
    DefaultFacetMatcher matcher = new DefaultFacetMatcher(facetName, facetTitle, options, FacetType.LIST);

    DefaultFacet facet = new DefaultFacet(facetName, facetTitle);
    facet.setOptions(options);

    assertTrue(matcher.matchesSafely(facet));
  }

  @Test
  public void testMatchesSafelyDifferentName() {
    FacetOption option1 = new FacetOption("option1", 100L);
    FacetOption option2 = new FacetOption("option2", 20L);

    List<FacetOption> options = Lists.newArrayList(option1, option2);
    String facetTitle = "title";
    DefaultFacetMatcher matcher = new DefaultFacetMatcher("expectedName", facetTitle, options, FacetType.LIST);

    DefaultFacet facet = new DefaultFacet("actualName", facetTitle);
    facet.setOptions(options);

    assertFalse(matcher.matchesSafely(facet));
  }

  @Test
  public void testMatchesSafelyDifferentTitle() {
    FacetOption option1 = new FacetOption("option1", 100L);
    FacetOption option2 = new FacetOption("option2", 20L);

    List<FacetOption> options = Lists.newArrayList(option1, option2);
    String facetName = "name";
    DefaultFacetMatcher matcher = new DefaultFacetMatcher(facetName, "exepectedTitle", options, FacetType.LIST);

    DefaultFacet facet = new DefaultFacet(facetName, "actualTitle");
    facet.setOptions(options);

    assertFalse(matcher.matchesSafely(facet));
  }

  @Test
  public void testMatchesSafelyOptionsInADifferentOrder() {
    FacetOption option1 = new FacetOption("option1", 100L);
    FacetOption option2 = new FacetOption("option2", 20L);

    List<FacetOption> options = Lists.newArrayList(option1, option2);
    String facetTitle = "title";
    String facetName = "name";
    DefaultFacetMatcher matcher = new DefaultFacetMatcher(facetName, facetTitle, options, FacetType.LIST);

    DefaultFacet facet = new DefaultFacet(facetName, facetTitle);
    facet.setOptions(Lists.newArrayList(option2, option1));

    assertTrue(matcher.matchesSafely(facet));
  }

  @Test
  public void testMatchesSafelyDifferentOptions() {
    FacetOption option1 = new FacetOption("option1", 100L);
    FacetOption option2 = new FacetOption("option2", 20L);

    List<FacetOption> expectedOptions = Lists.newArrayList(option1, option2);
    String facetTitle = "title";
    String facetName = "name";
    DefaultFacetMatcher matcher = new DefaultFacetMatcher(facetName, facetTitle, expectedOptions, FacetType.LIST);

    DefaultFacet facet = new DefaultFacet(facetName, facetTitle);
    FacetOption option3 = new FacetOption("option3", 600L);
    facet.setOptions(Lists.newArrayList(option1, option3));

    assertFalse(matcher.matchesSafely(facet));
  }

  @Test
  public void testMatchesSafelyMoreOptions() {
    FacetOption option1 = new FacetOption("option1", 100L);
    FacetOption option2 = new FacetOption("option2", 20L);

    List<FacetOption> expectedOptions = Lists.newArrayList(option1, option2);
    String facetTitle = "title";
    String facetName = "name";
    DefaultFacetMatcher matcher = new DefaultFacetMatcher(facetName, facetTitle, expectedOptions, FacetType.LIST);

    DefaultFacet facet = new DefaultFacet(facetName, facetTitle);
    FacetOption option3 = new FacetOption("option3", 600L);
    facet.setOptions(Lists.newArrayList(option1, option2, option3));

    assertFalse(matcher.matchesSafely(facet));
  }

  @Test
  public void testMatchesSafelyLessOptions() {
    FacetOption option1 = new FacetOption("option1", 100L);
    FacetOption option2 = new FacetOption("option2", 20L);

    List<FacetOption> expectedOptions = Lists.newArrayList(option1, option2);
    String facetTitle = "title";
    String facetName = "name";
    DefaultFacetMatcher matcher = new DefaultFacetMatcher(facetName, facetTitle, expectedOptions, FacetType.LIST);

    DefaultFacet facet = new DefaultFacet(facetName, facetTitle);
    facet.setOptions(Lists.newArrayList(option1));

    assertFalse(matcher.matchesSafely(facet));
  }

  @Test
  public void testMatchesSafelyDifferentType() {
    FacetOption option1 = new FacetOption("option1", 100L);
    FacetOption option2 = new FacetOption("option2", 20L);

    List<FacetOption> options = Lists.newArrayList(option1, option2);
    String facetTitle = "title";
    String facetName = "name";
    DefaultFacetMatcher matcher = new DefaultFacetMatcher(facetName, facetTitle, options, FacetType.BOOLEAN);

    DefaultFacet facet = new DefaultFacet(facetName, facetTitle);
    facet.setOptions(options);

    assertFalse(matcher.matchesSafely(facet));
  }
}
