package nl.knaw.huygens.facetedsearch.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.google.common.base.Objects;

public class FacetDefinition {
  String name = "";
  String title = "";
  FacetType type = FacetType.LIST;

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

    switch (type) {
    case RANGE:
      addRangeFacet(result, queryResponse);
      break;
    default:
      addDefaultFacet(result, queryResponse);
    }

  }

  @SuppressWarnings("rawtypes")
  private void addRangeFacet(FacetedSearchResult result, QueryResponse queryResponse) {
    for (org.apache.solr.client.solrj.response.RangeFacet solrRange : queryResponse.getFacetRanges()) {
      if (Objects.equal(getName(), solrRange.getName())) {
        long lowerLimit = (Long) solrRange.getStart();
        long upperLimit = (Long) solrRange.getEnd();

        result.addFacet(new RangeFacet(name, title, lowerLimit, upperLimit));

      }
    }

  }

  private void addDefaultFacet(FacetedSearchResult result, QueryResponse queryResponse) {
    DefaultFacet facet = new DefaultFacet(getName(), getTitle());
    org.apache.solr.client.solrj.response.FacetField solrField = queryResponse.getFacetField(getName());

    for (Count count : solrField.getValues()) {
      facet.addOption(new FacetOption(count.getName(), count.getCount()));
    }

    result.addFacet(facet);
  }

}
