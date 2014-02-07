package nl.knaw.huygens.solr;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import nl.knaw.huygens.LoggableObject;
import nl.knaw.huygens.facetedsearch.model.FacetCount;
import nl.knaw.huygens.facetedsearch.model.FacetCount.Option;
import nl.knaw.huygens.facetedsearch.model.FacetType;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

public abstract class AbstractSolrServer extends LoggableObject implements SolrServerWrapper {
  public static final String KEY_NUMFOUND = "numFound";

  public final int commitWithin;

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

  protected void optimizeAndCommit(SolrServer server) throws SolrServerException, IOException {
    server.commit();
    server.optimize();
  }

  @Override
  public boolean ping() {
    try {
      return getSolrServer().ping().getStatus() == 0;
    } catch (Exception e) {
      LOG.error("ping failed with '{}'", e.getMessage());
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

  /**
   * Because all exceptions handled this way.
   * @param e
   * @throws IndexException
   */
  protected void handleException(Exception e) throws IndexException {
    throw new IndexException(e.getMessage());
  }

  /**
   * Returns a list of facetinfo with counts.
   * @param field The FacetField to convert
   * @param title the title of the facet
   * @param type the FacetType of the facet
   */
  protected FacetCount convertFacet(FacetField field, String title, FacetType type) {
    if (field != null) {
      FacetCount facetCount = new FacetCount()//
          .setName(field.getName())//
          .setTitle(title)//
          .setType(type);
      List<Count> counts = field.getValues();
      if (counts != null) {
        for (Count count : counts) {
          Option option = new FacetCount.Option()//
              .setName(count.getName())//
              .setCount(count.getCount());
          facetCount.addOption(option);
        }
      }
      return facetCount;
    }
    return null;
  }

  /**
   * Get the right {@code SolrServer} to perform an action on. 
   * @return the {@code SolrServer}.
   */
  protected abstract SolrServer getSolrServer();

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

}
