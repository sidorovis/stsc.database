package stsc.database.service.schemas.optimizer;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import liquibase.exception.LiquibaseException;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.storages.optimizer.OptimizerDatabaseStorage;

public class OrmliteOptimizerSubExecutionParameterTest {

	@Test
	public void testOrmliteOptimizerStringParameters() throws SQLException, LiquibaseException, IOException {
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
			final OrmliteOptimizerStringParameter stringParameter = new OrmliteOptimizerStringParameter(execution.getId());
			stringParameter.setParameterName("parameterOfAlgorithmA");
			stringParameter.setParameterDomen("parameter|domen");
			Assert.assertEquals(1, storage.saveStringParameter(stringParameter).getNumLinesChanged());
		}
		{
			final OrmliteOptimizerExperiment experimentCopy = storage.loadExperiment(1);
			final OrmliteOptimizerExecution executionCopy = storage.loadExecutions(experimentCopy).get(0);
			final OrmliteOptimizerStringParameter copy = storage.loadStringParameters(executionCopy).get(0);
			Assert.assertEquals("parameterOfAlgorithmA", copy.getParameterName());
			Assert.assertEquals("parameter|domen", copy.getParameterDomen());
			Assert.assertNotNull(copy.getCreatedAt());
			Assert.assertNotNull(copy.getUpdatedAt());
		}
		settings.dropAll();
	}

}
