package nl.knaw.huygens.facetedsearch.model.parameters;

import java.util.List;
import java.util.Map;

import nl.knaw.huygens.facetedsearch.model.FacetDefinition;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;

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
  private final List<String> fullTextSearchFields;

  public IndexDescription(Map<String, FacetDefinition> facetDefinitionMap, List<String> sortFieldList, List<String> fullTextSearchFields, List<String> allIndexedFields) {
    this.facetDefinitionMap = facetDefinitionMap;
    this.sortFieldList = sortFieldList;
    this.fullTextSearchFields = fullTextSearchFields;
    this.allIndexedFields = allIndexedFields;
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

  public void addFacetDataToSearchResult(FacetedSearchResult searchResult, QueryResponse queryResponse) {
    for (FacetDefinition facetDefinition : facetDefinitionMap.values()) {
      facetDefinition.addFacetToResult(searchResult, queryResponse);
    }
  }

  public boolean doesFullTextSearchFieldExist(String fieldName) {
    return fullTextSearchFields.contains(fieldName);
  }
}
