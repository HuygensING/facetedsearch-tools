package nl.knaw.huygens.facetedsearch.model;

public class HighlightingOptions {
  private int fragSize = 100;
  private int maxChars = -1;
  private boolean mergeContiguous = false; // Also the solr default.

  public int getFragSize() {
    return fragSize;
  }

  public void setFragSize(int fragSize) {
    this.fragSize = fragSize;
  }

  public int getMaxChars() {
    return maxChars;
  }

  public void setMaxChars(int maxChars) {
    this.maxChars = maxChars;
  }

  public boolean isMergeContiguous() {
    return mergeContiguous;
  }

  public void setMergeContiguous(boolean mergeContiguous) {
    this.mergeContiguous = mergeContiguous;
  }
}
