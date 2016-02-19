package stsc.changelogs.yahoo_downloader;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public final class YahooDownloaderChangelogTest {

	@Test
	public void yahooDownloaderChangelogTest() throws URISyntaxException {
		Assert.assertTrue(new YahooDownloaderChangelog().getDbChangelog().toFile().exists());
	}

}
