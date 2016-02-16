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

public class OrmliteOptimizerIntegerArgumentTest {

	@Test
	public void testOrmliteOptimizerIntegerArguments() throws SQLException, LiquibaseException, IOException {
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
			
			final OrmliteOptimizerIntegerArgument integerArgument = new OrmliteOptimizerIntegerArgument(executionInstance.getId());
			integerArgument.setParameterName("name_of_p");
			integerArgument.setParameterValue(5426);
			
			Assert.assertEquals(1, storage.saveIntegerArgument(integerArgument).getNumLinesChanged());
		}
		{
			final OrmliteOptimizerTradingStrategy ts = storage.loadTradingStrategy(1);
			final OrmliteOptimizerExecutionInstance executionInstance = storage.loadExecutionInstance(ts).get(0);
			final OrmliteOptimizerIntegerArgument integerArguments = storage.loadIntegerArguments(executionInstance).get(0);
			Assert.assertEquals(1, integerArguments.getId().intValue());
			Assert.assertEquals("name_of_p", integerArguments.getParameterName());
			Assert.assertEquals(5426, integerArguments.getParameterValue());
			Assert.assertNotNull(integerArguments.getCreatedAt());
			Assert.assertNotNull(integerArguments.getUpdatedAt());
		}
		settings.dropAll();
	}

}
