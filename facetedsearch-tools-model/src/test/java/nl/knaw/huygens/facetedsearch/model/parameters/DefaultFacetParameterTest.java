package nl.knaw.huygens.facetedsearch.model.parameters;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

public class DefaultFacetParameterTest  {


  private static final String FIRST_VALUE = "firstValue";
  private static final String NAME = "Name";
  public static final String SECOND_VALUE = "secondValue";

  @Test
  public void getQueryValueReturnsAnEmptyStringWhenItHasNoValues() {
    // setup
    DefaultFacetParameter instance = new DefaultFacetParameter("Test", Lists.<String>newArrayList());

    // action
    String queryValue = instance.getQueryValue();

    // verify
    assertThat(queryValue, isEmptyString());
  }

  @Test
  public void getQueryValueReturnsAnEmptyStringWhenValuesIsNull() {
    // setup
    DefaultFacetParameter instance = new DefaultFacetParameter("Test", null);

    // action
    String queryValue = instance.getQueryValue();

    // verify
    assertThat(queryValue, isEmptyString());
  }

  @Test
  public void getQueryValueReturnsTheFirstValueIfOneValueIsPresent(){
    // setup
    DefaultFacetParameter instance = new DefaultFacetParameter(NAME, Lists.newArrayList(FIRST_VALUE));

    // action
    String queryValue = instance.getQueryValue();

    // verify
    assertThat(queryValue, is(FIRST_VALUE));
  }

  @Test
  public void getQueryValueConcatenatesTheValuesAndSurroundsThemWithParenthesesWhenMultipleValuesArePresent(){
    // setup
    DefaultFacetParameter instance = new DefaultFacetParameter(NAME, Lists.newArrayList(FIRST_VALUE, SECOND_VALUE));

    // action
    String queryValue = instance.getQueryValue();

    // verify
    assertThat(queryValue, is(String.format("(%s %s)",FIRST_VALUE, SECOND_VALUE)));
  }
}
