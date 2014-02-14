package nl.knaw.huygens.facetedsearch.model;


public abstract class FacetParameter {
  private String name = "";

  public FacetParameter(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public abstract String getQueryValue();
}
