package nl.knaw.huygens.facetedsearch.model;

public class WrongFacetValueException extends Exception {

  private static final long serialVersionUID = 1L;

  public WrongFacetValueException(String facetName, long lowerLimit, long upperLimit) {
    super(String.format("Lower limit \"%d\" or upper limit \"%d\" not allowed for facet \"%s\"", lowerLimit, upperLimit, facetName));
  }
}
