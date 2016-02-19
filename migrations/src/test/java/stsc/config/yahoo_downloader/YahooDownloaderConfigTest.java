package stsc.config.yahoo_downloader;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public final class YahooDownloaderConfigTest {

	// @Test
	// public void getChangeLogFileTest() throws URISyntaxException {
	// Assert.assertEquals(StscOptimizerConfig.class.getResource("db.changelog.xml").toURI().getRawPath(), new StscOptimizerConfig().getChangeLogFile());
	// }

	@Test
	public void getConfigFilesTest() throws URISyntaxException {
		final YahooDownloaderConfig soc = new YahooDownloaderConfig();
		Assert.assertTrue(new File(soc.getTestConfigFile()).exists());
		Assert.assertTrue(new File(soc.getDevelopmentConfigFile()).exists());
		Assert.assertTrue(new File(soc.getProductionConfigFile()).exists());
	}
}