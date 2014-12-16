package nl.knaw.huygens.facetedsearch;

import nl.knaw.huygens.facetedsearch.definition.SolrSearcher;
import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;
import nl.knaw.huygens.facetedsearch.model.NoSuchFieldInIndexException;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.query.SolrQueryCreator;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

public class FacetedSearchLibrary {

  private final SolrQueryCreator queryCreator;
  private final SolrSearcher solrCore;
  private final SearchResultCreator searchResultBuilder;

  public FacetedSearchLibrary(SolrSearcher solrCore) {

    this(solrCore, new SolrQueryCreator(solrCore.getIndexDescription()), new SearchResultCreator(solrCore.getIndexDescription()));
  }

  public FacetedSearchLibrary(SolrSearcher solrCore, SolrQueryCreator queryCreator, SearchResultCreator searchResultBuilder) {
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
  public <T extends FacetedSearchParameters<T>> FacetedSearchResult search(FacetedSearchParameters<T> searchParameters) throws NoSuchFieldInIndexException, FacetedSearchException {
    searchParameters.validate(solrCore.getIndexDescription());
    SolrQuery query = queryCreator.createSearchQuery(searchParameters);
    QueryResponse queryResponse;
    try {
      queryResponse = solrCore.search(query);
    } catch (SolrServerException e) {
      throw new FacetedSearchException(e.getMessage());
    }
    FacetedSearchResult searchResult = searchResultBuilder.build(queryResponse, searchParameters);

    return searchResult;
  }
}
