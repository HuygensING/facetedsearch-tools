package nl.knaw.huygens.solr;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.NoSuchFieldInIndexException;
import nl.knaw.huygens.facetedsearch.model.WrongFacetValueException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

public class FacetedSearchLibrary {

  private final SolrQueryCreator queryCreator;
  private final SolrCoreWrapper solrCore;
  private SearchResultBuilder searchResultBuilder;

  public FacetedSearchLibrary(SolrCoreWrapper solrCore) {

    this(solrCore, new SolrQueryCreator());
  }

  public FacetedSearchLibrary(SolrCoreWrapper solrCore, SolrQueryCreator queryCreator) {
    this.solrCore = solrCore;
    this.queryCreator = queryCreator;
    this.searchResultBuilder = new SearchResultBuilder();
  }

  public <T extends FacetedSearchParameters<T>> SearchResult search(FacetedSearchParameters<T> searchParameters) throws NoSuchFieldInIndexException, WrongFacetValueException {
    SolrQuery query = queryCreator.createSearchQuery(searchParameters, null);
    QueryResponse queryResponse = solrCore.query(query);
    SearchResult searchResult = searchResultBuilder.build(queryResponse);

    return searchResult;
  }

  protected void setSearchResultBuilder(SearchResultBuilder searchResultBuilder) {
    this.searchResultBuilder = searchResultBuilder;
  }

}
