package nl.knaw.huygens.solr;

import static org.assertj.core.api.Assertions.assertThat;
import nl.knaw.huygens.LoggableObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FacetedSearchParametersTest<T> extends LoggableObject {

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testSetTerm() throws Exception {
		@SuppressWarnings("rawtypes")
		FacetedSearchParameters<?> facetedSearchParameters = new FacetedSearchParameters()//
				.setCaseSensitive(true)//
				.setFacetFields(new String[] {});
		assertThat(facetedSearchParameters.isCaseSensitive()).isTrue();
	}

}
