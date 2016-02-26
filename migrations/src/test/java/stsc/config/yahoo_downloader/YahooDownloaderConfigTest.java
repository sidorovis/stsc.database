package stsc.config.yahoo_downloader;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public final class YahooDownloaderConfigTest {

	@Test
	public void getConfigFilesTest() throws URISyntaxException {
		final YahooDownloaderConfig soc = new YahooDownloaderConfig();
		Assert.assertNotNull(soc.getTestConfigFile());
		Assert.assertTrue(new File(soc.getDevelopmentConfigFile()).exists());
		Assert.assertTrue(new File(soc.getProductionConfigFile()).exists());
	}
}