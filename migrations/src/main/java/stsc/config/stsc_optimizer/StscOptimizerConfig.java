package stsc.config.stsc_optimizer;

import java.net.URISyntaxException;

/**
 * You can use this class to database configuration file.
 */
public final class StscOptimizerConfig {

	public final String getTestConfigFile() throws URISyntaxException {
		return StscOptimizerConfig.class.getResource("test.properties").toURI().getRawPath();
	}

	public final String getDevelopmentConfigFile() throws URISyntaxException {
		return StscOptimizerConfig.class.getResource("development.properties").toURI().getRawPath();
	}

	public final String getProductionConfigFile() throws URISyntaxException {
		return StscOptimizerConfig.class.getResource("production.properties").toURI().getRawPath();
	}

}