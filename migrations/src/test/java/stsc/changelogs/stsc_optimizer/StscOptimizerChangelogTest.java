package stsc.changelogs.stsc_optimizer;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public final class StscOptimizerChangelogTest {

	@Test
	public void stscOptimizerChangelogTest() throws URISyntaxException {
		Assert.assertTrue(new File(new StscOptimizerChangelog().getDbChangelog()).exists());
	}

}
