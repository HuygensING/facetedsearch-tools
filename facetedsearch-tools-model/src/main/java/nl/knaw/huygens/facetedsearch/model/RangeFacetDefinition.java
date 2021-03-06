package nl.knaw.huygens.facetedsearch.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.RangeFacetField;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.google.common.collect.Lists;

public class RangeFacetDefinition extends FacetDefinition {

  private String upperLimitField;
  private String lowerLimitField;

  public String getLowerLimitField() {
    return lowerLimitField;
  }

  public String getUpperLimitField() {
    return upperLimitField;
  }

  public RangeFacetDefinition setLowerLimitField(String lowerLimitField) {
    this.lowerLimitField = lowerLimitField;
    return this;
  }

  public RangeFacetDefinition setUpperLimitField(String upperLimitField) {
    this.upperLimitField = upperLimitField;
    return this;
  }

  @Override
  public void addFacetToResult(FacetedSearchResult result, QueryResponse queryResponse) {
    if (facetFieldContainsValues(queryResponse, lowerLimitField) && //
        facetFieldContainsValues(queryResponse, upperLimitField)) {
      long minValue = Collections.min(getValuesOfFacetField(queryResponse.getFacetField(lowerLimitField)));
      long maxValue = Collections.max(getValuesOfFacetField(queryResponse.getFacetField(upperLimitField)));

      result.addFacet(new RangeFacet(getName(), getTitle(), minValue, maxValue));
    }
  }

  private Collection<? extends Long> getValuesOfFacetField(FacetField facetField) {
    List<Long> values = Lists.newArrayList();

    for (Count count : facetField.getValues()) {
      values.add(Long.valueOf(count.getName()));
    }

    return values;
  }

  @Override
  public Collection<String> getFields() {
    return Lists.newArrayList(lowerLimitField, upperLimitField);
  }

  @Override
  public nl.knaw.huygens.facetedsearch.model.parameters.FacetField toFacetField() {
    RangeFacetField field = new RangeFacetField(getName(), lowerLimitField, upperLimitField);
    return field;
  }

  @Override
  public void appendQueryValue(StringBuilder stringBuilder, FacetParameter facetParameter) {
    stringBuilder.append("+(") //
        .append(lowerLimitField).append(":") //
        .append(facetParameter.getQueryValue()) //
        .append(" ") //
        .append(upperLimitField).append(":") //
        .append(facetParameter.getQueryValue()) //
        .append(")");

  }
}
