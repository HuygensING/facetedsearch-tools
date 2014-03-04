package nl.knaw.huygens.facetedsearch.model;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetField;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;

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
    DefaultFacet facet = new DefaultFacet(getName(), getTitle());
    org.apache.solr.client.solrj.response.FacetField solrField = queryResponse.getFacetField(getName());

    for (Count count : solrField.getValues()) {
      facet.addOption(new FacetOption(count.getName(), count.getCount()));
    }

    result.addFacet(facet);
  }

  public boolean isValidFacetField(FacetField facetField) {
    // TODO add validation
    return true;
  }

  public boolean isValidFacetParameter(FacetParameter facetParameter) {
    // TODO add validation
    return true;
  }
}
