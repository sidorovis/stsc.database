package stsc.config.stsc_optimizer;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

import stsc.config.stsc_optimizer.StscOptimizerConfig;

public final class StscOptimizerConfigTest {

	@Test
	public void getConfigFilesTest() throws URISyntaxException {
		final StscOptimizerConfig soc = new StscOptimizerConfig();
		Assert.assertNotNull(soc.getTestConfigFile());
		Assert.assertTrue(new File(soc.getDevelopmentConfigFile()).exists());
		Assert.assertTrue(new File(soc.getProductionConfigFile()).exists());
	}
}