package nl.knaw.huygens.facetedsearch.model;

public abstract class Facet<T extends Facet<T>> {
  private String name;
  private String title;

  public Facet(String name, String title) {
    this.name = name;
    this.title = title;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Method to check if the facets can be combined.
   * @param other the facet to check with if they can be combined.
   * @return true if they can be combined false if not.
   */
  protected boolean isCombinable(T other) {
    return other.getName().equals(getName()) //
        && other.getTitle().equals(getTitle()) //
        && other.getType().equals(getType());
  }

  /**
   * Combines the current facet to another facets into a new facet.
   * @param otherFacet the facet to combine this facet with.
   * @return a new {@code Facet} with the combined data.
   * @throws RuntimeException when the facets cannot be combined.
   */
  public abstract T combineWith(T otherFacet);

  public abstract FacetType getType();

}
