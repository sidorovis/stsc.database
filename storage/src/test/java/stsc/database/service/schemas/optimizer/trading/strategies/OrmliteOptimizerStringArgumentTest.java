package stsc.database.service.schemas.optimizer.trading.strategies;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import liquibase.exception.LiquibaseException;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerExecution;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerExperiment;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerStringDomen;
import stsc.database.service.storages.optimizer.OptimizerDatabaseStorage;

public class OrmliteOptimizerStringArgumentTest {

	int createExperimentAndGetStringDomenId(OrmliteOptimizerExperiment experiment, final OptimizerDatabaseStorage storage) throws SQLException {
		final OrmliteOptimizerExecution execution = new OrmliteOptimizerExecution(experiment.getId(), 1);
		execution.setExecutionName("exec1");
		execution.setAlgorithmName("SuperProfitableAlgo");
		execution.setAlgorithmType("STOCK or EOD");
		Assert.assertEquals(1, storage.getExperimentsStorage().saveExecution(execution).getNumLinesChanged());
		final OrmliteOptimizerStringDomen stringParameter = new OrmliteOptimizerStringDomen(execution.getId());
		stringParameter.setParameterName("parameterOfAlgorithmA");
		stringParameter.setParameterDomen("My_Domen|second|value");
		Assert.assertEquals(1, storage.getExperimentsStorage().saveStringParameter(stringParameter).getNumLinesChanged());
		return stringParameter.getId();
	}

	@Test
	public void testOrmliteOptimizerStringArguments() throws SQLException, LiquibaseException, IOException, URISyntaxException {
		final OptimizerDatabaseSettings settings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final OptimizerDatabaseStorage storage = new OptimizerDatabaseStorage(settings);
		Assert.assertNotNull(storage);
		final OrmliteOptimizerExperiment experiment = new OrmliteOptimizerExperiment("optimizer", "description for optimization experiment");
		Assert.assertEquals(1, storage.getExperimentsStorage().saveExperiment(experiment).getNumLinesChanged());
		final int domenId = createExperimentAndGetStringDomenId(experiment, storage);
		{
			final OrmliteOptimizerTradingStrategy tradingStrategy = new OrmliteOptimizerTradingStrategy(experiment.getId());
			Assert.assertEquals(1, storage.getTradingStrategiesStorage().saveTradingStrategy(tradingStrategy).getNumLinesChanged());

			final OrmliteOptimizerExecutionInstance executionInstance = new OrmliteOptimizerExecutionInstance(tradingStrategy.getId());
			executionInstance.setIndexNumber(2);
			executionInstance.setExecutionInstanceName("execution instance name");
			executionInstance.setAlgorithmName("algo name");
			executionInstance.setAlgorithmType("algo type");
			Assert.assertEquals(1, storage.getTradingStrategiesStorage().saveExecutionInstance(executionInstance).getNumLinesChanged());

			final OrmliteOptimizerStringArgument stringArgument = new OrmliteOptimizerStringArgument(executionInstance.getId());
			stringArgument.setStringDomenId(domenId);
			stringArgument.setArgumentIndex(54);

			Assert.assertEquals(1, storage.getTradingStrategiesStorage().saveStringArgument(stringArgument).getNumLinesChanged());
		}
		{
			final OrmliteOptimizerTradingStrategy ts = storage.getTradingStrategiesStorage().loadTradingStrategy(1);
			final OrmliteOptimizerExecutionInstance executionInstance = storage.getTradingStrategiesStorage().loadExecutionInstance(ts).get(0);
			final OrmliteOptimizerStringArgument stringArguments = storage.getTradingStrategiesStorage().loadStringArguments(executionInstance).get(0);
			Assert.assertEquals(1, stringArguments.getId().intValue());
			Assert.assertEquals(domenId, stringArguments.getStringDomenId());
			Assert.assertEquals(54, stringArguments.getArgumentIndex());
			Assert.assertNotNull(stringArguments.getCreatedAt());
			Assert.assertNotNull(stringArguments.getUpdatedAt());
		}
		settings.dropAll();
	}

}
