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

public abstract class AbstractSolrServer implements SearchServer, IndexServer {
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

  /* (non-Javadoc)
   * @see nl.knaw.huygens.solr.IndexServer#add(org.apache.solr.common.SolrInputDocument)
   */
  @Override
  public void add(SolrInputDocument doc) throws IndexException {
    try {
      getSolrServer().add(doc, commitWithin);
    } catch (Exception e) {
      handleException(e);
    }
  }

  /* (non-Javadoc)
   * @see nl.knaw.huygens.solr.IndexServer#add(java.util.Collection)
   */
  @Override
  public void add(Collection<SolrInputDocument> docs) throws IndexException {
    try {
      getSolrServer().add(docs, commitWithin);
    } catch (Exception e) {
      handleException(e);
    }
  }

  /* (non-Javadoc)
   * @see nl.knaw.huygens.solr.IndexServer#commit()
   */
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

  /* (non-Javadoc)
   * @see nl.knaw.huygens.solr.IndexServer#deleteById(java.lang.String)
   */
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

  /* (non-Javadoc)
   * @see nl.knaw.huygens.solr.IndexServer#deleteById(java.util.List)
   */
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

  /* (non-Javadoc)
   * @see nl.knaw.huygens.solr.IndexServer#deleteByQuery(java.lang.String)
   */
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

  /* (non-Javadoc)
   * @see nl.knaw.huygens.solr.IndexServer#empty()
   */
  @Override
  public void empty() throws IndexException {
    try {
      getSolrServer().deleteByQuery("*:*", commitWithin);
    } catch (Exception e) {
      handleException(e);
    }
  }

  /* (non-Javadoc)
   * @see nl.knaw.huygens.solr.IndexServer#ping()
   */
  @Override
  public boolean ping() {
    try {
      return getSolrServer().ping().getStatus() == 0;
    } catch (Exception e) {
      getLogger().error("ping failed with '{}'", e.getMessage());
      return false;
    }
  }

  /* (non-Javadoc)
   * @see nl.knaw.huygens.solr.SearchServer#search(nl.knaw.huygens.facetedsearch.model.FacetedSearchParameters, nl.knaw.huygens.solr.FacetedSearchParametersValidator)
   */
  @Override
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

  /* (non-Javadoc)
   * @see nl.knaw.huygens.solr.IndexServer#shutdown()
   */
  @Override
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
