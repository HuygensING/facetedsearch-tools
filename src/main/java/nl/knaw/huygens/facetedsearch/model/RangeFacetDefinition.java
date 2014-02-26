package nl.knaw.huygens.facetedsearch.model;

public class RangeFacetDefinition extends FacetDefinition {

  private String upperLimitField;
  private String lowerLimitField;

  public String getLowerLimitField() {
    return lowerLimitField;
  }

  public String getUpperLimitField() {
    return upperLimitField;
  }

  public RangeFacetDefinition setLowerLimitField(String lowerLimitField) {
    this.lowerLimitField = lowerLimitField;
    return this;
  }

  public RangeFacetDefinition setUpperLimitField(String upperLimitField) {
    this.upperLimitField = upperLimitField;
    return this;
  }

}
