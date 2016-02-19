package stsc.config.feedzilla_downloader;

import java.net.URISyntaxException;

/**
 * You can use this class to database configuration file.
 */
public final class FeedzillaDownloaderConfig {

	public final String getTestConfigFile() throws URISyntaxException {
		return FeedzillaDownloaderConfig.class.getResource("test.properties").toURI().getRawPath();
	}

	public final String getDevelopmentConfigFile() throws URISyntaxException {
		return FeedzillaDownloaderConfig.class.getResource("development.properties").toURI().getRawPath();
	}

	public final String getProductionConfigFile() throws URISyntaxException {
		return FeedzillaDownloaderConfig.class.getResource("production.properties").toURI().getRawPath();
	}

}