package nl.knaw.huygens.facetedsearch.serialization;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import nl.knaw.huygens.facetedsearch.model.parameters.DefaultFacetParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.RangeParameter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FacetParameterDeserializer extends JsonDeserializer<FacetParameter> {

  private static final String UPPER_LIMIT_KEY = "upperLimit";
  private static final String LOWER_LIMIT_KEY = "lowerLimit";
  private static final String NAME_KEY = "name";
  private static final String VALUES_KEY = "values";

  @Override
  public FacetParameter deserialize(JsonParser jp, DeserializationContext context) throws IOException, JsonProcessingException {
    JsonNode node = jp.readValueAsTree();

    FacetParameter facetParameter = null;

    if (node.has(VALUES_KEY)) {
      facetParameter = deserializeDefaultFacetParameter(node);
    } else if (node.has(LOWER_LIMIT_KEY) && node.has(UPPER_LIMIT_KEY)) {
      facetParameter = deserializeRangeParameter(node);
    }

    return facetParameter;
  }

  private FacetParameter deserializeRangeParameter(JsonNode node) {
    FacetParameter facetParameter;
    String name = getName(node);
    long lowerLimit = getFieldAsLong(node, LOWER_LIMIT_KEY);
    long upperLimit = getFieldAsLong(node, UPPER_LIMIT_KEY);

    facetParameter = new RangeParameter(name, lowerLimit, upperLimit);
    return facetParameter;
  }

  private long getFieldAsLong(JsonNode node, String fieldName) {
    return node.get(fieldName).asLong();
  }

  private FacetParameter deserializeDefaultFacetParameter(JsonNode node) throws IOException {
    String name = getName(node);
    List<String> values = getValues(node);

    return new DefaultFacetParameter(name, values);
  }

  private List<String> getValues(JsonNode node) throws IOException {
    String[] values = null;

    values = new ObjectMapper().readValue(node.get(VALUES_KEY).traverse(), String[].class);

    return Arrays.asList(values);
  }

  private String getName(JsonNode node) {
    return node.get(NAME_KEY).asText();
  }
}
