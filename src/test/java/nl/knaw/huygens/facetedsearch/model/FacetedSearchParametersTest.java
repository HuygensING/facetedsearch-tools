package nl.knaw.huygens.facetedsearch.model;

import static org.assertj.core.api.Assertions.assertThat;
import nl.knaw.huygens.LoggableObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class FacetedSearchParametersTest<T> extends LoggableObject {

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testSetTerm() throws Exception {
    FacetedSearchParameters<?> facetedSearchParameters = new DefaultFacetedSearchParameters()//
        .setCaseSensitive(true)//
        .setFacetFields(Lists.<String> newArrayList());

    assertThat(facetedSearchParameters.isCaseSensitive()).isTrue();
  }
}
