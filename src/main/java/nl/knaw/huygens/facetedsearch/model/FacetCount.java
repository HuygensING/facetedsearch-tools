package nl.knaw.huygens.facetedsearch.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.collect.Lists;

public class FacetCount {
	private String name = "";
	private String title = "";
	private FacetType type;
	private final List<FacetCountOption> options = Lists.newArrayList();

	public FacetCount setName(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public FacetCount setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public FacetCount setType(FacetType type) {
		this.type = type;
		return this;
	}

	public FacetType getType() {
		return type;
	}

	public FacetCount addOption(FacetCountOption option) {
		options.add(option);
		return this;
	}

	public List<FacetCountOption> getOptions() {
		return options;
	}

	private static final Comparator<FacetCountOption> COUNT_HIGH_TO_LOW = new Comparator<FacetCountOption>() {
		@Override
		public int compare(FacetCountOption o1, FacetCountOption o2) {
			if (o1 instanceof Option && o2 instanceof Option) {
				return (int) (((Option) o2).getCount() - ((Option) o1).getCount());
			}
			return 0;
		}
	};

	public void combineWith(FacetCount other) {
		if (//
		!other.getName().equals(getName()) //
				|| !other.getTitle().equals(getTitle()) //
				|| !other.getType().equals(getType()) //
		) {
			throw new RuntimeException("faceCounts can't be combined, name/title/type doesn't match.");
		}
		List<FacetCountOption> otherOptions = other.getOptions();
		for (FacetCountOption otherfcOption : otherOptions) {
			boolean newOption = true;
			for (FacetCountOption fcoption : options) {
				if (fcoption instanceof Option && otherfcOption instanceof Option) {
					Option option = (Option) fcoption;
					Option otherOption = (Option) otherfcOption;
					if (option.getName().equals(otherOption.getName())) {
						option.setCount(option.getCount() + otherOption.getCount());
						newOption = false;
						break;
					}
				} else if (fcoption instanceof RangeOption && otherfcOption instanceof RangeOption) {
					RangeOption option = (RangeOption) fcoption;
					RangeOption otherOption = (RangeOption) otherfcOption;
					if (otherOption.getLowerLimit() < option.getLowerLimit()) {
						option.setLowerLimit(otherOption.getLowerLimit());
					}
					if (otherOption.getUpperLimit() > option.getUpperLimit()) {
						option.setUpperLimit(otherOption.getUpperLimit());
					}
					newOption = false;

				}
			}
			if (newOption) {
				addOption(otherfcOption);
			}
		}
		Collections.sort(options, COUNT_HIGH_TO_LOW);
	}

	public static class Option implements FacetCountOption {
		private String name = "";
		private long count = 0;

		public Option setName(String name) {
			this.name = name;
			return this;
		}

		public String getName() {
			return name;
		}

		public Option setCount(long l) {
			this.count = l;
			return this;
		}

		public long getCount() {
			return count;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE, false);
		}
	}

	/* ------------------------------------------------------------------------------------------------------------------------------------ */

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE, false);
	}

}
