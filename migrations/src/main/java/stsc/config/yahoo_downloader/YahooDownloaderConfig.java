package stsc.config.yahoo_downloader;

import java.net.URISyntaxException;

/**
 * You can use this class to database configuration file.
 */
public final class YahooDownloaderConfig {

	public final String getTestConfigFile() throws URISyntaxException {
		return YahooDownloaderConfig.class.getResource("test.properties").toURI().getRawPath();
	}

	public final String getDevelopmentConfigFile() throws URISyntaxException {
		return YahooDownloaderConfig.class.getResource("development.properties").toURI().getRawPath();
	}

	public final String getProductionConfigFile() throws URISyntaxException {
		return YahooDownloaderConfig.class.getResource("production.properties").toURI().getRawPath();
	}

}