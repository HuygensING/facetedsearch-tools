package nl.knaw.huygens.solr;

import java.util.List;

import nl.knaw.huygens.facetedsearch.SolrSearcher;
import nl.knaw.huygens.facetedsearch.model.FacetDefinition;

import com.google.common.base.Preconditions;

public class AbstractSolrServerBuilder {

  private final SolrServerType serverType;
  private final int commitWithinSeconds;
  private final List<FacetDefinition> facetDefinitions;

  private String coreName;
  private String solrDir;
  private String solrUrl;

  public AbstractSolrServerBuilder(SolrServerType serverType, int commitWithinSeconds, List<FacetDefinition> facetDefinitions) {
    this.serverType = serverType;
    this.commitWithinSeconds = commitWithinSeconds;
    this.facetDefinitions = facetDefinitions;
  }

  public SolrSearcher build() {
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

    return new LocalSolrServer(solrDir, coreName, commitWithinSeconds, facetDefinitions);
  }

  private RemoteSolrServer createRemoteSolrServer() {
    Preconditions.checkNotNull(solrUrl);
    return new RemoteSolrServer(solrUrl, commitWithinSeconds, facetDefinitions);
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
