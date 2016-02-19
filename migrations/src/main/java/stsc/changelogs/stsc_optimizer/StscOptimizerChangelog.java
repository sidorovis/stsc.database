package stsc.changelogs.stsc_optimizer;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class StscOptimizerChangelog {

	public final Path getDbChangelog() throws URISyntaxException {
		return Paths.get(StscOptimizerChangelog.class.getResource("db.changelog.xml").toURI());
	}

}
