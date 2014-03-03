package nl.knaw.huygens.facetedsearch.model;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetField;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;

public class DefaultFacetDefinition implements FacetDefinition {
  private String name = "";
  private String title = "";
  private FacetType type = FacetType.LIST;

  /* (non-Javadoc)
   * @see nl.knaw.huygens.facetedsearch.model.FacetDefinition#getName()
   */
  @Override
  public String getName() {
    return name;
  }

  public DefaultFacetDefinition setName(String name) {
    this.name = name;
    return this;
  }

  /* (non-Javadoc)
   * @see nl.knaw.huygens.facetedsearch.model.FacetDefinition#getTitle()
   */
  @Override
  public String getTitle() {
    return title;
  }

  public DefaultFacetDefinition setTitle(String title) {
    this.title = title;
    return this;
  }

  /* (non-Javadoc)
   * @see nl.knaw.huygens.facetedsearch.model.FacetDefinition#getType()
   */
  @Override
  public FacetType getType() {
    return type;
  }

  public DefaultFacetDefinition setType(FacetType type) {
    this.type = type;
    return this;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE, false);
  }

  /* (non-Javadoc)
   * @see nl.knaw.huygens.facetedsearch.model.FacetDefinition#addFacetToResult(nl.knaw.huygens.facetedsearch.model.FacetedSearchResult, org.apache.solr.client.solrj.response.QueryResponse)
   */
  @Override
  public void addFacetToResult(FacetedSearchResult result, QueryResponse queryResponse) {
    DefaultFacet facet = new DefaultFacet(getName(), getTitle());
    org.apache.solr.client.solrj.response.FacetField solrField = queryResponse.getFacetField(getName());

    for (Count count : solrField.getValues()) {
      facet.addOption(new FacetOption(count.getName(), count.getCount()));
    }

    result.addFacet(facet);
  }

  /* (non-Javadoc)
   * @see nl.knaw.huygens.facetedsearch.model.FacetDefinition#isValidFacetField(nl.knaw.huygens.facetedsearch.model.parameters.FacetField)
   */
  @Override
  public boolean isValidFacetField(FacetField facetField) {
    throw new UnsupportedOperationException("Yet to be implemented");
  }

  /* (non-Javadoc)
   * @see nl.knaw.huygens.facetedsearch.model.FacetDefinition#isValidFacetParameter(nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter)
   */
  @Override
  public boolean isValidFacetParameter(FacetParameter facetParameter) {
    throw new UnsupportedOperationException("Yet to be implemented");
  }
}
