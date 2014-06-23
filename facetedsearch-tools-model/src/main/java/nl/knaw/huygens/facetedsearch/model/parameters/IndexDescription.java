package nl.knaw.huygens.facetedsearch.model.parameters;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.FacetDefinition;

/**
 * Describes the index:
 * - Facets / facet fields
 * - Sort fields
 * - Full text search fields
 */
public class IndexDescription {
  public IndexDescription(List<FacetDefinition> facetDefinitions) {

  }

  public boolean doesSortParameterExist(SortParameter sortParameter) {
    throw new UnsupportedOperationException();
  }

  public boolean doesResultFieldExist(String resultField) {
    throw new UnsupportedOperationException();
  }

  public boolean doesFacetParameterExist(FacetParameter facetParameter) {
    throw new UnsupportedOperationException();
  }

  public boolean doesFacetFieldExist(FacetField facetField) {
    throw new UnsupportedOperationException();
  }
}
