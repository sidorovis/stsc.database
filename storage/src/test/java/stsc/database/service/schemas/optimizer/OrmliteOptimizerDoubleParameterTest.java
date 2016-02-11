package stsc.database.service.schemas.optimizer;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import liquibase.exception.LiquibaseException;
import stsc.common.Settings;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.storages.optimizer.OptimizerDatabaseStorage;

public class OrmliteOptimizerDoubleParameterTest {

	@Test
	public void testOrmliteOptimizerDoubleParameters() throws SQLException, LiquibaseException, IOException {
		final OptimizerDatabaseSettings settings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final OptimizerDatabaseStorage storage = new OptimizerDatabaseStorage(settings);
		Assert.assertNotNull(storage);
		{
			final OrmliteOptimizerExperiment experiment = new OrmliteOptimizerExperiment("optimizer", "description for optimization experiment");
			Assert.assertEquals(1, storage.saveExperiment(experiment).getNumLinesChanged());
			final OrmliteOptimizerExecution execution = new OrmliteOptimizerExecution(experiment.getId(), 1);
			execution.setExecutionName("exec1");
			execution.setAlgorithmName("SuperProfitableAlgo");
			execution.setExecutionType("STOCK or EOD");
			Assert.assertEquals(1, storage.saveExecution(execution).getNumLinesChanged());
			final OrmliteOptimizerDoubleParameter doubleParameter = new OrmliteOptimizerDoubleParameter(execution.getId());
			doubleParameter.setParameterName("parameterOfAlgorithmA");
			doubleParameter.setFrom(10.0);
			doubleParameter.setStep(2.5);
			doubleParameter.setTo(20.0);
			Assert.assertEquals(1, storage.saveDoubleParameter(doubleParameter).getNumLinesChanged());
		}
		{
			final OrmliteOptimizerExperiment experimentCopy = storage.loadExperiment(1);
			final OrmliteOptimizerExecution executionCopy = storage.loadExecutions(experimentCopy).get(0);
			final OrmliteOptimizerDoubleParameter copy = storage.loadDoubleParameters(executionCopy).get(0);
			Assert.assertEquals("parameterOfAlgorithmA", copy.getParameterName());
			Assert.assertEquals(10.0, copy.getFrom().doubleValue(), Settings.doubleEpsilon);
			Assert.assertEquals(2.5, copy.getStep().doubleValue(), Settings.doubleEpsilon);
			Assert.assertEquals(20.0, copy.getTo().doubleValue(), Settings.doubleEpsilon);
			Assert.assertNotNull(copy.getCreatedAt());
			Assert.assertNotNull(copy.getUpdatedAt());
		}
		settings.dropAll();
	}

}
