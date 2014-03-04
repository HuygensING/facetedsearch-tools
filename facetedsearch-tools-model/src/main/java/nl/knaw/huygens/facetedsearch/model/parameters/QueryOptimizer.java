package nl.knaw.huygens.facetedsearch.model.parameters;

/**
 * A class that contains information to tweak a solr query.
 */
public class QueryOptimizer {
  private int rows = 50000;
  private int facetLimit = 10000;
  private int facetMinCount = 1;

  public int getRows() {
    return rows;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  public int getFacetLimit() {
    return facetLimit;
  }

  public void setFacetLimit(int facetLimit) {
    this.facetLimit = facetLimit;
  }

  public int getFacetMinCount() {
    return facetMinCount;
  }

  public void setFacetMinCount(int facetMinCount) {
    this.facetMinCount = facetMinCount;
  }
}
