package nl.knaw.huygens.facetedsearch.model.parameters;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * A facet field that should be include in the search result.
 */
public class FacetField {
  private final String facetName;

  public FacetField(String facetName) {
    this.facetName = facetName;
  }

  public List<String> getFields() {
    return Lists.newArrayList(facetName);
  }

  public String getName() {
    return facetName;
  }
}
