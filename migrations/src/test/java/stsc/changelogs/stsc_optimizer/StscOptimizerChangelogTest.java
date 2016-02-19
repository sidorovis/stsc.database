package stsc.changelogs.stsc_optimizer;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public final class StscOptimizerChangelogTest {

	@Test
	public void stscOptimizerChangelogTest() throws URISyntaxException {
		Assert.assertTrue(new StscOptimizerChangelog().getDbChangelog().toFile().exists());
	}

}
