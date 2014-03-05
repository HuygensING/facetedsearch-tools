package nl.knaw.huygens.facetedsearch.model;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Map;

import nl.knaw.huygens.facetedsearch.model.parameters.DefaultFacetParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.DefaultFacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetField;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.RangeFacetField;
import nl.knaw.huygens.facetedsearch.model.parameters.RangeParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.SortDirection;
import nl.knaw.huygens.facetedsearch.model.parameters.SortParameter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

public class FacetedSearchParamatersTest {
  private static final String SECOND_SORT_PARAM = "sortParam2";
  private static final String FIRST_SORT_PARAM = "sortParam1";
  private static final String SECOND_FACET_PARAM = "defaultParam";
  private static final String FIRST_FACET_PARAM = "rangeParam";
  private static final String SECOND_RESULT_FIELD = "resultField2";
  private static final String FIRST_RESULT_FIELD = "resultField1";
  private static final String SECOND_FACET_FIELD = "rangeField";
  private static final String FIRST_FACET_FIELD = "facetField";
  private DefaultFacetedSearchParameters instance;
  @Mock
  private Map<String, FacetDefinition> facectDefinitionMapMock;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    instance = new DefaultFacetedSearchParameters();

    //facet fields
    ArrayList<FacetField> facetFields = Lists.newArrayList(//
        new FacetField(FIRST_FACET_FIELD), //
        new RangeFacetField(SECOND_FACET_FIELD, "field"));
    instance.setFacetFields(facetFields);
    //result fields
    ArrayList<String> resultFields = Lists.newArrayList(FIRST_RESULT_FIELD, SECOND_RESULT_FIELD);
    instance.setResultFields(resultFields);
    //facet parameters
    ArrayList<FacetParameter> facetParameters = Lists.newArrayList(//
        new RangeParameter(FIRST_FACET_PARAM, 10, 20),//
        new DefaultFacetParameter(SECOND_FACET_PARAM, Lists.newArrayList("value1")));
    instance.setFacetParameters(facetParameters);
    //sort parameters
    ArrayList<SortParameter> sortParameters = Lists.newArrayList(//
        createSortParameter(FIRST_SORT_PARAM), //
        createSortParameter(SECOND_SORT_PARAM));
    instance.setSortParameters(sortParameters);
  }

  private SortParameter createSortParameter(String name) {
    return new SortParameter(name, SortDirection.ASCENDING);
  }

  @Test
  public void testValidateAllValid() throws NoSuchFieldInIndexException {
    //when
    when(facectDefinitionMapMock.containsKey(anyString())).thenReturn(true);

    // action
    instance.validate(facectDefinitionMapMock);

    // verify
    verify(facectDefinitionMapMock).containsKey(FIRST_FACET_FIELD);
    verify(facectDefinitionMapMock).containsKey(SECOND_FACET_FIELD);
    verify(facectDefinitionMapMock).containsKey(SECOND_FACET_PARAM);
    verify(facectDefinitionMapMock).containsKey(FIRST_FACET_PARAM);
    verify(facectDefinitionMapMock).containsKey(FIRST_RESULT_FIELD);
    verify(facectDefinitionMapMock).containsKey(SECOND_RESULT_FIELD);
    verify(facectDefinitionMapMock).containsKey(FIRST_SORT_PARAM);
    verify(facectDefinitionMapMock).containsKey(SECOND_SORT_PARAM);
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateUnknownFacetField() throws NoSuchFieldInIndexException {

    //when
    when(facectDefinitionMapMock.containsKey(anyString())).thenReturn(true);
    when(facectDefinitionMapMock.containsKey(SECOND_FACET_FIELD)).thenReturn(false);

    try {
      // action
      instance.validate(facectDefinitionMapMock);
    } finally {
      // verify
      verify(facectDefinitionMapMock).containsKey(FIRST_FACET_FIELD);
      verify(facectDefinitionMapMock).containsKey(SECOND_FACET_FIELD);
      verifyNoMoreInteractions(facectDefinitionMapMock);
    }
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateFirstFacetFieldUnknown() throws NoSuchFieldInIndexException {

    //when
    when(facectDefinitionMapMock.containsKey(anyString())).thenReturn(true);
    when(facectDefinitionMapMock.containsKey(FIRST_FACET_FIELD)).thenReturn(false);

    try {
      // action
      instance.validate(facectDefinitionMapMock);
    } finally {
      // verify
      verify(facectDefinitionMapMock).containsKey(FIRST_FACET_FIELD);
      verifyNoMoreInteractions(facectDefinitionMapMock);
    }
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateUnknownFacetParameter() throws NoSuchFieldInIndexException {
    //when
    when(facectDefinitionMapMock.containsKey(anyString())).thenReturn(true);
    when(facectDefinitionMapMock.containsKey(SECOND_FACET_PARAM)).thenReturn(false);

    try {
      // action
      instance.validate(facectDefinitionMapMock);
    } finally {
      // verify
      verify(facectDefinitionMapMock).containsKey(FIRST_FACET_FIELD);
      verify(facectDefinitionMapMock).containsKey(SECOND_FACET_FIELD);
      verify(facectDefinitionMapMock).containsKey(FIRST_FACET_PARAM);
      verify(facectDefinitionMapMock).containsKey(SECOND_FACET_PARAM);
      verifyNoMoreInteractions(facectDefinitionMapMock);
    }
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateUnknownResultField() throws NoSuchFieldInIndexException {
    //when
    when(facectDefinitionMapMock.containsKey(anyString())).thenReturn(true);
    when(facectDefinitionMapMock.containsKey(SECOND_RESULT_FIELD)).thenReturn(false);

    try {
      // action
      instance.validate(facectDefinitionMapMock);
    } finally {
      // verify
      verify(facectDefinitionMapMock).containsKey(FIRST_FACET_FIELD);
      verify(facectDefinitionMapMock).containsKey(SECOND_FACET_FIELD);
      verify(facectDefinitionMapMock).containsKey(FIRST_FACET_PARAM);
      verify(facectDefinitionMapMock).containsKey(SECOND_FACET_PARAM);
      verify(facectDefinitionMapMock).containsKey(FIRST_RESULT_FIELD);
      verify(facectDefinitionMapMock).containsKey(SECOND_RESULT_FIELD);
      verifyNoMoreInteractions(facectDefinitionMapMock);
    }
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateUnknownSortParameter() throws NoSuchFieldInIndexException {
    //when
    when(facectDefinitionMapMock.containsKey(anyString())).thenReturn(true);
    when(facectDefinitionMapMock.containsKey(FIRST_SORT_PARAM)).thenReturn(false);

    try {
      // action
      instance.validate(facectDefinitionMapMock);
    } finally {
      // verify
      verify(facectDefinitionMapMock).containsKey(FIRST_FACET_FIELD);
      verify(facectDefinitionMapMock).containsKey(SECOND_FACET_FIELD);
      verify(facectDefinitionMapMock).containsKey(FIRST_FACET_PARAM);
      verify(facectDefinitionMapMock).containsKey(SECOND_FACET_PARAM);
      verify(facectDefinitionMapMock).containsKey(FIRST_RESULT_FIELD);
      verify(facectDefinitionMapMock).containsKey(SECOND_RESULT_FIELD);
      verify(facectDefinitionMapMock).containsKey(FIRST_SORT_PARAM);
      verifyNoMoreInteractions(facectDefinitionMapMock);
    }
  }

}
