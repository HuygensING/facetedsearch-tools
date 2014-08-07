package nl.knaw.huygens.facetedsearch.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FacetOptionTest {
  @Test
  public void testIsCombinable() {
    FacetOption option1 = new FacetOption("test", 1);
    FacetOption option2 = new FacetOption("test", 1);

    assertTrue(option1.isCombinable(option2));
  }

  @Test
  public void testIsCombinableWithDifferentNames() {
    FacetOption option1 = new FacetOption("name", 1);
    FacetOption option2 = new FacetOption("test", 1);

    assertFalse(option1.isCombinable(option2));
  }

  @Test
  public void testIsCombinableWithDifferentCounts() {
    FacetOption option1 = new FacetOption("test", 23);
    FacetOption option2 = new FacetOption("test", 1);

    assertTrue(option1.isCombinable(option2));
  }

  @Test
  public void testCombine() {
    FacetOption option1 = new FacetOption("test", 23);
    FacetOption option2 = new FacetOption("test", 1);

    assertEquals(24, option1.combineWith(option2).getCount());
  }
}
