package nl.knaw.huygens.facetedsearch.model;

import java.util.List;

import nl.knaw.huygens.solr.SolrUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;

public class FacetParameter {
	// TODO: split up into FacetTypeParameters, and make it work with jackon/jersey
	String name = "";

	public String getName() {
		return name;
	}

	public FacetParameter setName(String name) {
		this.name = name;
		return this;
	}

	List<String> values = Lists.newArrayList();

	public List<String> getValues() {
		return values;
	}

	public FacetParameter setValues(List<String> values) {
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

	public FacetParameter setLowerLimit(long lowerLimit) {
		this.lowerLimit = lowerLimit;
		return this;
	}

	public long getLowerLimit() {
		return lowerLimit;
	}

	public FacetParameter setUpperLimit(long upperLimit) {
		this.upperLimit = upperLimit;
		return this;
	}

	public long getUpperLimit() {
		return upperLimit;
	}

	public boolean isRangeFacetParameter() {
		return lowerLimit != -1 && upperLimit != -1;
	}

}
