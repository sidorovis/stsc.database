package stsc.database.service.schemas.optimizer.trading.strategies;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import liquibase.exception.LiquibaseException;
import stsc.common.Settings;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.storages.optimizer.OptimizerTradingStrategiesDatabaseStorage;

public class OrmliteOptimizerDoubleArgumentTest {

	@Test
	public void testOrmliteOptimizerDoubleArguments() throws SQLException, LiquibaseException, IOException, URISyntaxException {
		final OptimizerDatabaseSettings settings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final ConnectionSource source = new JdbcConnectionSource(settings.getJdbcUrl(), settings.getLogin(), settings.getPassword());
		final OptimizerTradingStrategiesDatabaseStorage storage = new OptimizerTradingStrategiesDatabaseStorage(source);
		Assert.assertNotNull(storage);
		{
			final OrmliteOptimizerTradingStrategy tradingStrategy = new OrmliteOptimizerTradingStrategy();
			Assert.assertEquals(1, storage.saveTradingStrategy(tradingStrategy).getNumLinesChanged());

			final OrmliteOptimizerExecutionInstance executionInstance = new OrmliteOptimizerExecutionInstance(tradingStrategy.getId());
			executionInstance.setIndexNumber(2);
			executionInstance.setExecutionInstanceName("execution instance name");
			executionInstance.setAlgorithmName("algo name");
			executionInstance.setAlgorithmType("algo type");
			Assert.assertEquals(1, storage.saveExecutionInstance(executionInstance).getNumLinesChanged());

			final OrmliteOptimizerDoubleArgument doubleArgument = new OrmliteOptimizerDoubleArgument(executionInstance.getId());
			doubleArgument.setParameterName("name_of_p");
			doubleArgument.setParameterValue(5426.653);

			Assert.assertEquals(1, storage.saveDoubleArgument(doubleArgument).getNumLinesChanged());
		}
		{
			final OrmliteOptimizerTradingStrategy ts = storage.loadTradingStrategy(1);
			final OrmliteOptimizerExecutionInstance executionInstance = storage.loadExecutionInstance(ts).get(0);
			final OrmliteOptimizerDoubleArgument doubleArguments = storage.loadDoubleArguments(executionInstance).get(0);
			Assert.assertEquals(1, doubleArguments.getId().intValue());
			Assert.assertEquals("name_of_p", doubleArguments.getParameterName());
			Assert.assertEquals(5426.653, doubleArguments.getParameterValue(), Settings.doubleEpsilon);
			Assert.assertNotNull(doubleArguments.getCreatedAt());
			Assert.assertNotNull(doubleArguments.getUpdatedAt());
		}
		settings.dropAll();
	}

}
