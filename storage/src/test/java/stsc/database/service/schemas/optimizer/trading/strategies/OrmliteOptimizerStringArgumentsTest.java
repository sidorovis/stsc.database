package stsc.database.service.schemas.optimizer.trading.strategies;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import liquibase.exception.LiquibaseException;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.storages.optimizer.OptimizerTradingStrategiesDatabaseStorage;

public class OrmliteOptimizerStringArgumentsTest {

	@Test
	public void testOrmliteOptimizerStringArguments() throws SQLException, LiquibaseException, IOException {
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
			
			final OrmliteOptimizerStringArguments stringArgument = new OrmliteOptimizerStringArguments(executionInstance.getId());
			stringArgument.setParameterName("p_name");
			stringArgument.setParameterValue("p_value");
			
			Assert.assertEquals(1, storage.saveStringArgument(stringArgument).getNumLinesChanged());
		}
		{
			final OrmliteOptimizerTradingStrategy ts = storage.loadTradingStrategy(1);
			final OrmliteOptimizerExecutionInstance executionInstance = storage.loadExecutionInstance(ts).get(0);
			final OrmliteOptimizerStringArguments stringArguments = storage.loadStringArguments(executionInstance).get(0);
			Assert.assertEquals(1, stringArguments.getId().intValue());
			Assert.assertEquals("p_name", stringArguments.getParameterName());
			Assert.assertEquals("p_value", stringArguments.getParameterValue());
			Assert.assertNotNull(stringArguments.getCreatedAt());
			Assert.assertNotNull(stringArguments.getUpdatedAt());
		}
		settings.dropAll();
	}

}
