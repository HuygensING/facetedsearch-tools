package nl.knaw.huygens.facetedsearch;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.FacetDefinition;

import com.google.common.collect.Lists;

public class FacetFieldCollector {

  public String[] find(List<FacetDefinition> facetDefinitions) {
    List<String> facetFields = createFacetFieldCollection();

    for (FacetDefinition facetDefinition : facetDefinitions) {
      facetFields.addAll(facetDefinition.getFields());
    }

    return facetFields.toArray(new String[0]);
  }

  protected List<String> createFacetFieldCollection() {
    return Lists.newArrayList();
  }

}
