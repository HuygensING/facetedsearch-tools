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

public abstract class AbstractSolrServer implements SolrServerWrapper {
  public static final String KEY_NUMFOUND = "numFound";

  private final int commitWithin;

  /**
   * @param commitWithinInSeconds all the changes to the server will be committed within the this time. 
   */
  public AbstractSolrServer(int commitWithinInSeconds) {
    // solr uses commits within milliseconds.
    this.commitWithin = commitWithinInSeconds * 1000;
  }

  @Override
  public void empty() throws IndexException {
    try {
      getSolrServer().deleteByQuery("*:*", commitWithin);
    } catch (Exception e) {
      handleException(e);
    }
  }

  @Override
  public void shutdown() throws IndexException {
    try {
      SolrServer server = getSolrServer();
      optimizeAndCommit(server);
    } catch (Exception e) {
      handleException(e);
    }
  }

  @Override
  public boolean ping() {
    try {
      return getSolrServer().ping().getStatus() == 0;
    } catch (Exception e) {
      getLogger().error("ping failed with '{}'", e.getMessage());
      return false;
    }
  }

  @Override
  public void add(SolrInputDocument doc) throws IndexException {
    try {
      getSolrServer().add(doc, commitWithin);
    } catch (Exception e) {
      handleException(e);
    }
  }

  @Override
  public void add(Collection<SolrInputDocument> docs) throws IndexException {
    try {
      getSolrServer().add(docs, commitWithin);
    } catch (Exception e) {
      handleException(e);
    }
  }

  @Override
  public void deleteById(String id) throws IndexException {
    try {
      getSolrServer().deleteById(id, commitWithin);
    } catch (SolrServerException e) {
      handleException(e);
    } catch (IOException e) {
      handleException(e);
    }
  }

  @Override
  public QueryResponse search(SolrQuery query) throws IndexException {
    QueryResponse response = null;
    try {
      response = getSolrServer().query(query);
    } catch (SolrServerException e) {
      handleException(e);
    }
    return response;
  }

  @Override
  public void commit() throws IndexException {
    try {
      getSolrServer().commit();
    } catch (SolrServerException e) {
      handleException(e);
    } catch (IOException e) {
      handleException(e);
    }
  }

  @Override
  public void deleteById(List<String> ids) throws IndexException {
    try {
      getSolrServer().deleteById(ids, commitWithin);
    } catch (SolrServerException e) {
      handleException(e);
    } catch (IOException e) {
      handleException(e);
    }

  }

  @Override
  public void deleteByQuery(String query) throws IndexException {
    try {
      getSolrServer().deleteByQuery(query, commitWithin);
    } catch (SolrServerException e) {
      handleException(e);
    } catch (IOException e) {
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
   * @throws IndexException
   */
  protected void handleException(Exception e) throws IndexException {
    throw new IndexException(e.getMessage());
  }

  protected void optimizeAndCommit(SolrServer server) throws SolrServerException, IOException {
    server.commit();
    server.optimize();
  }
}
