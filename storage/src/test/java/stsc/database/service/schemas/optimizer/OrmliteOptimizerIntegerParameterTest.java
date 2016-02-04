package stsc.database.service.schemas.optimizer;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import liquibase.exception.LiquibaseException;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.storages.optimizer.OptimizerDatabaseStorage;

public class OrmliteOptimizerIntegerParameterTest {

	@Test
	public void testOrmliteOptimizerIntegerParameters() throws SQLException, LiquibaseException, IOException {
		final OptimizerDatabaseSettings settings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final OptimizerDatabaseStorage storage = new OptimizerDatabaseStorage(settings);
		Assert.assertNotNull(storage);
		{
			final OrmliteOptimizerExperiment experiment = new OrmliteOptimizerExperiment("optimizer", "description for optimization experiment");
			Assert.assertEquals(1, storage.setExperiments(experiment).getNumLinesChanged());
			final OrmliteOptimizerExecution execution = new OrmliteOptimizerExecution(experiment.getId(), 1);
			execution.setExecutionName("exec1");
			execution.setAlgorithmName("SuperProfitableAlgo");
			execution.setExecutionType("STOCK or EOD");
			Assert.assertEquals(1, storage.setExecutions(execution).getNumLinesChanged());
			final OrmliteOptimizerIntegerParameter integerParameter = new OrmliteOptimizerIntegerParameter(execution.getId());
			integerParameter.setParameterName("parameterOfAlgorithmA");
			integerParameter.setFrom(10);
			integerParameter.setStep(2);
			integerParameter.setTo(20);
			Assert.assertEquals(1, storage.setIntegerParameters(integerParameter).getNumLinesChanged());
		}
		{
			final OrmliteOptimizerExperiment experimentCopy = storage.getExperiment(1);
			final OrmliteOptimizerExecution executionCopy = storage.getExecutions(experimentCopy).get(0);
			final OrmliteOptimizerIntegerParameter copy = storage.getIntegerParameters(executionCopy).get(0);
			Assert.assertEquals("parameterOfAlgorithmA", copy.getParameterName());
			Assert.assertEquals(10, copy.getFrom().intValue());
			Assert.assertEquals(2, copy.getStep().intValue());
			Assert.assertEquals(20, copy.getTo().intValue());
			Assert.assertNotNull(copy.getCreatedAt());
			Assert.assertNotNull(copy.getUpdatedAt());
		}
		settings.dropAll();
	}

}
