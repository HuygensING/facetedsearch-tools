package nl.knaw.huygens.solr;

import java.util.List;

import nl.knaw.huygens.facetedsearch.model.FacetCount;
import nl.knaw.huygens.facetedsearch.model.FacetCount.Option;
import nl.knaw.huygens.facetedsearch.model.FacetType;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;

public class SolrQueryResponseHelper {
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
