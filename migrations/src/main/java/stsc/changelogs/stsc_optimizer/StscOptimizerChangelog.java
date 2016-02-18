package stsc.changelogs.stsc_optimizer;

import java.net.URISyntaxException;

public final class StscOptimizerChangelog {

	public final String getDbChangelog() throws URISyntaxException {
		return StscOptimizerChangelog.class.getResource("db.changelog.xml").toURI().getRawPath();
	}
	
}
