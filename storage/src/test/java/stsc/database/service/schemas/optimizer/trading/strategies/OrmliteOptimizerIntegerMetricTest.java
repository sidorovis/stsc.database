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
import stsc.database.service.storages.optimizer.OptimizerTradingStrategiesDatabaseStorage;

public class OrmliteOptimizerIntegerMetricTest {

	@Test
	public void testOrmliteOptimizerIntegerMetric() throws SQLException, LiquibaseException, IOException, URISyntaxException {
		final OptimizerDatabaseSettings settings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final ConnectionSource source = new JdbcConnectionSource(settings.getJdbcUrl(), settings.getLogin(), settings.getPassword());
		final OptimizerTradingStrategiesDatabaseStorage storage = new OptimizerTradingStrategiesDatabaseStorage(source);
		Assert.assertNotNull(storage);
		{
			final OrmliteOptimizerTradingStrategy tradingStrategy = new OrmliteOptimizerTradingStrategy(0);
			Assert.assertEquals(1, storage.saveTradingStrategy(tradingStrategy).getNumLinesChanged());

			final OrmliteOptimizerMetricsTuple metricsTuple = new OrmliteOptimizerMetricsTuple(tradingStrategy.getId());
			Assert.assertEquals(1, storage.saveMetricsTuple(metricsTuple).getNumLinesChanged());

			final OrmliteOptimizerIntegerMetric integerMetric = new OrmliteOptimizerIntegerMetric(metricsTuple.getId());
			integerMetric.setMetricType("Metric Type");
			integerMetric.setMetricValue(34);
			Assert.assertEquals(1, storage.saveIntegerMetric(integerMetric).getNumLinesChanged());
}
		{
			final OrmliteOptimizerTradingStrategy copy = storage.loadTradingStrategy(1);
			final OrmliteOptimizerMetricsTuple metricsTuple = storage.loadMetricsTuples(copy).get(0);
			Assert.assertNotNull(metricsTuple);
			final OrmliteOptimizerIntegerMetric integerMetric = storage.loadIntegerMetrics(metricsTuple).get(0);
			Assert.assertNotNull(integerMetric.getCreatedAt());
			Assert.assertNotNull(integerMetric.getUpdatedAt());
			Assert.assertEquals("Metric Type", integerMetric.getMetricType());
			Assert.assertEquals(34, integerMetric.getMetricValue());
		}
		settings.dropAll();
	}

}
