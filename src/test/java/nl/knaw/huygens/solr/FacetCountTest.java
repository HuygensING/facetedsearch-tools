package nl.knaw.huygens.solr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import nl.knaw.huygens.LoggableObject;
import nl.knaw.huygens.solr.FacetCount.Option;

import org.junit.Test;

public class FacetCountTest extends LoggableObject {

	@Test
	public void testCombineWith() throws Exception {
		// setup
		FacetCount one = new FacetCount().setName("name").setTitle("title").setType(FacetType.LIST);
		FacetCount two = new FacetCount().setName("name").setTitle("title").setType(FacetType.LIST);
		Option option1 = new Option().setName("name1").setCount(1l);
		Option option2 = new Option().setName("name2").setCount(2l);
		one.addOption(option1).addOption(option2);
		Option option3 = new Option().setName("name2").setCount(2l);
		Option option4 = new Option().setName("name3").setCount(3l);
		two.addOption(option3).addOption(option4);

		// method to test
		one.combineWith(two);
		assertThat(one.getName()).isEqualTo("name");
		assertThat(one.getTitle()).isEqualTo("title");
		assertThat(one.getType()).isEqualTo(FacetType.LIST);

		List<FacetCountOption> options = one.getOptions();
		LOG.info("options={}", options);
		assertThat(options).hasSize(3);

		Option option0 = (Option) options.get(0);
		assertThat(option0.getName()).isEqualTo("name2");
		assertThat(option0.getCount()).isEqualTo(4l);

		Option options1 = (Option) options.get(1);
		assertThat(options1.getName()).isEqualTo("name3");
		assertThat(options1.getCount()).isEqualTo(3l);

		Option options2 = (Option) options.get(2);
		assertThat(options2.getName()).isEqualTo("name1");
		assertThat(options2.getCount()).isEqualTo(1l);
	}

	@Test(expected = RuntimeException.class)
	public void testCombineWithIncompatibleOther() throws Exception {
		// setup
		FacetCount one = new FacetCount().setName("name").setTitle("title").setType(FacetType.LIST);
		FacetCount two = new FacetCount().setName("othername").setTitle("othertitle").setType(FacetType.LIST);
		Option option1 = new Option().setName("name1").setCount(1l);
		Option option2 = new Option().setName("name2").setCount(1l);
		one.addOption(option1).addOption(option2);
		Option option3 = new Option().setName("name2").setCount(1l);
		Option option4 = new Option().setName("name3").setCount(1l);
		two.addOption(option3).addOption(option4);

		// method to test
		one.combineWith(two);
		fail("RuntimeException should've been thrown");
	}
}
