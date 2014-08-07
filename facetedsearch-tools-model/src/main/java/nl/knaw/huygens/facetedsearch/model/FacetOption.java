package nl.knaw.huygens.facetedsearch.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.base.Objects;

public class FacetOption {
  private String name;
  private long count;

  public FacetOption(String name, long count) {
    this.name = name;
    this.count = count;
  }

  public String getName() {
    return name;
  }

  public long getCount() {
    return count;
  }

  public boolean isCombinable(FacetOption otherOption) {
    if (otherOption == null) {
      return false;
    } else {
      return Objects.equal(name, otherOption.getName());
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof FacetOption)) {
      return false;
    }

    FacetOption other = (FacetOption) obj;

    return Objects.equal(name, other.name) && Objects.equal(count, other.count);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, count);
  }

  public FacetOption combineWith(FacetOption otherOption) {
    long combinedCount = count + otherOption.count;

    return new FacetOption(name, combinedCount);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE, false);
  }
}
