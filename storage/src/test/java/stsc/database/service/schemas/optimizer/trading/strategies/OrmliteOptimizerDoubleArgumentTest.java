package stsc.database.service.schemas.optimizer.trading.strategies;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import liquibase.exception.LiquibaseException;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerDoubleDomen;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerExecution;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerExperiment;
import stsc.database.service.storages.optimizer.OptimizerDatabaseStorage;

public class OrmliteOptimizerDoubleArgumentTest {

	int createExperimentAndGetDoubleDomenId(OrmliteOptimizerExperiment experiment, final OptimizerDatabaseStorage storage) throws SQLException {
		final OrmliteOptimizerExecution execution = new OrmliteOptimizerExecution(experiment.getId(), 1);
		execution.setExecutionName("exec1");
		execution.setAlgorithmName("SuperProfitableAlgo");
		execution.setAlgorithmType("STOCK or EOD");
		Assert.assertEquals(1, storage.getExperimentsStorage().saveExecution(execution).getNumLinesChanged());
		final OrmliteOptimizerDoubleDomen doubleParameter = new OrmliteOptimizerDoubleDomen(execution.getId());
		doubleParameter.setParameterName("parameterOfAlgorithmA");
		doubleParameter.setFrom(10.0);
		doubleParameter.setStep(2.5);
		doubleParameter.setTo(20.0);
		Assert.assertEquals(1, storage.getExperimentsStorage().saveDoubleParameter(doubleParameter).getNumLinesChanged());
		return doubleParameter.getId();
	}

	@Test
	public void testOrmliteOptimizerDoubleArguments() throws SQLException, LiquibaseException, IOException, URISyntaxException {
		final OptimizerDatabaseSettings settings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final OptimizerDatabaseStorage storage = new OptimizerDatabaseStorage(settings);
		Assert.assertNotNull(storage);
		final OrmliteOptimizerExperiment experiment = new OrmliteOptimizerExperiment("optimizer", "description for optimization experiment");
		Assert.assertEquals(1, storage.getExperimentsStorage().saveExperiment(experiment).getNumLinesChanged());
		final int domenId = createExperimentAndGetDoubleDomenId(experiment, storage);
		{
			final OrmliteOptimizerTradingStrategy tradingStrategy = new OrmliteOptimizerTradingStrategy(experiment.getId());
			Assert.assertEquals(1, storage.getTradingStrategiesStorage().saveTradingStrategy(tradingStrategy).getNumLinesChanged());

			final OrmliteOptimizerExecutionInstance executionInstance = new OrmliteOptimizerExecutionInstance(tradingStrategy.getId());
			executionInstance.setIndexNumber(2);
			executionInstance.setExecutionInstanceName("execution instance name");
			executionInstance.setAlgorithmName("algo name");
			executionInstance.setAlgorithmType("algo type");
			Assert.assertEquals(1, storage.getTradingStrategiesStorage().saveExecutionInstance(executionInstance).getNumLinesChanged());

			final OrmliteOptimizerDoubleArgument doubleArgument = new OrmliteOptimizerDoubleArgument(executionInstance.getId());
			doubleArgument.setDoubleDomenId(domenId);
			doubleArgument.setArgumentIndex(3);

			Assert.assertEquals(1, storage.getTradingStrategiesStorage().saveDoubleArgument(doubleArgument).getNumLinesChanged());
		}
		{
			final OrmliteOptimizerTradingStrategy ts = storage.getTradingStrategiesStorage().loadTradingStrategy(1);
			final OrmliteOptimizerExecutionInstance executionInstance = storage.getTradingStrategiesStorage().loadExecutionInstance(ts).get(0);
			final OrmliteOptimizerDoubleArgument doubleArguments = storage.getTradingStrategiesStorage().loadDoubleArguments(executionInstance).get(0);
			Assert.assertEquals(1, doubleArguments.getId().intValue());
			Assert.assertEquals(domenId, doubleArguments.getDoubleDomenId());
			Assert.assertEquals(3, doubleArguments.getArgumentIndex());
			Assert.assertNotNull(doubleArguments.getCreatedAt());
			Assert.assertNotNull(doubleArguments.getUpdatedAt());
		}
		settings.dropAll();
	}

}
