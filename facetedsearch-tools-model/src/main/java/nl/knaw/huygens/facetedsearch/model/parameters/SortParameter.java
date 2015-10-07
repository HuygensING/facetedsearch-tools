package nl.knaw.huygens.facetedsearch.model.parameters;

public class SortParameter {
  private String fieldname;
  private SortDirection direction = SortDirection.ASCENDING;

  public SortParameter() {

  }

  public SortParameter(String fieldName, SortDirection direction) {
    fieldname = fieldName;
    this.direction = direction;
  }

  public String getFieldname() {
    return fieldname;
  }

  public SortParameter setFieldname(String fieldname) {
    this.fieldname = fieldname;
    return this;
  }

  public SortDirection getDirection() {
    return direction;
  }

  public SortParameter setDirection(SortDirection direction) {
    this.direction = direction;
    return this;
  }

}
