package nl.knaw.huygens.facetedsearch.serialization;

import static nl.knaw.huygens.facetedsearch.serialization.FacetParameterMatcher.isDefaultFacetParameter;
import static nl.knaw.huygens.facetedsearch.serialization.FacetParameterMatcher.isRangeParameter;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

public class FacetParameterDeserializerTest {

  private static final String FACET_NAME = "facet_name";
  private DeserializationContext nullContext = null;
  private JsonFactory factory;
  private FacetParameterDeserializer instance;

  @Before
  public void setUp() {
    factory = new ObjectMapper().getFactory();
    instance = new FacetParameterDeserializer();
  }

  @Test
  public void testDeserializeDefaultFacetParameter() throws JsonParseException, IOException {
    ArrayList<String> values = Lists.newArrayList("Serbia", "Russia");

    String content = createDefaultFacetParameterContent(FACET_NAME, values);
    JsonParser parser = factory.createParser(content);

    FacetParameter facetParameter = instance.deserialize(parser, nullContext);
    assertThat(facetParameter, isDefaultFacetParameter(FACET_NAME, values));
  }

  private String createDefaultFacetParameterContent(String name, ArrayList<String> values) {
    return String.format("{\"name\":\"%s\",\"values\":%s}", name, toJsonCollection(values));
  }

  private String toJsonCollection(Collection<String> values) {
    StringBuilder builder = new StringBuilder("[");
    boolean isFirst = true;
    for (String value : values) {
      if (!isFirst) {
        builder.append(", ");
      }

      builder.append(String.format("\"%s\"", value));
      isFirst = false;
    }
    builder.append("]");

    return builder.toString();
  }

  @Test
  public void testDeserializeRangeFacetParameter() throws JsonProcessingException, IOException {
    long lowerLimit = 19840501l;
    long upperLimit = 20001201l;

    String content = createRangeParameterContent(FACET_NAME, lowerLimit, upperLimit);
    JsonParser jp = factory.createParser(content);

    FacetParameter facetParameter = instance.deserialize(jp, nullContext);

    assertThat(facetParameter, isRangeParameter(FACET_NAME, lowerLimit, upperLimit));
  }

  private String createRangeParameterContent(String name, long lowerLimit, long upperLimit) {
    return String.format("{\"name\":\"%s\",\"lowerLimit\":\"%d\",\"upperLimit\":\"%d\"}", name, lowerLimit, upperLimit);
  }
}
