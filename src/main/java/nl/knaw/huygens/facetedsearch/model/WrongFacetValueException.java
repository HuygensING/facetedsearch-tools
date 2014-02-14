package nl.knaw.huygens.facetedsearch.model;

public class WrongFacetValueException extends Exception {

  private static final long serialVersionUID = 1L;

  public WrongFacetValueException(String facetName, String value) {
    super(String.format("Facet with name \"%s\" have wrong value: \"%s\"", facetName, value));
  }
}
