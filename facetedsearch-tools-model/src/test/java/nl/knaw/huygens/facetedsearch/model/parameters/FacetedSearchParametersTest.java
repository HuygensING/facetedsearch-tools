package nl.knaw.huygens.facetedsearch.model.parameters;

import static nl.knaw.huygens.facetedsearch.model.parameters.FacetFieldNameMatcher.facetFieldWithName;
import static nl.knaw.huygens.facetedsearch.model.parameters.FacetParameterNameMatcher.facetParameterWithName;
import static nl.knaw.huygens.facetedsearch.model.parameters.SortParameterNameMatcher.sortParameterWithName;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import nl.knaw.huygens.facetedsearch.model.NoSuchFieldInIndexException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

public class FacetedSearchParametersTest {
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
  private IndexDescription indexDescriptionMock;

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
    when(indexDescriptionMock.doesFacetFieldExist(any(FacetField.class))).thenReturn(true);
    when(indexDescriptionMock.doesFacetParameterExist(any(FacetParameter.class))).thenReturn(true);
    when(indexDescriptionMock.doesResultFieldExist(anyString())).thenReturn(true);
    when(indexDescriptionMock.doesSortParameterExist(any(SortParameter.class))).thenReturn(true);

    // action
    instance.validate(indexDescriptionMock);

    // verify
    verify(indexDescriptionMock).doesFacetFieldExist(facetFieldWithName(FIRST_FACET_FIELD));
    verify(indexDescriptionMock).doesFacetFieldExist(facetFieldWithName(SECOND_FACET_FIELD));
    verify(indexDescriptionMock).doesFacetParameterExist(facetParameterWithName(FIRST_FACET_PARAM));
    verify(indexDescriptionMock).doesFacetParameterExist(facetParameterWithName(SECOND_FACET_PARAM));
    verify(indexDescriptionMock).doesResultFieldExist(FIRST_RESULT_FIELD);
    verify(indexDescriptionMock).doesResultFieldExist(SECOND_RESULT_FIELD);
    verify(indexDescriptionMock).doesSortParameterExist(sortParameterWithName(FIRST_SORT_PARAM));
    verify(indexDescriptionMock).doesSortParameterExist(sortParameterWithName(SECOND_SORT_PARAM));
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateUnknownFacetField() throws NoSuchFieldInIndexException {

    //when
    when(indexDescriptionMock.doesFacetFieldExist(any(FacetField.class))).thenReturn(true);

    when(indexDescriptionMock.doesFacetFieldExist(facetFieldWithName(SECOND_FACET_FIELD))).thenReturn(false);

    try {
      // action
      instance.validate(indexDescriptionMock);
    } finally {
      // verify
      verify(indexDescriptionMock).doesFacetFieldExist(facetFieldWithName(FIRST_FACET_FIELD));
      verify(indexDescriptionMock).doesFacetFieldExist(facetFieldWithName(SECOND_FACET_FIELD));
      verifyNoMoreInteractions(indexDescriptionMock);
    }
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateFirstFacetFieldUnknown() throws NoSuchFieldInIndexException {

    //when
    when(indexDescriptionMock.doesFacetFieldExist(facetFieldWithName(FIRST_FACET_FIELD))).thenReturn(false);

    try {
      // action
      instance.validate(indexDescriptionMock);
    } finally {
      // verify
      verify(indexDescriptionMock).doesFacetFieldExist(facetFieldWithName(FIRST_FACET_FIELD));
      verifyNoMoreInteractions(indexDescriptionMock);
    }
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateUnknownFacetParameter() throws NoSuchFieldInIndexException {
    //when
    when(indexDescriptionMock.doesFacetFieldExist(any(FacetField.class))).thenReturn(true);
    when(indexDescriptionMock.doesFacetParameterExist(any(FacetParameter.class))).thenReturn(true);

    when(indexDescriptionMock.doesFacetParameterExist(facetParameterWithName(SECOND_FACET_PARAM))).thenReturn(false);

    try {
      // action
      instance.validate(indexDescriptionMock);
    } finally {
      // verify
      verify(indexDescriptionMock).doesFacetFieldExist(facetFieldWithName(FIRST_FACET_FIELD));
      verify(indexDescriptionMock).doesFacetFieldExist(facetFieldWithName(SECOND_FACET_FIELD));
      verify(indexDescriptionMock).doesFacetParameterExist(facetParameterWithName(FIRST_FACET_PARAM));
      verify(indexDescriptionMock).doesFacetParameterExist(facetParameterWithName(SECOND_FACET_PARAM));
      verifyNoMoreInteractions(indexDescriptionMock);
    }
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateUnknownResultField() throws NoSuchFieldInIndexException {
    //when
    when(indexDescriptionMock.doesFacetFieldExist(any(FacetField.class))).thenReturn(true);
    when(indexDescriptionMock.doesFacetParameterExist(any(FacetParameter.class))).thenReturn(true);
    when(indexDescriptionMock.doesResultFieldExist(anyString())).thenReturn(true);

    when(indexDescriptionMock.doesResultFieldExist(SECOND_RESULT_FIELD)).thenReturn(false);

    try {
      // action
      instance.validate(indexDescriptionMock);
    } finally {
      // verify
      verify(indexDescriptionMock).doesFacetFieldExist(facetFieldWithName(FIRST_FACET_FIELD));
      verify(indexDescriptionMock).doesFacetFieldExist(facetFieldWithName(SECOND_FACET_FIELD));
      verify(indexDescriptionMock).doesFacetParameterExist(facetParameterWithName(FIRST_FACET_PARAM));
      verify(indexDescriptionMock).doesFacetParameterExist(facetParameterWithName(SECOND_FACET_PARAM));
      verify(indexDescriptionMock).doesResultFieldExist(FIRST_RESULT_FIELD);
      verify(indexDescriptionMock).doesResultFieldExist(SECOND_RESULT_FIELD);
      verifyNoMoreInteractions(indexDescriptionMock);
    }
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateUnknownSortParameter() throws NoSuchFieldInIndexException {
    //when
    when(indexDescriptionMock.doesFacetFieldExist(any(FacetField.class))).thenReturn(true);
    when(indexDescriptionMock.doesFacetParameterExist(any(FacetParameter.class))).thenReturn(true);
    when(indexDescriptionMock.doesResultFieldExist(anyString())).thenReturn(true);
    when(indexDescriptionMock.doesSortParameterExist(any(SortParameter.class))).thenReturn(true);

    when(indexDescriptionMock.doesSortParameterExist(sortParameterWithName(FIRST_SORT_PARAM))).thenReturn(false);

    try {
      // action
      instance.validate(indexDescriptionMock);
    } finally {
      // verify
      verify(indexDescriptionMock).doesFacetFieldExist(facetFieldWithName(FIRST_FACET_FIELD));
      verify(indexDescriptionMock).doesFacetFieldExist(facetFieldWithName(SECOND_FACET_FIELD));
      verify(indexDescriptionMock).doesFacetParameterExist(facetParameterWithName(FIRST_FACET_PARAM));
      verify(indexDescriptionMock).doesFacetParameterExist(facetParameterWithName(SECOND_FACET_PARAM));
      verify(indexDescriptionMock).doesResultFieldExist(FIRST_RESULT_FIELD);
      verify(indexDescriptionMock).doesResultFieldExist(SECOND_RESULT_FIELD);
      verify(indexDescriptionMock).doesSortParameterExist(sortParameterWithName(FIRST_SORT_PARAM));
      verifyNoMoreInteractions(indexDescriptionMock);
    }
  }

}
