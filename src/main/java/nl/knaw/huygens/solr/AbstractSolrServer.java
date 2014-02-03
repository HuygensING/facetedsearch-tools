package nl.knaw.huygens.solr;

import java.util.Collection;
import java.util.List;

import nl.knaw.huygens.LoggableObject;
import nl.knaw.huygens.facetedsearch.model.FacetCount;
import nl.knaw.huygens.facetedsearch.model.FacetCount.Option;
import nl.knaw.huygens.facetedsearch.model.FacetType;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrInputDocument;

public abstract class AbstractSolrServer extends LoggableObject implements SolrServerWrapper {
  public static final String KEY_NUMFOUND = "numFound";

  protected SolrServer server;

  protected abstract void setServer();

  @Override
  public void initialize() throws IndexException {
    try {
      server.deleteByQuery("*:*");
    } catch (Exception e) {
      throw new IndexException(e.getMessage());
    }
  }

  @Override
  public void shutdown() throws IndexException {
    try {
      server.commit();
      server.optimize();
    } catch (Exception e) {
      throw new IndexException(e.getMessage());
    }
  }

  @Override
  public boolean ping() {
    try {
      return server.ping().getStatus() == 0;
    } catch (Exception e) {
      LOG.error("ping failed with '{}'", e.getMessage());
      return false;
    }
  }

  @Override
  public void add(SolrInputDocument doc) throws IndexException {
    try {
      server.add(doc);
    } catch (Exception e) {
      throw new IndexException(e.getMessage());
    }
  }

  @Override
  public void add(Collection<SolrInputDocument> docs) throws IndexException {
    try {
      server.add(docs);
    } catch (Exception e) {
      throw new IndexException(e.getMessage());
    }
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

}
