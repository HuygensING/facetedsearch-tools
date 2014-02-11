package nl.knaw.huygens.facetedsearch.model;

public class NoSuchFieldInIndexException extends Exception {

  private static final long serialVersionUID = 1L;

  public NoSuchFieldInIndexException(String fieldName) {
    super(String.format("Field with name \"%s\" does not exist", fieldName));
  }

}
