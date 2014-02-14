package nl.knaw.huygens.facetedsearch.model;

import java.util.List;

import nl.knaw.huygens.solr.SolrUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;

public class DefaultFacetParameter extends FacetParameter {
  // TODO: make it work with jackson/jersey
  private List<String> values = Lists.newArrayList();

  public DefaultFacetParameter(String name, List<String> values) {
    super(name);
    this.values = values;
  }

  public List<String> getValues() {
    return values;
  }

  public DefaultFacetParameter setValues(List<String> values) {
    this.values = values;
    return this;
  }

  public List<String> getEscapedValues() {
    Builder<String> builder = ImmutableList.builder();
    for (String value : getValues()) {
      builder.add(SolrUtils.escapeFacetValue(value));
    }
    return builder.build();
  }

  private long lowerLimit = -1;
  private long upperLimit = -1;

  public DefaultFacetParameter setLowerLimit(long lowerLimit) {
    this.lowerLimit = lowerLimit;
    return this;
  }

  public long getLowerLimit() {
    return lowerLimit;
  }

  public DefaultFacetParameter setUpperLimit(long upperLimit) {
    this.upperLimit = upperLimit;
    return this;
  }

  public long getUpperLimit() {
    return upperLimit;
  }

  public boolean isRangeFacetParameter() {
    return lowerLimit != -1 && upperLimit != -1;
  }

  @Override
  public String getQueryValue() {
    List<String> values = this.getValues();

    if (values.size() > 1) {
      StringBuilder builder = new StringBuilder();
      builder.append("(");
      String prefix = "";
      for (String value : values) {
        builder.append(prefix).append(SolrUtils.escapeFacetValue(value));
        prefix = " ";
      }
      builder.append(")");
      return builder.toString();
    }
    return SolrUtils.escapeFacetValue(values.get(0));

  }

}
