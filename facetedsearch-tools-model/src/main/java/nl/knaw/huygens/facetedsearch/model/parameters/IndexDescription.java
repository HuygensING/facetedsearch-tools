package nl.knaw.huygens.facetedsearch.model.parameters;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import nl.knaw.huygens.facetedsearch.model.FacetDefinition;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * Describes the index:
 * - Facets / facet fields
 * - Sort fields
 * - Full text search fields
 * - Other Solr fields
 */
public class IndexDescription {
  public static final String SCORE = "score";

  private final Collection<String> sortFieldList;
  private final Map<String, FacetDefinition> facetDefinitionMap;
  private final Collection<String> allIndexedFields;
  private final Collection<String> fullTextSearchFields;

  public IndexDescription(Map<String, FacetDefinition> facetDefinitionMap, Collection<String> sortFieldList, Collection<String> fullTextSearchFields, Collection<String> allIndexedFields) {
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
    return isScoreField(resultField) ? true : allIndexedFields.contains(resultField);
  }

  public boolean doesSortParameterExist(SortParameter sortParameter) {
    final String fieldName = sortParameter.getFieldName();
    return isScoreField(fieldName) ? true : sortFieldList.contains(fieldName);
  }

  private boolean isScoreField(String fieldName) {
    return Objects.equal(SCORE, fieldName);
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
