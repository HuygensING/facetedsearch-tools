package nl.knaw.huygens.facetedsearch.model;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import nl.knaw.huygens.facetedsearch.definition.FacetDefinition;
import nl.knaw.huygens.facetedsearch.model.parameters.DefaultFacetParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.DefaultFacetedSearchParameters;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetField;
import nl.knaw.huygens.facetedsearch.model.parameters.FacetParameter;
import nl.knaw.huygens.facetedsearch.model.parameters.SortDirection;
import nl.knaw.huygens.facetedsearch.model.parameters.SortParameter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

public class FacetedSearchParamatersTest {
  private DefaultFacetedSearchParameters searchParameters;
  @Mock
  private Map<String, FacetDefinition> facectDefinitionMapMock;
  private FacetDefinition facetDefinitionMock;
  private String facetName = "testFacet";

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    facetDefinitionMock = mock(FacetDefinition.class);
    searchParameters = new DefaultFacetedSearchParameters();
    searchParameters.setFacetDefinitionMap(facectDefinitionMapMock);
  }

  @Test
  public void testValidateFacetParameter() throws NoSuchFieldInIndexException, WrongFacetValueException {
    DefaultFacetParameter facetParameter = new DefaultFacetParameter(facetName, Lists.newArrayList("1", "2"));
    List<FacetParameter> facetParamaters = Lists.newArrayList();
    facetParamaters.add(facetParameter);

    searchParameters.setFacetParameters(facetParamaters);
    // when
    when(facectDefinitionMapMock.get(facetName)).thenReturn(facetDefinitionMock);
    when(facetDefinitionMock.isValidFacetParameter(facetParameter)).thenReturn(true);

    // action
    searchParameters.validate();

    // verify
    InOrder inOrder = Mockito.inOrder(facectDefinitionMapMock, facetDefinitionMock);
    inOrder.verify(facectDefinitionMapMock).get(facetName);
    inOrder.verify(facetDefinitionMock).isValidFacetParameter(facetParameter);

  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateFacetParameterDoesNotExist() throws NoSuchFieldInIndexException, WrongFacetValueException {
    DefaultFacetParameter facetParameter = new DefaultFacetParameter(facetName, Lists.newArrayList("1", "2"));
    List<FacetParameter> facetParamaters = Lists.newArrayList();
    facetParamaters.add(facetParameter);

    searchParameters.setFacetParameters(facetParamaters);
    // when
    when(facectDefinitionMapMock.get(facetName)).thenReturn(null);

    // action
    searchParameters.validate();

    // verify
    Mockito.inOrder(facectDefinitionMapMock, facetDefinitionMock);
    verify(facectDefinitionMapMock).get(facetName);
    verify(facetDefinitionMock, never()).isValidFacetParameter(facetParameter);
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateFacetParameterIsInvalid() throws NoSuchFieldInIndexException, WrongFacetValueException {
    DefaultFacetParameter facetParameter = new DefaultFacetParameter(facetName, Lists.newArrayList("1", "2"));
    List<FacetParameter> facetParamaters = Lists.newArrayList();
    facetParamaters.add(facetParameter);
    searchParameters.setFacetParameters(facetParamaters);

    // when
    when(facectDefinitionMapMock.get(facetName)).thenReturn(facetDefinitionMock);
    when(facetDefinitionMock.isValidFacetParameter(facetParameter)).thenReturn(false);

    // action
    searchParameters.validate();

    // verify
    InOrder inOrder = Mockito.inOrder(facectDefinitionMapMock, facetDefinitionMock);
    inOrder.verify(facectDefinitionMapMock).get(facetName);
    inOrder.verify(facetDefinitionMock).isValidFacetParameter(facetParameter);
  }

  @Test
  public void testValidateOneFacetFields() throws NoSuchFieldInIndexException, WrongFacetValueException {
    FacetField facetField = new FacetField(facetName);
    List<FacetField> facetFields = Lists.newArrayList(facetField);
    searchParameters.setFacetFields(facetFields);

    // when
    when(facectDefinitionMapMock.get(facetName)).thenReturn(facetDefinitionMock);
    when(facetDefinitionMock.isValidFacetField(facetField)).thenReturn(true);

    // action
    searchParameters.validate();

    // verify
    InOrder inOrder = Mockito.inOrder(facectDefinitionMapMock, facetDefinitionMock);
    inOrder.verify(facectDefinitionMapMock).get(facetName);
    inOrder.verify(facetDefinitionMock).isValidFacetField(facetField);
  }

  @Test
  public void testValidateMultipleFacetsFields() throws NoSuchFieldInIndexException, WrongFacetValueException {
    String facetName1 = "testFacet";
    FacetField facetField1 = new FacetField(facetName1);
    String facetName2 = "facet2";
    FacetField facetField2 = new FacetField(facetName2);
    List<FacetField> facetFields = Lists.newArrayList(facetField1, facetField2);
    searchParameters.setFacetFields(facetFields);

    // when
    when(facectDefinitionMapMock.get(anyString())).thenReturn(facetDefinitionMock);
    when(facetDefinitionMock.isValidFacetField(any(FacetField.class))).thenReturn(true);

    // action
    searchParameters.validate();

    // verify
    InOrder inOrder = Mockito.inOrder(facectDefinitionMapMock, facetDefinitionMock);
    inOrder.verify(facectDefinitionMapMock).get(facetName1);
    inOrder.verify(facetDefinitionMock).isValidFacetField(facetField1);
    inOrder.verify(facectDefinitionMapMock).get(facetName2);
    inOrder.verify(facetDefinitionMock).isValidFacetField(facetField2);
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateFacetDoesNotExist() throws NoSuchFieldInIndexException, WrongFacetValueException {

    String facetName = "testFacet";
    FacetField facetField = new FacetField(facetName);
    List<FacetField> facetFields = Lists.newArrayList(facetField);
    searchParameters.setFacetFields(facetFields);

    // when
    when(facectDefinitionMapMock.get(facetName)).thenReturn(null);

    try {
      // action
      searchParameters.validate();
    } finally {
      // verify
      verify(facectDefinitionMapMock).get(facetName);
      verify(facetDefinitionMock, never()).isValidFacetField(facetField);
    }
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateFacetFieldIsInvalid() throws NoSuchFieldInIndexException, WrongFacetValueException {
    String facetName = "testFacet";
    FacetField facetField = new FacetField(facetName);
    List<FacetField> facetFields = Lists.newArrayList(facetField);
    searchParameters.setFacetFields(facetFields);

    // when
    when(facectDefinitionMapMock.get(facetName)).thenReturn(facetDefinitionMock);
    when(facetDefinitionMock.isValidFacetField(facetField)).thenReturn(false);

    try {
      // action
      searchParameters.validate();
    } finally {
      // verify
      InOrder inOrder = Mockito.inOrder(facectDefinitionMapMock, facetDefinitionMock);
      inOrder.verify(facectDefinitionMapMock).get(facetName);
      inOrder.verify(facetDefinitionMock).isValidFacetField(facetField);
    }
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateResultFieldDoesNotExist() throws NoSuchFieldInIndexException, WrongFacetValueException {
    searchParameters.setResultFields(Lists.newArrayList(facetName));

    // when
    when(facectDefinitionMapMock.get(facetName)).thenReturn(null);

    try {
      // action
      searchParameters.validate();
    } finally {
      // verify
      verify(facectDefinitionMapMock).get(facetName);
      verifyNoMoreInteractions(facetDefinitionMock);
    }
  }

  @Test(expected = NoSuchFieldInIndexException.class)
  public void testValidateSortParameterDoesNotExist() throws NoSuchFieldInIndexException, WrongFacetValueException {
    SortParameter sortParameter = new SortParameter().setFieldname(facetName).setDirection(SortDirection.ASCENDING);
    searchParameters.setSortParameters(Lists.newArrayList(sortParameter));

    // when
    when(facectDefinitionMapMock.get(facetName)).thenReturn(null);

    try {
      // action
      searchParameters.validate();
    } finally {
      // verify
      verify(facectDefinitionMapMock).get(facetName);
      verifyNoMoreInteractions(facetDefinitionMock);
    }
  }
}
