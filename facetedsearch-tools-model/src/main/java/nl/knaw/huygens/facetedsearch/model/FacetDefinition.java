package nl.knaw.huygens.facetedsearch.model;

import java.util.Collection;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetField;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.google.common.collect.Lists;

public class FacetDefinition {
  private String name = "";
  private String title = "";
  private FacetType type = FacetType.LIST;

  public String getName() {
    return name;
  }

  public FacetDefinition setName(String name) {
    this.name = name;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public FacetDefinition setTitle(String title) {
    this.title = title;
    return this;
  }

  public FacetType getType() {
    return type;
  }

  public FacetDefinition setType(FacetType type) {
    this.type = type;
    return this;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE, false);
  }

  public void addFacetToResult(FacetedSearchResult result, QueryResponse queryResponse) {
    DefaultFacet facet = new DefaultFacet(getName(), getTitle(), getType());
    org.apache.solr.client.solrj.response.FacetField solrField = queryResponse.getFacetField(getName());

    if (facetFieldContainsValues(queryResponse, getName())) {
      for (Count count : solrField.getValues()) {
        facet.addOption(new FacetOption(count.getName(), count.getCount()));
      }
      result.addFacet(facet);
    }

  }

  public Collection<String> getFields() {
    return Lists.newArrayList(name);
  }

  public FacetField toFacetField() {
    return new FacetField(name);
  }

  protected boolean facetFieldContainsValues(QueryResponse queryResponse, String fieldName) {
    org.apache.solr.client.solrj.response.FacetField facetField = queryResponse.getFacetField(fieldName);
    return facetField != null && facetField.getValues() != null && !facetField.getValues().isEmpty();
  }

  public void appendQueryValue(StringBuilder stringBuilder, FacetParameter facetParameter) {
    // TODO Auto-generated method stub

  }
}
