package nl.knaw.huygens.facetedsearch.model;

import java.util.List;

import com.google.common.collect.Lists;

public class DefaultFacet extends Facet<DefaultFacet> {
  private List<DefaultOption> options;

  public DefaultFacet(String name, String title) {
    super(name, title);
    options = Lists.<DefaultOption> newArrayList();
  }

  @Override
  public DefaultFacet combineWith(DefaultFacet otherFacet) {
    if (!isCombinable(otherFacet)) {
      throw new RuntimeException("facets can't be combined, name/title/type doesn't match.");
    }

    DefaultFacet combinedFacet = createCombinedListFacet();

    for (DefaultOption option : options) {
      for (DefaultOption otherOption : otherFacet.getOptions()) {
        if (option.isCombinable(otherOption)) {
          combinedFacet.addOption(option.combineWith(otherOption));
        }
      }
    }

    // add missing options
    addMissingOptions(combinedFacet, options);
    addMissingOptions(combinedFacet, otherFacet.getOptions());

    return combinedFacet;
  }

  protected void addMissingOptions(DefaultFacet combinedFacet, List<DefaultOption> options) {
    for (DefaultOption option : options) {
      if (!combinedFacet.containsOption(option)) {
        combinedFacet.addOption(option);
      }
    }
  }

  protected DefaultFacet createCombinedListFacet() {
    return new DefaultFacet(getName(), getTitle());
  }

  @Override
  public FacetType getType() {
    return FacetType.LIST;
  }

  public void addOption(DefaultOption option) {
    options.add(option);
  }

  public List<DefaultOption> getOptions() {
    return options;
  }

  public boolean containsOption(DefaultOption option) {
    for (DefaultOption otherOption : options) {
      if (otherOption.isCombinable(option)) {
        return true;
      }
    }
    return false;
  }
}
