package nl.knaw.huygens.facetedsearch.model;

import com.google.common.base.Objects;

public class DefaultOption {
  private String name;
  private long count;

  public DefaultOption(String name, long count) {
    this.name = name;
    this.count = count;
  }

  public String getName() {
    return name;
  }

  public long getCount() {
    return count;
  }

  public boolean isCombinable(DefaultOption otherOption) {
    if (otherOption == null) {
      return false;
    } else {
      return Objects.equal(name, otherOption.getName());
    }
  }

  public DefaultOption combineWith(DefaultOption otherOption) {
    long combinedCount = count + otherOption.count;

    return new DefaultOption(name, combinedCount);
  }
}
