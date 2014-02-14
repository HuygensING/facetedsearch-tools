package nl.knaw.huygens.solr;

import com.google.common.base.Preconditions;

public class AbstractSolrServerBuilder {

  private final SolrServerType serverType;
  private final int commitWithinSeconds;
  private final SolrQueryCreator queryCreator;

  private String coreName;
  private String solrDir;
  private String solrUrl;

  public AbstractSolrServerBuilder(SolrServerType serverType, int commitWithinSeconds, SolrQueryCreator queryCreator) {
    this.serverType = serverType;
    this.commitWithinSeconds = commitWithinSeconds;
    this.queryCreator = queryCreator;
  }

  public SearchServer build() {
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

    return new LocalSolrServer(solrDir, coreName, commitWithinSeconds, queryCreator);
  }

  private RemoteSolrServer createRemoteSolrServer() {
    Preconditions.checkNotNull(solrUrl);
    return new RemoteSolrServer(solrUrl, commitWithinSeconds, queryCreator);
  }

  public AbstractSolrServerBuilder setCoreName(String coreName) {
    this.coreName = coreName;

    return this;
  }

  public AbstractSolrServerBuilder setSolrDir(String solrDir) {
    this.solrDir = solrDir;

    return this;
  }

  public AbstractSolrServerBuilder setSolrUrl(String solrUrl) {
    this.solrUrl = solrUrl;

    return this;
  }

  public static enum SolrServerType {
    LOCAL, REMOTE
  }
}
