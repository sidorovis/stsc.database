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
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerIntegerDomen;
import stsc.database.service.storages.optimizer.OptimizerDatabaseStorage;

public class OrmliteOptimizerIntegerArgumentTest {

	int createExperimentAndGetIntegerDomenId(OrmliteOptimizerExperiment experiment, final OptimizerDatabaseStorage storage) throws SQLException {
		final OrmliteOptimizerExecution execution = new OrmliteOptimizerExecution(experiment.getId(), 1);
		execution.setExecutionName("exec1");
		execution.setAlgorithmName("SuperProfitableAlgo");
		execution.setAlgorithmType("STOCK or EOD");
		Assert.assertEquals(1, storage.getExperimentsStorage().saveExecution(execution).getNumLinesChanged());
		final OrmliteOptimizerIntegerDomen integerParameter = new OrmliteOptimizerIntegerDomen(execution.getId());
		integerParameter.setParameterName("parameterOfAlgorithmA");
		integerParameter.setFrom(1);
		integerParameter.setStep(5);
		integerParameter.setTo(20);
		Assert.assertEquals(1, storage.getExperimentsStorage().saveIntegerParameter(integerParameter).getNumLinesChanged());
		return integerParameter.getId();
	}

	@Test
	public void testOrmliteOptimizerIntegerArguments() throws SQLException, LiquibaseException, IOException, URISyntaxException {
		final OptimizerDatabaseSettings settings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final OptimizerDatabaseStorage storage = new OptimizerDatabaseStorage(settings);
		Assert.assertNotNull(storage);
		final OrmliteOptimizerExperiment experiment = new OrmliteOptimizerExperiment("optimizer", "description for optimization experiment");
		Assert.assertEquals(1, storage.getExperimentsStorage().saveExperiment(experiment).getNumLinesChanged());
		final int domenId = createExperimentAndGetIntegerDomenId(experiment, storage);
		{
			final OrmliteOptimizerTradingStrategy tradingStrategy = new OrmliteOptimizerTradingStrategy(experiment.getId());
			Assert.assertEquals(1, storage.getTradingStrategiesStorage().saveTradingStrategy(tradingStrategy).getNumLinesChanged());

			final OrmliteOptimizerExecutionInstance executionInstance = new OrmliteOptimizerExecutionInstance(tradingStrategy.getId());
			executionInstance.setIndexNumber(2);
			executionInstance.setExecutionInstanceName("execution instance name");
			executionInstance.setAlgorithmName("algo name");
			executionInstance.setAlgorithmType("algo type");
			Assert.assertEquals(1, storage.getTradingStrategiesStorage().saveExecutionInstance(executionInstance).getNumLinesChanged());

			final OrmliteOptimizerIntegerArgument integerArgument = new OrmliteOptimizerIntegerArgument(executionInstance.getId());
			integerArgument.setIntegerDomenId(domenId);
			integerArgument.setArgumentIndex(34);

			Assert.assertEquals(1, storage.getTradingStrategiesStorage().saveIntegerArgument(integerArgument).getNumLinesChanged());
		}
		{
			final OrmliteOptimizerTradingStrategy ts = storage.getTradingStrategiesStorage().loadTradingStrategy(1);
			final OrmliteOptimizerExecutionInstance executionInstance = storage.getTradingStrategiesStorage().loadExecutionInstance(ts).get(0);
			final OrmliteOptimizerIntegerArgument integerArguments = storage.getTradingStrategiesStorage().loadIntegerArguments(executionInstance).get(0);
			Assert.assertEquals(1, integerArguments.getId().intValue());
			Assert.assertEquals(domenId, integerArguments.getIntegerDomenId());
			Assert.assertEquals(34, integerArguments.getArgumentIndex());
			Assert.assertNotNull(integerArguments.getCreatedAt());
			Assert.assertNotNull(integerArguments.getUpdatedAt());
		}
		settings.dropAll();
	}

}
