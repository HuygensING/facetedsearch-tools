package nl.knaw.huygens.facetedsearch.model.parameters;

public class FullTextSearchParameter {
  private String name;
  private String term;

  public FullTextSearchParameter() {}

  public FullTextSearchParameter(String name, String term) {
    this.name = name;
    this.term = term;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTerm() {
    return term;
  }

  public void setTerm(String term) {
    this.term = term;
  }
}
