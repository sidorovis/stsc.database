package stsc.database.service.storages.optimizer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import liquibase.exception.LiquibaseException;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerDoubleDomen;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerExecution;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerExperiment;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerIntegerDomen;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerStringDomen;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerSubExecutionDomen;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerDoubleArgument;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerExecutionInstance;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerIntegerArgument;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerStringArgument;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerSubExecutionArgument;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerTradingStrategy;

public class OptimizerTradingStrategiesDatabaseStorageTest {

	void createExperiment(OptimizerExperimentsDatabaseStorage storage) throws SQLException {
		final OrmliteOptimizerExperiment experiment = new OrmliteOptimizerExperiment("experiment A", "this is a test experiment");
		Assert.assertEquals(1, storage.saveExperiment(experiment).getNumLinesChanged());

		final OrmliteOptimizerExecution execution = new OrmliteOptimizerExecution(experiment.getId(), 0);
		execution.setAlgorithmName("algo name");
		execution.setExecutionName("execution name");
		execution.setAlgorithmType("execution type");
		Assert.assertEquals(1, storage.saveExecution(execution).getNumLinesChanged());

		final OrmliteOptimizerStringDomen stringParameter = new OrmliteOptimizerStringDomen(execution.getId());
		stringParameter.setParameterDomen("the domen");
		stringParameter.setParameterName("the name");
		Assert.assertEquals(1, storage.saveStringParameter(stringParameter).getNumLinesChanged());

		final OrmliteOptimizerSubExecutionDomen subExecutionParameter = new OrmliteOptimizerSubExecutionDomen(execution.getId());
		subExecutionParameter.setParameterDomen("the domen");
		subExecutionParameter.setParameterName("the name");
		Assert.assertEquals(1, storage.saveSubExecutionParameter(subExecutionParameter).getNumLinesChanged());

		final OrmliteOptimizerIntegerDomen integerParameter = new OrmliteOptimizerIntegerDomen(execution.getId());
		integerParameter.setParameterName("integer parameter");
		integerParameter.setFrom(1);
		integerParameter.setStep(2);
		integerParameter.setTo(14);
		Assert.assertEquals(1, storage.saveIntegerParameter(integerParameter).getNumLinesChanged());

		final OrmliteOptimizerDoubleDomen doubleParameter = new OrmliteOptimizerDoubleDomen(execution.getId());
		doubleParameter.setParameterName("double parameter");
		doubleParameter.setFrom(1.0);
		doubleParameter.setStep(2.0);
		doubleParameter.setTo(14.0);
		Assert.assertEquals(1, storage.saveDoubleParameter(doubleParameter).getNumLinesChanged());
		
		experiment.setCommited();
		Assert.assertEquals(1, storage.saveExperiment(experiment).getNumLinesChanged());
	}

	private OrmliteOptimizerTradingStrategy addTradingStrategy(final OptimizerExperimentsDatabaseStorage experimentsDatabaseStorage, final OptimizerTradingStrategiesDatabaseStorage tradigStrategiesStorage) throws SQLException {
		final Optional<OrmliteOptimizerExperiment> e = experimentsDatabaseStorage.bookExperiment();
		Assert.assertTrue(e.isPresent());
		final OrmliteOptimizerExperiment experiment = e.get();
		final OrmliteOptimizerExecution execution = experimentsDatabaseStorage.loadExecutions(experiment).get(0);
		
		final OrmliteOptimizerTradingStrategy tradingStrategy = new OrmliteOptimizerTradingStrategy(experiment.getId());
		Assert.assertEquals(1, tradigStrategiesStorage.saveTradingStrategy(tradingStrategy).getNumLinesChanged());
		Assert.assertEquals(experiment.getId().intValue(), tradingStrategy.getExperimentId());

		final OrmliteOptimizerExecutionInstance executionInstance = new OrmliteOptimizerExecutionInstance(tradingStrategy.getId());
		executionInstance.setAlgorithmName("algo_name");
		executionInstance.setAlgorithmType("algo_type");
		executionInstance.setIndexNumber(15);
		executionInstance.setExecutionInstanceName("execution1");
		Assert.assertEquals(1, tradigStrategiesStorage.saveExecutionInstance(executionInstance).getNumLinesChanged());

		final OrmliteOptimizerStringArgument stringArgument = new OrmliteOptimizerStringArgument(executionInstance.getId());
		stringArgument.setStringDomenId(experimentsDatabaseStorage.loadStringParameters(execution).get(0).getId());
		stringArgument.setArgumentIndex(89);
		Assert.assertEquals(1, tradigStrategiesStorage.saveStringArgument(stringArgument).getNumLinesChanged());

		final OrmliteOptimizerSubExecutionArgument subExecutionArgument = new OrmliteOptimizerSubExecutionArgument(executionInstance.getId());
		subExecutionArgument.setSubExecutionDomenId(experimentsDatabaseStorage.loadSubExecutionParameters(execution).get(0).getId());
		subExecutionArgument.setArgumentIndex(545);
		Assert.assertEquals(1, tradigStrategiesStorage.saveSubExecutionArgument(subExecutionArgument).getNumLinesChanged());

		final OrmliteOptimizerIntegerArgument integerArgument = new OrmliteOptimizerIntegerArgument(executionInstance.getId());
		integerArgument.setIntegerDomenId(experimentsDatabaseStorage.loadIntegerParameters(execution).get(0).getId());
		integerArgument.setArgumentIndex(89);
		Assert.assertEquals(1, tradigStrategiesStorage.saveIntegerArgument(integerArgument).getNumLinesChanged());

		final OrmliteOptimizerDoubleArgument doubleArgument = new OrmliteOptimizerDoubleArgument(executionInstance.getId());
		doubleArgument.setDoubleDomenId(experimentsDatabaseStorage.loadDoubleParameters(execution).get(0).getId());
		doubleArgument.setArgumentIndex(23);
		Assert.assertEquals(1, tradigStrategiesStorage.saveDoubleArgument(doubleArgument).getNumLinesChanged());

		experiment.setProcessed();
		Assert.assertEquals(1, experimentsDatabaseStorage.saveExperiment(experiment).getNumLinesChanged());
		return tradingStrategy;
	}

	@Test
	public void testOptimizerDatabaseStorage() throws SQLException, LiquibaseException, IOException, URISyntaxException {
		final OptimizerDatabaseSettings databaseSettings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final OptimizerDatabaseStorage storage = new OptimizerDatabaseStorage(databaseSettings);
		Assert.assertNotNull(storage);

		createExperiment(storage.getExperimentsStorage());
		addTradingStrategy(storage.getExperimentsStorage(), storage.getTradingStrategiesStorage());

		databaseSettings.dropAll();
	}

}
