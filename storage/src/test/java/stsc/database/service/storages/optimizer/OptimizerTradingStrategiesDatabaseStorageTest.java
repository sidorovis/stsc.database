package stsc.database.service.storages.optimizer;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import liquibase.exception.LiquibaseException;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerExecutionInstance;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerStringArgument;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerSubExecutionArgument;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerTradingStrategy;

public class OptimizerTradingStrategiesDatabaseStorageTest {

	private OrmliteOptimizerTradingStrategy addTradingStrategy(final OptimizerTradingStrategiesDatabaseStorage storage) throws SQLException {
		final OrmliteOptimizerTradingStrategy tradingStrategy = new OrmliteOptimizerTradingStrategy();
		Assert.assertEquals(1, storage.saveTradingStrategy(tradingStrategy).getNumLinesChanged());

		final OrmliteOptimizerExecutionInstance execution = new OrmliteOptimizerExecutionInstance(tradingStrategy.getId());
		execution.setAlgorithmName("algo_name");
		execution.setAlgorithmType("algo_type");
		execution.setIndexNumber(15);
		execution.setExecutionInstanceName("execution1");
		Assert.assertEquals(1, storage.saveExecutionInstance(execution).getNumLinesChanged());
		
		final OrmliteOptimizerStringArgument stringArgument = new OrmliteOptimizerStringArgument(execution.getId());
		stringArgument.setParameterName("vvv");
		stringArgument.setParameterValue("vvv");
		Assert.assertEquals(1, storage.saveStringArgument(stringArgument).getNumLinesChanged());

		final OrmliteOptimizerSubExecutionArgument subExecutionArgument = new OrmliteOptimizerSubExecutionArgument(execution.getId());
		subExecutionArgument.setSubExecutionName("sub exec");
		Assert.assertEquals(1, storage.saveSubExecutionArgument(subExecutionArgument).getNumLinesChanged());

		return tradingStrategy;
	}

	@Test
	public void testOptimizerDatabaseStorage() throws SQLException, LiquibaseException, IOException {
		final OptimizerDatabaseSettings databaseSettings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final ConnectionSource source = new JdbcConnectionSource(databaseSettings.getJdbcUrl(), databaseSettings.getLogin(), databaseSettings.getPassword());
		final OptimizerTradingStrategiesDatabaseStorage storage = new OptimizerTradingStrategiesDatabaseStorage(source);
		Assert.assertNotNull(storage);

		addTradingStrategy(storage);

		databaseSettings.dropAll();
	}

}
