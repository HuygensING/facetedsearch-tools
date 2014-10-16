package nl.knaw.huygens.facetedsearch.model.parameters;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.collect.Lists;

public abstract class FacetFieldMatcher extends TypeSafeMatcher<FacetField> {

  public static FacetFieldMatcher facetFieldLike(String name) {
    return new NormalFacetFieldMatcher(name);
  }

  public static FacetFieldMatcher rangeFacetFieldLike(String name, String lowerLimitField, String upperLimitField) {
    return new RangeFacetFieldMatcher(name, lowerLimitField, upperLimitField);
  }

  private static class RangeFacetFieldMatcher extends FacetFieldMatcher {

    private final String lowerLimitField;
    private final String upperLimitField;
    private final String name;

    public RangeFacetFieldMatcher(String name, String lowerLimitField, String upperLimitField) {
      this.name = name;
      this.lowerLimitField = lowerLimitField;
      this.upperLimitField = upperLimitField;
    }

    @Override
    protected boolean matchesSafely(FacetField item) {
      if (!(item instanceof RangeFacetField)) {
        return false;
      }

      RangeFacetField rangeFacetField = (RangeFacetField) item;
      boolean matches = rangeFacetField.getFields().containsAll(Lists.newArrayList(lowerLimitField, upperLimitField));
      matches &= name.equals(rangeFacetField.getName());

      return matches;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("RangeFacetField with name ").appendValue(name) //
          .appendText(" and fields ").appendValue(Lists.newArrayList(lowerLimitField, upperLimitField));
    }

    @Override
    protected void describeMismatchSafely(FacetField item, Description mismatchDescription) {
      mismatchDescription.appendText("RangeFacetField with name ").appendValue(item.getName()) //
          .appendText(" and fields ").appendValue(item.getFields());
    }
  }

  private static class NormalFacetFieldMatcher extends FacetFieldMatcher {

    private String name;

    public NormalFacetFieldMatcher(String name) {
      this.name = name;
    }

    @Override
    protected boolean matchesSafely(FacetField item) {
      return name.equals(item.getName());
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("FacetField with name ").appendValue(name);
    }

    @Override
    protected void describeMismatchSafely(FacetField item, Description mismatchDescription) {
      mismatchDescription.appendText("FacetField with name ").appendValue(item.getName());
    }

  }

}
