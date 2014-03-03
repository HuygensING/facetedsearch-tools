package nl.knaw.huygens.facetedsearch.model;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetField;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter;

import org.apache.solr.client.solrj.response.QueryResponse;

public interface FacetDefinition {

  String getName();

  String getTitle();

  FacetType getType();

  void addFacetToResult(FacetedSearchResult result, QueryResponse queryResponse);

  boolean isValidFacetField(FacetField facetField);

  boolean isValidFacetParameter(FacetParameter facetParameter);

}