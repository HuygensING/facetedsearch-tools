package nl.knaw.huygens.solr;

import java.util.Collection;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

public interface SolrServerWrapper {

  /**
   * @throws IndexException if an error occurs.
   */
  void emptyServer() throws IndexException;

  /**
   * @throws IndexException if an error occurs.
   */
  void shutdown() throws IndexException;

  /**
   * Checks the running status of the server.
   * @return the boolean value <code>true</code> if everything is OK,
   * <code>false</code> otherwise.
   */
  boolean ping();

  /**
   * Adds a document to the index, replacing a previously added document
   * with the same unique id.
   * @param coreName to add the document to.
   * @param doc the document to add.
   * @throws IndexException if an error occurs.
   */
  void add(String coreName, SolrInputDocument doc) throws IndexException;

  /**
   * Adds a document to the index, replacing a previously added document
   * with the same unique id.
   * @param coreName to add the documents to.
   * @param docs the collection of documents to add.
   * @throws IndexException if an error occurs.
   */
  void add(String coreName, Collection<SolrInputDocument> docs) throws IndexException;

  /**
   * Delete an indexed item for the core of {@code coreName} with the {@code id}.
   * @param coreName the core to delete from.
   * @param id the id of the item to delete.
   * @throws IndexException if an error occurs.
   */
  void deleteById(String coreName, String id) throws IndexException;

  /**
   * Search the index represented by {@code coreName}.
   * @param coreName the name of the core to search.
   * @param query that should be search.
   * @return the result of the query, that is executed on the core.
   * @throws IndexException if an error occurs.
   */
  QueryResponse search(String coreName, SolrQuery query) throws IndexException;

}
