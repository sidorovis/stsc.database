package stsc.config.feedzilla_downloader;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public final class FeedzillaDownloaderConfigTest {

	@Test
	public void getConfigFilesTest() throws URISyntaxException {
		final FeedzillaDownloaderConfig soc = new FeedzillaDownloaderConfig();
		Assert.assertNotNull(soc.getTestConfigFile());
		Assert.assertTrue(new File(soc.getDevelopmentConfigFile()).exists());
		Assert.assertTrue(new File(soc.getProductionConfigFile()).exists());
	}
}