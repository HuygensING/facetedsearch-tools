package nl.knaw.huygens.facetedsearch.model;

public class RangeOption implements FacetCountOption {
	private int lowerLimit = 99999999;
	private int upperLimit = 00000000;

	public int getLowerLimit() {
		return lowerLimit;
	}

	public RangeOption setLowerLimit(int lowerLimit) {
		this.lowerLimit = lowerLimit;
		return this;
	}

	public int getUpperLimit() {
		return upperLimit;
	}

	public RangeOption setUpperLimit(int upperLimit) {
		this.upperLimit = upperLimit;
		return this;
	}

}
