package nl.knaw.huygens.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteSolrServer extends AbstractSolrServer {
  private static final Logger LOG = LoggerFactory.getLogger(RemoteSolrServer.class);

  private final String solrUrl;
  private SolrServer server;

  public RemoteSolrServer(String solrUrl, int commitWithinInSeconds) {
    super(commitWithinInSeconds);
    this.solrUrl = solrUrl;
    createServer();
  }

  private void createServer() {
    LOG.info("SOLR URL = {}", solrUrl);
    server = new HttpSolrServer(solrUrl);
  }

  public String getUrl() {
    return solrUrl;
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
