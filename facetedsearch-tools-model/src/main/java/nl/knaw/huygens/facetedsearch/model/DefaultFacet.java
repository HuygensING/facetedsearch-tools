package nl.knaw.huygens.facetedsearch.model;

import java.util.List;

import com.google.common.collect.Lists;

public class DefaultFacet extends Facet {
  private List<FacetOption> options;
  private FacetType facetType;

  /**
   * Defaults the type to {@link FacetType.LIST}
   * @param name the name of the facet
   * @param title the title of the facet
   */
  public DefaultFacet(String name, String title) {
    this(name, title, FacetType.LIST);
  }

  public DefaultFacet(String name, String title, FacetType type) {
    super(name, title);
    facetType = type;
    options = Lists.<FacetOption> newArrayList();
  }

  @Override
  public DefaultFacet combineWith(Facet facet) {
    if (!isCombinable(facet)) {
      throwUncombinableFacetsException();
    }

    DefaultFacet otherFacet = (DefaultFacet) facet;

    DefaultFacet combinedFacet = createCombinedListFacet();

    for (FacetOption option : options) {
      for (FacetOption otherOption : otherFacet.getOptions()) {
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

  protected void addMissingOptions(DefaultFacet combinedFacet, List<FacetOption> options) {
    for (FacetOption option : options) {
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
    return facetType;
  }

  public void addOption(FacetOption option) {
    options.add(option);
  }

  public List<FacetOption> getOptions() {
    return options;
  }

  public void setOptions(List<FacetOption> options) {
    this.options = options;
  }

  public boolean containsOption(FacetOption option) {
    for (FacetOption otherOption : options) {
      if (otherOption.isCombinable(option)) {
        return true;
      }
    }
    return false;
  }
}
