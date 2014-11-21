package nl.knaw.huygens.facetedsearch.model;

public class RangeFacet extends Facet {
  private RangeFacetOptions options;

  public RangeFacet(String name, String title, long lowerLimit, long upperLimit) {
    super(name, title);
    options = new RangeFacetOptions();
    this.setLowerLimit(lowerLimit);
    this.setUpperLimit(upperLimit);
  }

  public RangeFacet(String name, String title) {
    this(name, title, 0, 0);
  }

  @Override
  public RangeFacet combineWith(Facet otherFacet) {
    if (!isCombinable(otherFacet)) {
      throwUncombinableFacetsException();
    }

    RangeFacet other = (RangeFacet) otherFacet;

    RangeFacet combinedFacet = new RangeFacet(getName(), getTitle());

    combinedFacet.setLowerLimit(Math.min(getLowerLimit(), other.getLowerLimit()));
    combinedFacet.setUpperLimit(Math.max(getUpperLimit(), other.getUpperLimit()));

    return combinedFacet;
  }

  @Override
  public FacetType getType() {
    return FacetType.RANGE;
  }

  public long getLowerLimit() {
    return options.getLowerLimit();
  }

  public void setLowerLimit(long lowerLimit) {
    options.setLowerLimit(lowerLimit);
  }

  public long getUpperLimit() {
    return options.getUpperLimit();
  }

  public void setUpperLimit(long upperLimit) {
    options.setUpperLimit(upperLimit);
  }

  private static class RangeFacetOptions {
    private long lowerLimit;
    private long upperLimit;

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

}
