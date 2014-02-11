package nl.knaw.huygens.solr;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalSolrServer extends AbstractSolrServer {

  private static final Logger LOG = LoggerFactory.getLogger(LocalSolrServer.class);

  public static final String SOLR_DIRECTORY = "solr";
  public static final String SOLR_CONFIG_FILE = "solrconfig.xml";

  private final String solrDir;
  private final String coreName;
  private CoreContainer container;
  private SolrServer server;

  public LocalSolrServer(String solrDir, String coreName, int commitWithinInSeconds) {
    super(commitWithinInSeconds);
    this.solrDir = StringUtils.defaultIfBlank(solrDir, SOLR_DIRECTORY);
    this.coreName = StringUtils.defaultIfBlank(coreName, "core1");
    createServer();
  }

  @Override
  public void shutdown() throws IndexException {
    try {
      super.optimizeAndCommit(getSolrServer());
    } catch (Exception e) {
      handleException(e);
    } finally {
      if (container != null) {
        container.shutdown();
      }
    }
  }

  private void createServer() {
    try {
      container = new CoreContainer(solrDir);
      container.load();
      server = new EmbeddedSolrServer(container, coreName);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  protected SolrServer getSolrServer() {
    return server;
  }

  @Override
  protected Logger getLogger() {
    return LOG;
  }
}
