package stsc.changelogs.feedzilla_downloader;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FeedzillaDownloaderChangelog {

	public final Path getDbChangelog() throws URISyntaxException {
		return Paths.get(FeedzillaDownloaderChangelog.class.getResource("db.changelog.xml").toURI());
	}

}
