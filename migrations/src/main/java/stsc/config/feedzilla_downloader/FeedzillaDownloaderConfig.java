package stsc.config.feedzilla_downloader;

import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * You can use this class to database configuration file.
 */
public final class FeedzillaDownloaderConfig {

	public final InputStream getTestConfigFile() throws URISyntaxException {
		return FeedzillaDownloaderConfig.class.getResourceAsStream("test.properties");
	}

	public final String getDevelopmentConfigFile() throws URISyntaxException {
		return FeedzillaDownloaderConfig.class.getResource("development.properties").toURI().getRawPath();
	}

	public final String getProductionConfigFile() throws URISyntaxException {
		return FeedzillaDownloaderConfig.class.getResource("production.properties").toURI().getRawPath();
	}

}