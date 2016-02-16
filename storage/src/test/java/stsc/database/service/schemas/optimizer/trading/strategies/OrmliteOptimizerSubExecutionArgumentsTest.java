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

public class OrmliteOptimizerSubExecutionArgumentsTest {

	@Test
	public void testOrmliteOptimizerSubExecutionArguments() throws SQLException, LiquibaseException, IOException {
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
			
			final OrmliteOptimizerSubExecutionArgument argument = new OrmliteOptimizerSubExecutionArgument(executionInstance.getId());
			argument.setSubExecutionName("se_name");
			
			Assert.assertEquals(1, storage.saveSubExecutionArgument(argument).getNumLinesChanged());
		}
		{
			final OrmliteOptimizerTradingStrategy ts = storage.loadTradingStrategy(1);
			final OrmliteOptimizerExecutionInstance executionInstance = storage.loadExecutionInstance(ts).get(0);
			final OrmliteOptimizerSubExecutionArgument arguments = storage.loadSubExecutionArguments(executionInstance).get(0);
			Assert.assertEquals(1, arguments.getId().intValue());
			Assert.assertEquals("se_name", arguments.getSubExecutionName());
			Assert.assertNotNull(arguments.getCreatedAt());
			Assert.assertNotNull(arguments.getUpdatedAt());
		}
		settings.dropAll();
	}

}
