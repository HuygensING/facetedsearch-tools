package nl.knaw.huygens.solr;

import nl.knaw.huygens.facetedsearch.model.FacetParameter;
import nl.knaw.huygens.facetedsearch.model.SortParameter;

/**
 * This interface is used to validate the faceted search parameters.
 *
 */
public interface FacetedSearchParametersValidator {
  boolean facetExists(FacetParameter facet);

  boolean isValidReturnField(String fieldName);

  boolean sortParameterExists(SortParameter sortParameter);
}
