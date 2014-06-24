package nl.knaw.huygens.facetedsearch.model.parameters;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import nl.knaw.huygens.facetedsearch.model.FacetDefinition;

import com.google.common.collect.Maps;

/**
 * A class to help create a new IndexDescription
 *
 */
public class IndexDescriptionBuilder {
  private Map<String, FacetDefinition> facetDefinitionMap = Maps.newHashMap();
  private Collection<String> sortFields;
  private Collection<String> allIndexedFields;
  private Collection<String> fullTextSearchFields;

  public IndexDescriptionBuilder() {

  }

  public IndexDescriptionBuilder setFacetDefinitions(List<FacetDefinition> facetDefinitions) {
    for (FacetDefinition facetDefinition : facetDefinitions) {
      facetDefinitionMap.put(facetDefinition.getName(), facetDefinition);
    }
    return this;
  }

  public IndexDescriptionBuilder setSortFields(Collection<String> sortFields) {
    this.sortFields = sortFields;
    return this;
  }

  public IndexDescriptionBuilder setIndexedFields(Collection<String> indexFields) {
    this.allIndexedFields = indexFields;
    return this;
  }

  public IndexDescriptionBuilder setFullTextSearchFields(Collection<String> fullTextSearchFields) {
    this.fullTextSearchFields = fullTextSearchFields;
    return this;
  }

  public IndexDescription build() {
    return new IndexDescription(facetDefinitionMap, sortFields, fullTextSearchFields, allIndexedFields);
  }
}
