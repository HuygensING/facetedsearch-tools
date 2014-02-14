package nl.knaw.huygens.solr;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.NoSuchFieldInIndexException;
import nl.knaw.huygens.facetedsearch.model.WrongFacetValueException;

public interface SearchServer {

  /**
   * Search the index represented by {@code coreName}.
   * @param searchParameters that should be search.
   * @return the result of the query, that is executed on the core.
   * @throws IndexException if an error occurs.
   * @throws WrongFacetValueException when the {@code searchParameters} contain a facet with a wrong value.
   * @throws NoSuchFieldInIndexException when the {@code searchParameters} contain a field or a facet that is not recognized.
   */
  <T extends FacetedSearchParameters<T>> SolrQueryResponse search(FacetedSearchParameters<T> searchParameters, FacetedSearchParametersValidator validator) throws IndexException,
      NoSuchFieldInIndexException, WrongFacetValueException;

  /**
   * Checks the running status of the server.
   * @return the boolean value <code>true</code> if everything is OK,
   * <code>false</code> otherwise.
   */
  boolean ping();

  /**
   * Shutdown the server.
   */
  void shutdown();

}