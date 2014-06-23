package nl.knaw.huygens.facetedsearch.model.parameters;

import java.util.List;
import java.util.Map;

import nl.knaw.huygens.facetedsearch.model.FacetDefinition;

import com.google.common.collect.Lists;

/**
 * Describes the index:
 * - Facets / facet fields
 * - Sort fields
 * - Full text search fields
 * - Other Solr fields
 */
public class IndexDescription {
  private final List<String> sortFieldList;
  private final Map<String, FacetDefinition> facetDefinitionMap;
  private final List<String> allIndexedFields;

  public IndexDescription(Map<String, FacetDefinition> facetDefinitionMap, List<String> sortFieldList, List<String> allIndexedFieldsMock) {
    this.facetDefinitionMap = facetDefinitionMap;
    this.sortFieldList = sortFieldList;
    this.allIndexedFields = allIndexedFieldsMock;
  }

  public boolean doesFacetFieldExist(FacetField facetField) {
    return facetDefinitionMap.containsKey(facetField.getName());
  }

  public boolean doesFacetParameterExist(FacetParameter facetParameter) {
    return facetDefinitionMap.containsKey(facetParameter.getName());
  }

  public boolean doesResultFieldExist(String resultField) {
    return allIndexedFields.contains(resultField);
  }

  public boolean doesSortParameterExist(SortParameter sortParameter) {
    return sortFieldList.contains(sortParameter.getFieldname());
  }

  public String[] findFacetFields() {
    List<String> facetFields = Lists.newArrayList();

    for (FacetDefinition facetDefinition : facetDefinitionMap.values()) {
      facetFields.addAll(facetDefinition.getFields());
    }

    return facetFields.toArray(new String[0]);
  }
}
