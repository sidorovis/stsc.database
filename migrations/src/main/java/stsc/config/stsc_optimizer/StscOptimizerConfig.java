package stsc.config.stsc_optimizer;

import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * You can use this class to database configuration file.
 */
public final class StscOptimizerConfig {

	public final InputStream getTestConfigFile() throws URISyntaxException {
		return StscOptimizerConfig.class.getResourceAsStream("test.properties");
	}

	public final String getDevelopmentConfigFile() throws URISyntaxException {
		return StscOptimizerConfig.class.getResource("development.properties").toURI().getRawPath();
	}

	public final String getProductionConfigFile() throws URISyntaxException {
		return StscOptimizerConfig.class.getResource("production.properties").toURI().getRawPath();
	}

}