package nl.knaw.huygens.solr;

import com.google.common.base.Preconditions;

public class SolrServerWrapperBuilder {

  private final SolrServerType serverType;
  private final int commitWithinSeconds;

  private String coreName;
  private String solrDir;
  private String solrUrl;

  public SolrServerWrapperBuilder(SolrServerType serverType, int commitWithinSeconds) {
    this.serverType = serverType;
    this.commitWithinSeconds = commitWithinSeconds;
  }

  public SolrServerWrapper build() {
    switch (serverType) {
    case LOCAL:
      return createLocalSolrServer();
    case REMOTE:
      return createRemoteSolrServer();
    default:
      throw new RuntimeException(String.format("Server type %s not supported.", serverType));
    }
  }

  private LocalSolrServer createLocalSolrServer() {
    Preconditions.checkNotNull(coreName);
    Preconditions.checkNotNull(solrDir);

    return new LocalSolrServer(solrDir, coreName, commitWithinSeconds);
  }

  private RemoteSolrServer createRemoteSolrServer() {
    Preconditions.checkNotNull(coreName);
    return new RemoteSolrServer(solrUrl, commitWithinSeconds);
  }

  public SolrServerWrapperBuilder setCoreName(String coreName) {
    this.coreName = coreName;

    return this;
  }

  public SolrServerWrapperBuilder setSolrDir(String solrDir) {
    this.solrDir = solrDir;

    return this;
  }

  public SolrServerWrapperBuilder setSolrUrl(String solrUrl) {
    this.solrUrl = solrUrl;

    return this;
  }

  public static enum SolrServerType {
    LOCAL, REMOTE
  }
}
