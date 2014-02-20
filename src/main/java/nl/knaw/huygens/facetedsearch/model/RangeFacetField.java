package nl.knaw.huygens.facetedsearch.model;

import java.util.List;

import com.google.common.collect.Lists;

public class RangeFacetField extends FacetField {
  private final String lowerField;
  private final String upperField;

  /**
   * A facet field for ranges. When there is some uncertainty is allowed for the values of
   * this field two facets can be used one for the lower limit and one for the upper limit. 
   * @param facetName name of the facet.
   * @param lowerField name of the facet that contains the lower limit
   * @param upperField name of the facet that contains the upper limit
   */
  public RangeFacetField(String facetName, String lowerField, String upperField) {
    super(facetName);
    this.lowerField = lowerField;
    this.upperField = upperField;
  }

  /**
   * Use this constructor, when the field for lower and upper limit are the same.
   * @param facetName name of the facet.
   * @param field name of the facet that contains the lower limit and the upper limit
   */
  public RangeFacetField(String facetName, String field) {
    this(facetName, field, field);
  }

  @Override
  public List<String> getFields() {
    return Lists.newArrayList(lowerField, upperField);
  }

}
