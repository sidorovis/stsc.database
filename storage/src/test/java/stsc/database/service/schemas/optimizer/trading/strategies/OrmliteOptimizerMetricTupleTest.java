package stsc.database.service.schemas.optimizer.trading.strategies;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import liquibase.exception.LiquibaseException;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerExperiment;
import stsc.database.service.storages.optimizer.OptimizerExperimentsDatabaseStorage;
import stsc.database.service.storages.optimizer.OptimizerTradingStrategiesDatabaseStorage;

public class OrmliteOptimizerMetricTupleTest {

	@Test
	public void testOrmliteOptimizerExecutionInstance() throws SQLException, LiquibaseException, IOException, URISyntaxException {
		final OptimizerDatabaseSettings settings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final ConnectionSource source = new JdbcConnectionSource(settings.getJdbcUrl(), settings.getLogin(), settings.getPassword());
		final OptimizerTradingStrategiesDatabaseStorage storage = new OptimizerTradingStrategiesDatabaseStorage(source);
		final OptimizerExperimentsDatabaseStorage experimentsDatabaseStorage = new OptimizerExperimentsDatabaseStorage(source);
		final OrmliteOptimizerExperiment experiment = new OrmliteOptimizerExperiment("optimizer", "description for optimization experiment");
		Assert.assertEquals(1, experimentsDatabaseStorage.saveExperiment(experiment).getNumLinesChanged());
		Assert.assertNotNull(storage);
		{
			final OrmliteOptimizerTradingStrategy tradingStrategy = new OrmliteOptimizerTradingStrategy(experiment.getId());
			Assert.assertEquals(1, storage.saveTradingStrategy(tradingStrategy).getNumLinesChanged());

			final OrmliteOptimizerMetricsTuple metricsTuple = new OrmliteOptimizerMetricsTuple(tradingStrategy.getId());
			Assert.assertEquals(1, storage.saveMetricsTuple(metricsTuple).getNumLinesChanged());
		}
		{
			final OrmliteOptimizerTradingStrategy copy = storage.loadTradingStrategy(1);
			final OrmliteOptimizerMetricsTuple metricsTuple = storage.loadMetricsTuples(copy).get(0);
			Assert.assertNotNull(metricsTuple);
		}
		settings.dropAll();
	}

}
