package nl.knaw.huygens.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

public class RemoteSolrServer extends AbstractSolrServer {
  private final String solrUrl;
  private SolrServer server;

  public RemoteSolrServer(String solrUrl) {
    super();
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

}
