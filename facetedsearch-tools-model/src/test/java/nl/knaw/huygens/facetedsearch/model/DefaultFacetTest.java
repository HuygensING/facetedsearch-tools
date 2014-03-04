package nl.knaw.huygens.facetedsearch.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class DefaultFacetTest {
  @Test
  public void testIsCombinable() {
    DefaultFacet facet1 = new DefaultFacet("name", "title");
    DefaultFacet facet2 = new DefaultFacet("name", "title");

    assertTrue(facet1.isCombinable(facet2));
  }

  @Test
  public void testIsCombinableWithDifferentNames() {
    DefaultFacet facet1 = new DefaultFacet("name1", "title");
    DefaultFacet facet2 = new DefaultFacet("name2", "title");

    assertFalse(facet1.isCombinable(facet2));
  }

  @Test
  public void testIsCombinableWithDifferentTitles() {
    DefaultFacet facet1 = new DefaultFacet("name", "title1");
    DefaultFacet facet2 = new DefaultFacet("name", "title2");

    assertFalse(facet1.isCombinable(facet2));
  }

  @Test
  public void testCombineWithSameSingleOption() {
    // setup
    final DefaultFacet combinedFacetMock = mock(DefaultFacet.class);
    FacetOption optionFacet1Mock = mock(FacetOption.class);
    DefaultFacet facet1 = new DefaultFacet("name", "title") {
      @Override
      protected DefaultFacet createCombinedListFacet() {
        return combinedFacetMock;
      }
    };
    facet1.addOption(optionFacet1Mock);

    FacetOption optionFacet2Mock = mock(FacetOption.class);
    DefaultFacet facet2 = createFacet(optionFacet2Mock);

    FacetOption combinedOptionMock = mock(FacetOption.class);

    // when
    when(optionFacet1Mock.isCombinable(any(FacetOption.class))).thenReturn(false);
    when(optionFacet1Mock.isCombinable(optionFacet2Mock)).thenReturn(true);
    when(optionFacet1Mock.combineWith(optionFacet2Mock)).thenReturn(combinedOptionMock);

    // action
    DefaultFacet resultFacet = facet1.combineWith(facet2);

    // verify
    verify(optionFacet1Mock).isCombinable(optionFacet2Mock);
    verify(optionFacet1Mock).combineWith(optionFacet2Mock);

    verify(combinedFacetMock).addOption(combinedOptionMock);

    assertThat(resultFacet, is(combinedFacetMock));
  }

  @Test
  public void testCombineWithMultipleOptions() {
    // setup
    final DefaultFacet combinedFacetMock = mock(DefaultFacet.class);
    FacetOption option1Facet1Mock = mock(FacetOption.class);
    FacetOption option2Facet1Mock = mock(FacetOption.class);
    DefaultFacet facet1 = new DefaultFacet("name", "title") {
      @Override
      protected DefaultFacet createCombinedListFacet() {
        return combinedFacetMock;
      }
    };
    facet1.addOption(option1Facet1Mock);
    facet1.addOption(option2Facet1Mock);

    FacetOption option1Facet2Mock = mock(FacetOption.class);
    FacetOption option2Facet2Mock = mock(FacetOption.class);
    DefaultFacet facet2 = createFacet(option1Facet2Mock, option2Facet2Mock);

    FacetOption combinedOption1 = mock(FacetOption.class);
    FacetOption combinedOption2 = mock(FacetOption.class);

    // when
    when(option1Facet1Mock.isCombinable(any(FacetOption.class))).thenReturn(false);
    when(option1Facet1Mock.isCombinable(option1Facet2Mock)).thenReturn(true);
    when(option1Facet1Mock.combineWith(option1Facet2Mock)).thenReturn(combinedOption1);

    when(option2Facet1Mock.isCombinable(any(FacetOption.class))).thenReturn(false);
    when(option2Facet1Mock.isCombinable(option2Facet2Mock)).thenReturn(true);
    when(option2Facet1Mock.combineWith(option2Facet2Mock)).thenReturn(combinedOption2);

    // action
    DefaultFacet resultFacet = facet1.combineWith(facet2);

    // verify
    verify(option1Facet1Mock).isCombinable(option1Facet2Mock);
    verify(option1Facet1Mock).isCombinable(option2Facet2Mock);
    verify(option1Facet1Mock).combineWith(option1Facet2Mock);

    verify(combinedFacetMock).addOption(combinedOption1);
    verify(combinedFacetMock).addOption(combinedOption2);

    verify(option2Facet1Mock).isCombinable(option1Facet2Mock);
    verify(option2Facet1Mock).isCombinable(option2Facet2Mock);
    verify(option2Facet1Mock).combineWith(option2Facet2Mock);

    assertThat(resultFacet, is(combinedFacetMock));
  }

  private DefaultFacet createFacet(FacetOption... options) {
    DefaultFacet facet = new DefaultFacet("name", "title");
    for (FacetOption option : options) {
      facet.addOption(option);
    }
    return facet;
  }

  @Test
  public void testCombineWithFacetHasOtherOptions() {
    // setup
    final DefaultFacet combinedFacetMock = mock(DefaultFacet.class);
    FacetOption option1Facet1Mock = mock(FacetOption.class);
    FacetOption option2Facet1Mock = mock(FacetOption.class);
    DefaultFacet facet1 = new DefaultFacet("name", "title") {
      @Override
      protected DefaultFacet createCombinedListFacet() {
        return combinedFacetMock;
      }
    };
    facet1.addOption(option1Facet1Mock);
    facet1.addOption(option2Facet1Mock);

    FacetOption optionFacet2Mock = mock(FacetOption.class);
    DefaultFacet facet2 = createFacet(optionFacet2Mock);

    FacetOption combinedOptionMock = mock(FacetOption.class);

    // when
    when(option1Facet1Mock.isCombinable(any(FacetOption.class))).thenReturn(false);
    when(option1Facet1Mock.isCombinable(optionFacet2Mock)).thenReturn(true);
    when(option1Facet1Mock.combineWith(optionFacet2Mock)).thenReturn(combinedOptionMock);

    when(option2Facet1Mock.isCombinable(any(FacetOption.class))).thenReturn(false);

    when(combinedFacetMock.containsOption(any(FacetOption.class))).thenReturn(true);
    when(combinedFacetMock.containsOption(option2Facet1Mock)).thenReturn(false);

    // action
    DefaultFacet resultFacet = facet1.combineWith(facet2);

    // verify
    verify(option1Facet1Mock).isCombinable(optionFacet2Mock);
    verify(option1Facet1Mock).combineWith(optionFacet2Mock);

    verify(combinedFacetMock).addOption(combinedOptionMock);
    verify(combinedFacetMock).addOption(option2Facet1Mock);

    assertThat(resultFacet, is(combinedFacetMock));
  }

  @Test
  public void testCombineWithOtherFacetHasOtherOptions() {
    // setup
    final DefaultFacet combinedFacetMock = mock(DefaultFacet.class);
    FacetOption optionFacet1Mock = mock(FacetOption.class);
    DefaultFacet facet1 = new DefaultFacet("name", "title") {
      @Override
      protected DefaultFacet createCombinedListFacet() {
        return combinedFacetMock;
      }
    };
    facet1.addOption(optionFacet1Mock);

    FacetOption option1Facet2Mock = mock(FacetOption.class);
    FacetOption option2Facet2Mock = mock(FacetOption.class);
    DefaultFacet facet2 = createFacet(option1Facet2Mock, option2Facet2Mock);

    FacetOption combinedOptionMock = mock(FacetOption.class);

    // when
    when(optionFacet1Mock.isCombinable(any(FacetOption.class))).thenReturn(false);
    when(optionFacet1Mock.isCombinable(option1Facet2Mock)).thenReturn(true);
    when(optionFacet1Mock.combineWith(option1Facet2Mock)).thenReturn(combinedOptionMock);

    when(option2Facet2Mock.isCombinable(any(FacetOption.class))).thenReturn(false);

    // action
    DefaultFacet resultFacet = facet1.combineWith(facet2);

    // verify
    verify(optionFacet1Mock).isCombinable(option1Facet2Mock);
    verify(optionFacet1Mock).combineWith(option1Facet2Mock);

    verify(combinedFacetMock).addOption(combinedOptionMock);
    verify(combinedFacetMock).addOption(option2Facet2Mock);

    assertThat(resultFacet, is(combinedFacetMock));
  }

  @Test(expected = RuntimeException.class)
  public void testCombineCouldNotBeCombined() {
    DefaultFacet facet1 = new DefaultFacet("name", "title");
    DefaultFacet facet2 = new DefaultFacet("name1", "title");

    facet1.combineWith(facet2);
  }
}
