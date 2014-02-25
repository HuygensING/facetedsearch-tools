package nl.knaw.huygens.facetedsearch.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DefautlOptionTest {
  @Test
  public void testIsCombinable() {
    DefaultOption option1 = new DefaultOption("test", 1);
    DefaultOption option2 = new DefaultOption("test", 1);

    assertTrue(option1.isCombinable(option2));
  }

  @Test
  public void testIsCombinableWithDifferentNames() {
    DefaultOption option1 = new DefaultOption("name", 1);
    DefaultOption option2 = new DefaultOption("test", 1);

    assertFalse(option1.isCombinable(option2));
  }

  @Test
  public void testIsCombinableWithDifferentCounts() {
    DefaultOption option1 = new DefaultOption("test", 23);
    DefaultOption option2 = new DefaultOption("test", 1);

    assertTrue(option1.isCombinable(option2));
  }

  @Test
  public void testCombine() {
    DefaultOption option1 = new DefaultOption("test", 23);
    DefaultOption option2 = new DefaultOption("test", 1);

    assertEquals(24, option1.combineWith(option2).getCount());
  }
}
