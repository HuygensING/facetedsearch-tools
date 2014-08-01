package nl.knaw.huygens.facetedsearch.serialization;


import java.util.List;

import nl.knaw.huygens.facetedsearch.model.parameters.DefaultFacetParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.RangeParameter;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.base.Objects;

public abstract class FacetParameterMatcher extends TypeSafeMatcher<FacetParameter> {

  private final String name;

  private FacetParameterMatcher(String name) {
    this.name = name;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("FacetParameter with name ") //
        .appendValue(name);
  }

  @Override
  protected void describeMismatchSafely(FacetParameter item, Description mismatchDescription) {
    mismatchDescription.appendText("FacetParameter with name ") //
        .appendValue(item.getName());
  }

  @Override
  protected boolean matchesSafely(FacetParameter facetParameter) {
    return Objects.equal(name, facetParameter.getName());
  }

  public static FacetParameterMatcher isDefaultFacetParameter(String name, List<String> values) {
    return new DefaultFacetParameterMatcher(name, values);
  }

  public static FacetParameterMatcher isRangeParameter(String name, long lowerLimit, long upperLimit) {
    return new RangeParameterMatcher(name, lowerLimit, upperLimit);
  }

  private static class DefaultFacetParameterMatcher extends FacetParameterMatcher {

    private final List<String> values;

    private DefaultFacetParameterMatcher(String name, List<String> values) {
      super(name);
      this.values = values;
    }

    @Override
    public void describeTo(Description description) {
      super.describeTo(description);
      description.appendText(" values ") //
          .appendValue(values);

    }

    @Override
    protected void describeMismatchSafely(FacetParameter item, Description mismatchDescription) {
      super.describeMismatchSafely(item, mismatchDescription);

      if (item instanceof DefaultFacetParameter) {
        DefaultFacetParameter defaultFacetParameter = (DefaultFacetParameter) item;

        mismatchDescription.appendText(" values ") //
            .appendValue(defaultFacetParameter.getValues());
      }

    }

    @Override
    protected boolean matchesSafely(FacetParameter facetParameter) {
      if (!(facetParameter instanceof DefaultFacetParameter)) {
        return false;
      }

      DefaultFacetParameter defaultParameter = (DefaultFacetParameter) facetParameter;

      boolean isEqual = super.matchesSafely(defaultParameter);
      isEqual &= Objects.equal(values, defaultParameter.getValues());

      return isEqual;
    }
  }

  private static class RangeParameterMatcher extends FacetParameterMatcher {
    private long lowerLimit;
    private long upperLimit;

    private RangeParameterMatcher(String name, long lowerLimit, long upperLimit) {
      super(name);
      this.lowerLimit = lowerLimit;
      this.upperLimit = upperLimit;
    }

    @Override
    public void describeTo(Description description) {
      super.describeTo(description);
      description.appendText(" lowerLimit ") //
          .appendValue(lowerLimit) //
          .appendText(" upperLimit ") //
          .appendValue(upperLimit);
    }

    @Override
    protected void describeMismatchSafely(FacetParameter item, Description mismatchDescription) {
      super.describeMismatchSafely(item, mismatchDescription);

      if (item instanceof RangeParameter) {
        RangeParameter rangeParameter = (RangeParameter) item;
        mismatchDescription.appendText(" lowerLimit ") //
            .appendValue(rangeParameter.getLowerLimit()) //
            .appendText(" upperLimit ") //
            .appendValue(rangeParameter.getUpperLimit());
      }

    }

    @Override
    protected boolean matchesSafely(FacetParameter facetParameter) {
      if (!(facetParameter instanceof RangeParameter)) {
        return false;
      }

      RangeParameter rangeParameter = (RangeParameter) facetParameter;

      boolean isEqual = super.matchesSafely(rangeParameter);
      isEqual &= Objects.equal(rangeParameter.getLowerLimit(), lowerLimit);
      isEqual &= Objects.equal(rangeParameter.getUpperLimit(), upperLimit);

      return isEqual;
    }
  }

}
