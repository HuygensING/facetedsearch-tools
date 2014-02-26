package nl.knaw.huygens.facetedsearch.model;

public class RangeFacet extends Facet {
  private long lowerLimit;
  private long upperLimit;

  public RangeFacet(String name, String title, long lowerLimit, long upperLimit) {
    super(name, title);
    this.lowerLimit = lowerLimit;
    this.upperLimit = upperLimit;
  }

  @Override
  public RangeFacet combineWith(Facet otherFacet) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FacetType getType() {
    // TODO Auto-generated method stub
    return FacetType.RANGE;
  }

  public long getLowerLimit() {
    return lowerLimit;
  }

  public void setLowerLimit(long lowerLimit) {
    this.lowerLimit = lowerLimit;
  }

  public long getUpperLimit() {
    return upperLimit;
  }

  public void setUpperLimit(long upperLimit) {
    this.upperLimit = upperLimit;
  }

}
