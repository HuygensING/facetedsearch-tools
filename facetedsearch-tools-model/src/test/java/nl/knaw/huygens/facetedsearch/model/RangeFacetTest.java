package nl.knaw.huygens.facetedsearch.model;

import static nl.knaw.huygens.facetedsearch.model.RangeFacetMatcher.rangeFacetHasCharacteristics;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RangeFacetTest {
  private static final String FACET_TITLE = "title";
  private static final String FACET_NAME = "name";

  @Test
  public void testIsCombinable() {
    RangeFacet facet1 = new RangeFacet(FACET_NAME, FACET_TITLE, 20l, 100l);
    RangeFacet facet2 = new RangeFacet(FACET_NAME, FACET_TITLE, 25l, 102l);

    assertTrue(facet1.isCombinable(facet2));
  }

  @Test
  public void testIsCombinableWithDifferentNames() {
    RangeFacet facet1 = new RangeFacet(FACET_NAME, FACET_TITLE, 20l, 100l);
    RangeFacet facet2 = new RangeFacet("otherName", FACET_TITLE, 25l, 102l);

    assertFalse(facet1.isCombinable(facet2));

  }

  @Test
  public void testIsCombinableWithDifferentTitles() {
    RangeFacet facet1 = new RangeFacet(FACET_NAME, FACET_TITLE, 20l, 100l);
    RangeFacet facet2 = new RangeFacet(FACET_NAME, "otherTitle", 25l, 102l);

    assertFalse(facet1.isCombinable(facet2));
  }

  @Test
  public void testCombineWithSameLimits() {
    RangeFacet facet1 = new RangeFacet(FACET_NAME, FACET_TITLE, 20l, 100l);
    RangeFacet facet2 = new RangeFacet(FACET_NAME, FACET_TITLE, 20l, 100l);

    RangeFacet resultFacet = facet1.combineWith(facet2);

    assertThat(resultFacet, rangeFacetHasCharacteristics(FACET_NAME, FACET_TITLE, 20l, 100l));
  }

  @Test
  public void testCombineWithOtherHigherLowerLimit() {
    RangeFacet facet1 = new RangeFacet(FACET_NAME, FACET_TITLE, 20l, 100l);
    RangeFacet facet2 = new RangeFacet(FACET_NAME, FACET_TITLE, 25l, 100l);

    RangeFacet resultFacet = facet1.combineWith(facet2);

    assertThat(resultFacet, rangeFacetHasCharacteristics(FACET_NAME, FACET_TITLE, 20l, 100l));
  }

  @Test
  public void testCombineWithOtherLowerLowerLimit() {
    RangeFacet facet1 = new RangeFacet(FACET_NAME, FACET_TITLE, 20l, 100l);
    RangeFacet facet2 = new RangeFacet(FACET_NAME, FACET_TITLE, 10l, 100l);

    RangeFacet resultFacet = facet1.combineWith(facet2);

    assertThat(resultFacet, rangeFacetHasCharacteristics(FACET_NAME, FACET_TITLE, 10l, 100l));

  }

  @Test
  public void testCombineWithOtherHigherUpperLimit() {
    RangeFacet facet1 = new RangeFacet(FACET_NAME, FACET_TITLE, 20l, 100l);
    RangeFacet facet2 = new RangeFacet(FACET_NAME, FACET_TITLE, 20l, 120l);

    RangeFacet resultFacet = facet1.combineWith(facet2);

    assertThat(resultFacet, rangeFacetHasCharacteristics(FACET_NAME, FACET_TITLE, 20l, 120l));
  }

  @Test
  public void testCombineWithOtherLowerUpperLimit() {
    RangeFacet facet1 = new RangeFacet(FACET_NAME, FACET_TITLE, 20l, 100l);
    RangeFacet facet2 = new RangeFacet(FACET_NAME, FACET_TITLE, 20l, 80l);

    RangeFacet resultFacet = facet1.combineWith(facet2);

    assertThat(resultFacet, rangeFacetHasCharacteristics(FACET_NAME, FACET_TITLE, 20l, 100l));
  }

  @Test(expected = RuntimeException.class)
  public void testCombineNotCombinable() {
    RangeFacet facet1 = new RangeFacet(FACET_NAME, FACET_TITLE, 20l, 100l);
    RangeFacet facet2 = new RangeFacet("otherFacetName", FACET_TITLE, 20l, 80l);

    facet1.combineWith(facet2);
  }
}
