package nl.knaw.huygens.facetedsearch;

import nl.knaw.huygens.facetedsearch.model.DefaultFacetDefinition;
import nl.knaw.huygens.facetedsearch.model.FacetType;
import nl.knaw.huygens.facetedsearch.model.RangeFacetDefinition;

import com.google.common.base.Preconditions;

public class FacetDefinitionBuilder {

  private String name;
  private String title;
  private FacetType type;

  private String lowerLimitField;
  private String upperLimitField;

  public FacetDefinitionBuilder(String name, String title, FacetType type) {
    this.name = name;
    this.title = title;
    this.type = type;
  }

  public FacetDefinitionBuilder setLowerLimitField(String lowerLimitField) {
    this.lowerLimitField = lowerLimitField;
    return this;
  }

  public FacetDefinitionBuilder setUpperLimitField(String upperLimitField) {
    this.upperLimitField = upperLimitField;
    return this;
  }

  public DefaultFacetDefinition build() {
    switch (type) {
      case RANGE:
        return buildRangeFacetDefinition();
      default:
        return buildDefaultFacetDefinition();
    }
  }

  private DefaultFacetDefinition buildRangeFacetDefinition() {
    Preconditions.checkNotNull(lowerLimitField);
    Preconditions.checkNotNull(upperLimitField);

    return new RangeFacetDefinition().setLowerLimitField(lowerLimitField).setUpperLimitField(upperLimitField)//
        .setName(name).setTitle(title).setType(type);
  }

  private DefaultFacetDefinition buildDefaultFacetDefinition() {

    return new DefaultFacetDefinition().setName(name).setTitle(title).setType(type);
  }

}
