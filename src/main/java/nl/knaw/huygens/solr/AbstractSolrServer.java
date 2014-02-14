package nl.knaw.huygens.solr;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.NoSuchFieldInIndexException;
import nl.knaw.huygens.facetedsearch.model.WrongFacetValueException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;

public abstract class AbstractSolrServer {
  public static final String KEY_NUMFOUND = "numFound";

  private final int commitWithin;
  private final SolrQueryCreator queryCreator;

  /**
   * @param commitWithinInSeconds all the changes to the server will be committed within the this time. 
   */
  public AbstractSolrServer(int commitWithinInSeconds, SolrQueryCreator queryCreator) {
    // solr uses commits within milliseconds.
    this.commitWithin = commitWithinInSeconds * 1000;
    this.queryCreator = queryCreator;
  }

  /**
   * Adds a document to the index, replacing a previously added document
   * with the same unique id.
   * @param doc the document to add.
   * @throws IndexException if an error occurs.
   */
  public void add(SolrInputDocument doc) throws IndexException {
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
   * @throws IndexException if an error occurs.
   */
  public void add(Collection<SolrInputDocument> docs) throws IndexException {
    try {
      getSolrServer().add(docs, commitWithin);
    } catch (Exception e) {
      handleException(e);
    }
  }

  /**
   * Commit all the currently added items.
   * @throws IndexException if an error occurs.
   */
  public void commit() throws IndexException {
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
   * @throws IndexException if an error occurs.
   */
  public void deleteById(String id) throws IndexException {
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
   * @throws IndexException if an error occurs.
   */
  public void deleteById(List<String> ids) throws IndexException {
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
   * @throws IndexException if an error occurs.
   */
  public void deleteByQuery(String query) throws IndexException {
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
   * @throws IndexException if an error occurs.
   */
  public void empty() throws IndexException {
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

  /**
   * Search the index represented by {@code coreName}.
   * @param searchParameters that should be search.
   * @return the result of the query, that is executed on the core.
   * @throws IndexException if an error occurs.
   * @throws WrongFacetValueException when the {@code searchParameters} contain a facet with a wrong value.
   * @throws NoSuchFieldInIndexException when the {@code searchParameters} contain a field or a facet that is not recognized.
   */
  public <T extends FacetedSearchParameters<T>> SolrQueryResponse search(FacetedSearchParameters<T> searchParameters, FacetedSearchParametersValidator validator) throws IndexException,
      NoSuchFieldInIndexException, WrongFacetValueException {

    QueryResponse response = null;

    SolrQuery query = queryCreator.createSearchQuery(searchParameters, validator);

    try {
      response = getSolrServer().query(query);
    } catch (SolrServerException e) {
      handleException(e);
    }
    return new SolrQueryResponse(response);
  }

  /**
   * Shutdown the server.
   */
  public void shutdown() {
    try {
      commitAndOptimize();
    } catch (Exception e) {
      getLogger().error("An exception occured during shutdown: {}", e.getMessage());
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
   * @throws IndexException
   */
  protected void handleException(Exception e) throws IndexException {
    throw new IndexException(e.getMessage());
  }

  protected void commitAndOptimize() throws SolrServerException, IOException {
    getSolrServer().commit();
    getSolrServer().optimize();
  }
}
