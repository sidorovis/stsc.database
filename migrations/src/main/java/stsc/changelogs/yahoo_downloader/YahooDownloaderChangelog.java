package stsc.changelogs.yahoo_downloader;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class YahooDownloaderChangelog {

	public final Path getDbChangelog() throws URISyntaxException {
		return Paths.get(YahooDownloaderChangelog.class.getResource("db.changelog.xml").toURI());
	}

}
