package nl.knaw.huygens.facetedsearch.model.parameters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;
import nl.knaw.huygens.facetedsearch.services.SolrUtils;

import java.util.List;

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

  @Override
  public String getQueryValue() {
    List<String> values = this.getValues();

    if (values == null || values.isEmpty()) {
      return "";
    } else if (values.size() == 1) {
      return SolrUtils.escapeFacetValue(values.get(0));
    }

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

}
