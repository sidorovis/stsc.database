package stsc.changelogs.feedzilla_downloader;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public final class FeedzillaDownloaderChangelogTest {

	@Test
	public void feedzillaDownloaderChangelogTest() throws URISyntaxException {
		Assert.assertTrue(new FeedzillaDownloaderChangelog().getDbChangelog().toFile().exists());
	}

}
