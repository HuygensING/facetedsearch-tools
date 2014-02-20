package nl.knaw.huygens.solr;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;

public abstract class AbstractSolrServer implements SolrCoreWrapper {
  public static final String KEY_NUMFOUND = "numFound";

  private final int commitWithin;

  /**
   * @param commitWithinInSeconds all the changes to the server will be committed within the this time. 
   */
  public AbstractSolrServer(int commitWithinInSeconds) {
    // solr uses commits within milliseconds.
    this.commitWithin = commitWithinInSeconds * 1000;
  }

  /**
   * Adds a document to the index, replacing a previously added document
   * with the same unique id.
   * @param doc the document to add.
   * @throws FacetedSearchException if an error occurs.
   */
  public void add(SolrInputDocument doc) throws FacetedSearchException {
    try {
      getSolrServer().add(doc, commitWithin);
    } catch (Exception e) {
      handleException(e);
    }
  }

  /**
   * Adds a document to the index, replacing a previously added document
   * with the same unique id.
   * @param docs the collection of documents to add.
   * @throws FacetedSearchException if an error occurs.
   */
  public void add(Collection<SolrInputDocument> docs) throws FacetedSearchException {
    try {
      getSolrServer().add(docs, commitWithin);
    } catch (Exception e) {
      handleException(e);
    }
  }

  /**
   * Commit all the currently added items.
   * @throws FacetedSearchException if an error occurs.
   */
  public void commit() throws FacetedSearchException {
    try {
      getSolrServer().commit();
    } catch (SolrServerException e) {
      handleException(e);
    } catch (IOException e) {
      handleException(e);
    }
  }

  /**
   * Delete an indexed item with the {@code id}.
   * @param id the id of the item to delete.
   * @throws FacetedSearchException if an error occurs.
   */
  public void deleteById(String id) throws FacetedSearchException {
    try {
      getSolrServer().deleteById(id, commitWithin);
    } catch (SolrServerException e) {
      handleException(e);
    } catch (IOException e) {
      handleException(e);
    }
  }

  /**
   * Delete all indexed items with id's in the list {@code ids}.
   * @param ids the id's to delete.
   * @throws FacetedSearchException if an error occurs.
   */
  public void deleteById(List<String> ids) throws FacetedSearchException {
    try {
      getSolrServer().deleteById(ids, commitWithin);
    } catch (SolrServerException e) {
      handleException(e);
    } catch (IOException e) {
      handleException(e);
    }

  }

  /**
   * Delete all items found by the {@code query}.
   * @param query the query that is used to find the items to delete.
   * @throws FacetedSearchException if an error occurs.
   */
  public void deleteByQuery(String query) throws FacetedSearchException {
    try {
      getSolrServer().deleteByQuery(query, commitWithin);
    } catch (SolrServerException e) {
      handleException(e);
    } catch (IOException e) {
      handleException(e);
    }

  }

  /**
   * Clear the server.
   * @throws FacetedSearchException if an error occurs.
   */
  public void empty() throws FacetedSearchException {
    try {
      getSolrServer().deleteByQuery("*:*", commitWithin);
    } catch (Exception e) {
      handleException(e);
    }
  }

  /**
   * Checks the running status of the server.
   * @return the boolean value <code>true</code> if everything is OK,
   * <code>false</code> otherwise.
   */
  public boolean ping() {
    try {
      return getSolrServer().ping().getStatus() == 0;
    } catch (Exception e) {
      getLogger().error("ping failed with '{}'", e.getMessage());
      return false;
    }
  }

  @Override
  public QueryResponse search(SolrQuery query) throws FacetedSearchException {
    try {
      return getSolrServer().query(query);
    } catch (SolrServerException e) {
      getLogger().error("An exception occured during shutdown: {}", e.getMessage());
      throw new FacetedSearchException(e.getMessage());
    }
  }

  /**
   * Shutdown the server.
   * @throws FacetedSearchException 
   */
  public void shutdown() throws FacetedSearchException {
    try {
      commitAndOptimize();
    } catch (Exception e) {
      getLogger().error("An exception occured during shutdown: {}", e.getMessage());
      handleException(e);
    }
  }

  protected abstract Logger getLogger();

  /**
   * Get the right {@code SolrServer} to perform an action on. 
   * @return the {@code SolrServer}.
   */
  protected abstract SolrServer getSolrServer();

  /**
   * Because all exceptions handled this way.
   * @param e
   * @throws FacetedSearchException
   */
  protected void handleException(Exception e) throws FacetedSearchException {
    throw new FacetedSearchException(e.getMessage());
  }

  protected void commitAndOptimize() throws SolrServerException, IOException {
    getSolrServer().commit();
    getSolrServer().optimize();
  }
}
