package nl.knaw.huygens.facetedsearch.model.parameters;


public class RangeParameter extends FacetParameter {
  private long lowerLimit = -1;
  private long upperLimit = -1;

  public RangeParameter(String name, long lowerLimit, long upperLimit) {
    super(name);
    this.setLowerLimit(lowerLimit);
    this.setUpperLimit(upperLimit);
  }

  public long getLowerLimit() {
    return lowerLimit;
  }

  private void setLowerLimit(long lowerLimit) {
    this.lowerLimit = lowerLimit;
  }

  public long getUpperLimit() {
    return upperLimit;
  }

  private void setUpperLimit(long upperLimit) {
    this.upperLimit = upperLimit;
  }

  @Override
  public String getQueryValue() {
    return String.format("[%s TO %s]", this.getLowerLimit(), this.getUpperLimit());

  }

}
