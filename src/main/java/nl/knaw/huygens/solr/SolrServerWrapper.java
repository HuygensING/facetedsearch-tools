package nl.knaw.huygens.solr;

import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

public interface SolrServerWrapper {

  /**
   * Empty the index that belongs to the core.
   * @throws IndexException if an error occurs.
   */
  void empty() throws IndexException;

  /**
   * Shutdown the core.
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
   * @param doc the document to add.
   * @throws IndexException if an error occurs.
   */
  void add(SolrInputDocument doc) throws IndexException;

  /**
   * Adds a document to the index, replacing a previously added document
   * with the same unique id.
   * @param docs the collection of documents to add.
   * @throws IndexException if an error occurs.
   */
  void add(Collection<SolrInputDocument> docs) throws IndexException;

  /**
   * Commit all the currently added items.
   * @throws IndexException if an error occurs.
   */
  void commit() throws IndexException;

  /**
   * Delete an indexed item with the {@code id}.
   * @param id the id of the item to delete.
   * @throws IndexException if an error occurs.
   */
  void deleteById(String id) throws IndexException;

  /**
   * Delete all indexed items with id's in the list {@code ids}.
   * @param ids the id's to delete.
   * @throws IndexException if an error occurs.
   */
  void deleteById(List<String> ids) throws IndexException;

  /**
   * Delete all items found by the {@code query}.
   * @param query the query that is used to find the items to delete.
   * @throws IndexException if an error occurs.
   */
  void deleteByQuery(String query) throws IndexException;

  /**
   * Search the index represented by {@code coreName}.
   * @param query that should be search.
   * @return the result of the query, that is executed on the core.
   * @throws IndexException if an error occurs.
   */
  QueryResponse search(SolrQuery query) throws IndexException;

}
