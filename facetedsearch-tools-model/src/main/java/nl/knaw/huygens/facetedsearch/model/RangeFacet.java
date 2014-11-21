package nl.knaw.huygens.facetedsearch.model;

import java.util.List;

import com.google.common.collect.Lists;

public class RangeFacet extends Facet {
  private RangeFacetOption option;

  public RangeFacet(String name, String title, long lowerLimit, long upperLimit) {
    super(name, title);
    option = new RangeFacetOption();
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

  private long getLowerLimit() {
    return option.getLowerLimit();
  }

  public void setLowerLimit(long lowerLimit) {
    option.setLowerLimit(lowerLimit);
  }

  private long getUpperLimit() {
    return option.getUpperLimit();
  }

  public void setUpperLimit(long upperLimit) {
    option.setUpperLimit(upperLimit);
  }

  public List<RangeFacetOption> getOptions() {
    return Lists.newArrayList(option);
  }

  public static class RangeFacetOption {
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
