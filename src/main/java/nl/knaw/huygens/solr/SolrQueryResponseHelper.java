package nl.knaw.huygens.solr;

import java.util.List;
import java.util.Map;

import nl.knaw.huygens.facetedsearch.model.FacetCount;
import nl.knaw.huygens.facetedsearch.model.FacetCount.Option;
import nl.knaw.huygens.facetedsearch.model.FacetInfo;
import nl.knaw.huygens.facetedsearch.model.FacetType;
import nl.knaw.huygens.facetedsearch.model.SolrFields;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

import com.google.common.collect.Lists;

public class SolrQueryResponseHelper {
  private final QueryResponse queryResponse;

  public SolrQueryResponseHelper(QueryResponse queryResponse) {
    this.queryResponse = queryResponse;
  }

  /**
   * Get the id's of all the documents of the {@code QueryResponse}.
   * @return a list with the id's an emptyList if none found.
   */
  public List<String> getIds() {
    List<String> ids = Lists.newArrayList();

    for (SolrDocument doc : queryResponse.getResults()) {
      ids.add((String) doc.get(SolrFields.DOC_ID));
    }

    return ids;
  }

  /**
   * Get the {@code FacetCount}s of all the documents of the {@code QueryResponse}.
   * @param facetNames the names of the facets you want the counts from.
   * @param facetInfos a mapping with the meta data of the facets.
   * @return a list of the {@code FacetCount}, an empty list if none are found.
   */
  public List<FacetCount> getFacetCounts(List<String> facetNames, Map<String, FacetInfo> facetInfos) {
    throw new UnsupportedOperationException("Yet to be implemented");
  }

  /**
   * Get the {@code SolrDocument}s from the {@code QueryResponse}. 
   * @return a list of {@code SolrDocument}s
   */
  public List<SolrDocument> getDocuments() {
    return queryResponse.getResults();
  }

  /**
   * Get the number of matches found. This could be a different value then the number of {@code SolrDocument}ss.
   * @return the number of the found documents
   */
  public long getNumFound() {
    return queryResponse.getResults().getNumFound();
  }

  /**
   * Get the score of the best matching {@code SolrDocument}
   * @return a {@code float} with the score
   */
  public float getMaxScore() {
    return queryResponse.getResults().getMaxScore();

  }

  /**
   * The offset of the current search result.
   * @return the offset
   */
  public long getStart() {
    return queryResponse.getResults().getStart();
  }

  /**
   * Returns a list of facetinfo with counts.
   * @param field The FacetField to convert
   * @param title the title of the facet
   * @param type the FacetType of the facet
   */
  public FacetCount convertFacet(FacetField field, String title, FacetType type) {
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
