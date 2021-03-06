package nl.knaw.huygens.facetedsearch.model.parameters;

public enum SortDirection {
  ASCENDING("asc"), DESCENDING("desc");

  private final String value;

  SortDirection(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
