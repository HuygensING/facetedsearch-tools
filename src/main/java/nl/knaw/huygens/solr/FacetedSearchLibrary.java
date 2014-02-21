package nl.knaw.huygens.solr;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;
import nl.knaw.huygens.facetedsearch.model.NoSuchFieldInIndexException;
import nl.knaw.huygens.facetedsearch.model.WrongFacetValueException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

public class FacetedSearchLibrary {

  private final SolrQueryCreator queryCreator;
  private final SolrCoreWrapper solrCore;
  private SearchResultBuilder searchResultBuilder;

  public FacetedSearchLibrary(SolrCoreWrapper solrCore) {

    this(solrCore, new SolrQueryCreator(), null);
  }

  public FacetedSearchLibrary(SolrCoreWrapper solrCore, SolrQueryCreator queryCreator, SearchResultBuilder searchResultBuilder) {
    this.solrCore = solrCore;
    this.queryCreator = queryCreator;
    this.searchResultBuilder = searchResultBuilder;
  }

  /**
   * Search the index.
   * @param searchParameters that should be search.
   * @return the result of the query, that is executed on the core.
   * @throws WrongFacetValueException when the {@code searchParameters} contain a facet with a wrong value.
   * @throws NoSuchFieldInIndexException when the {@code searchParameters} contain a field or a facet that is not recognized.
   * @throws FacetedSearchException when the search fails to execute.
   */
  public <T extends FacetedSearchParameters<T>> FacetedSearchResult search(FacetedSearchParameters<T> searchParameters) throws NoSuchFieldInIndexException, WrongFacetValueException,
      FacetedSearchException {
    SolrQuery query = queryCreator.createSearchQuery(searchParameters, null);
    QueryResponse queryResponse = solrCore.search(query);
    FacetedSearchResult searchResult = searchResultBuilder.build(queryResponse);

    return searchResult;
  }

  protected void setSearchResultBuilder(SearchResultBuilder searchResultBuilder) {
    this.searchResultBuilder = searchResultBuilder;
  }

}
