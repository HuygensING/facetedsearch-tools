package nl.knaw.huygens.facetedsearch.model.parameters;

public class FullTextSearchParameter {
  private String name;
  private String term;

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

  public String getQueryValue(String generalTerm) {
    StringBuilder builder = new StringBuilder();
    builder.append(name);
    builder.append(":");
    builder.append(term);
    if (isAUseFullTerm(generalTerm)) {
      builder.append(" ");
      builder.append(generalTerm);
    }
    return builder.toString();
  }

  private boolean isAUseFullTerm(String generalTerm) {
    return generalTerm != null && !"*".equals(generalTerm);
  }
}
