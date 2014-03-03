package nl.knaw.huygens.solr;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import nl.knaw.huygens.facetedsearch.SolrSearcher;
import nl.knaw.huygens.facetedsearch.model.FacetDefinition;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;

public abstract class AbstractSolrServer implements SolrSearcher {
  public static final String KEY_NUMFOUND = "numFound";

  private final int commitWithin;
  private final List<FacetDefinition> facetDefinitions;

  /**
   * @param commitWithinInSeconds all the changes to the server will be committed within the this time. 
   */
  public AbstractSolrServer(int commitWithinInSeconds, List<FacetDefinition> facetDefinitions) {
    this.facetDefinitions = facetDefinitions;
    // solr uses commits within milliseconds.
    this.commitWithin = commitWithinInSeconds * 1000;
  }

  /**
   * Adds a document to the index, replacing a previously added document
   * with the same unique id.
   * @param doc the document to add.
   * @throws IOException 
   * @throws SolrServerException 
   */
  public void add(SolrInputDocument doc) throws SolrServerException, IOException {

    getSolrServer().add(doc, commitWithin);

  }

  /**
   * Adds a document to the index, replacing a previously added document
   * with the same unique id.
   * @param docs the collection of documents to add.
   * @throws IOException 
   * @throws SolrServerException 
   */
  public void add(Collection<SolrInputDocument> docs) throws SolrServerException, IOException {

    getSolrServer().add(docs, commitWithin);

  }

  /**
   * Commit all the currently added items.
   * @throws IOException 
   * @throws SolrServerException 
   */
  public void commit() throws SolrServerException, IOException {
    getSolrServer().commit();
  }

  /**
   * Delete an indexed item with the {@code id}.
   * @param id the id of the item to delete.
   * @throws IOException 
   * @throws SolrServerException 
   */
  public void deleteById(String id) throws SolrServerException, IOException {

    getSolrServer().deleteById(id, commitWithin);
  }

  /**
   * Delete all indexed items with id's in the list {@code ids}.
   * @param ids the id's to delete.
   * @throws IOException 
   * @throws SolrServerException 
   */
  public void deleteById(List<String> ids) throws SolrServerException, IOException {

    getSolrServer().deleteById(ids, commitWithin);

  }

  /**
   * Delete all items found by the {@code query}.
   * @param query the query that is used to find the items to delete.
   * @throws IOException 
   * @throws SolrServerException 
   */
  public void deleteByQuery(String query) throws SolrServerException, IOException {

    getSolrServer().deleteByQuery(query, commitWithin);

  }

  /**
   * Clear the server.
   * @throws IOException 
   * @throws SolrServerException 
   */
  public void empty() throws SolrServerException, IOException {
    getSolrServer().deleteByQuery("*:*", commitWithin);
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
  public QueryResponse search(SolrQuery query) throws SolrServerException {
    return getSolrServer().query(query);
  }

  /**
   * Shutdown the server.
   * @throws IOException 
   * @throws SolrServerException 
   */
  public void shutdown() throws SolrServerException, IOException {

    commitAndOptimize();

  }

  @Override
  public List<FacetDefinition> getFacetDefinitions() {
    return this.facetDefinitions;
  }

  protected abstract Logger getLogger();

  /**
   * Get the right {@code SolrServer} to perform an action on. 
   * @return the {@code SolrServer}.
   */
  protected abstract SolrServer getSolrServer();

  protected void commitAndOptimize() throws SolrServerException, IOException {
    getSolrServer().commit();
    getSolrServer().optimize();
  }
}
