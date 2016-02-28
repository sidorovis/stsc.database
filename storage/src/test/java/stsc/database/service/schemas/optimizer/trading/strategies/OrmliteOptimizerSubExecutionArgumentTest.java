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
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerSubExecutionDomen;
import stsc.database.service.storages.optimizer.OptimizerDatabaseStorage;

public class OrmliteOptimizerSubExecutionArgumentTest {

	int createExperimentAndGetSubExecutionDomenId(OrmliteOptimizerExperiment experiment, final OptimizerDatabaseStorage storage) throws SQLException {
		final OrmliteOptimizerExecution execution = new OrmliteOptimizerExecution(experiment.getId(), 1);
		execution.setExecutionName("exec1");
		execution.setAlgorithmName("SuperProfitableAlgo");
		execution.setAlgorithmType("STOCK or EOD");
		Assert.assertEquals(1, storage.getExperimentsStorage().saveExecution(execution).getNumLinesChanged());
		final OrmliteOptimizerSubExecutionDomen subExecutionParameter = new OrmliteOptimizerSubExecutionDomen(execution.getId());
		subExecutionParameter.setParameterName("parameterOfAlgorithmA");
		subExecutionParameter.setParameterDomen("My_Domen|second|value");
		Assert.assertEquals(1, storage.getExperimentsStorage().saveSubExecutionParameter(subExecutionParameter).getNumLinesChanged());
		return subExecutionParameter.getId();
	}

	@Test
	public void testOrmliteOptimizerSubExecutionArguments() throws SQLException, LiquibaseException, IOException, URISyntaxException {
		final OptimizerDatabaseSettings settings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final OptimizerDatabaseStorage storage = new OptimizerDatabaseStorage(settings);
		Assert.assertNotNull(storage);
		final OrmliteOptimizerExperiment experiment = new OrmliteOptimizerExperiment("optimizer", "description for optimization experiment");
		Assert.assertEquals(1, storage.getExperimentsStorage().saveExperiment(experiment).getNumLinesChanged());
		final int domenId = createExperimentAndGetSubExecutionDomenId(experiment, storage);
		{
			final OrmliteOptimizerTradingStrategy tradingStrategy = new OrmliteOptimizerTradingStrategy(experiment.getId());
			Assert.assertEquals(1, storage.getTradingStrategiesStorage().saveTradingStrategy(tradingStrategy).getNumLinesChanged());

			final OrmliteOptimizerExecutionInstance executionInstance = new OrmliteOptimizerExecutionInstance(tradingStrategy.getId());
			executionInstance.setIndexNumber(2);
			executionInstance.setExecutionInstanceName("execution instance name");
			executionInstance.setAlgorithmName("algo name");
			executionInstance.setAlgorithmType("algo type");
			Assert.assertEquals(1, storage.getTradingStrategiesStorage().saveExecutionInstance(executionInstance).getNumLinesChanged());

			final OrmliteOptimizerSubExecutionArgument argument = new OrmliteOptimizerSubExecutionArgument(executionInstance.getId());
			argument.setSubExecutionDomenId(domenId);
			argument.setArgumentIndex(35);

			Assert.assertEquals(1, storage.getTradingStrategiesStorage().saveSubExecutionArgument(argument).getNumLinesChanged());
		}
		{
			final OrmliteOptimizerTradingStrategy ts = storage.getTradingStrategiesStorage().loadTradingStrategy(1);
			final OrmliteOptimizerExecutionInstance executionInstance = storage.getTradingStrategiesStorage().loadExecutionInstance(ts).get(0);
			final OrmliteOptimizerSubExecutionArgument arguments = storage.getTradingStrategiesStorage().loadSubExecutionArguments(executionInstance).get(0);
			Assert.assertEquals(1, arguments.getId().intValue());
			Assert.assertEquals(domenId, arguments.getSubExecutionDomenId());
			Assert.assertEquals(35, arguments.getArgumentIndex());
			Assert.assertNotNull(arguments.getCreatedAt());
			Assert.assertNotNull(arguments.getUpdatedAt());
		}
		settings.dropAll();
	}

}
