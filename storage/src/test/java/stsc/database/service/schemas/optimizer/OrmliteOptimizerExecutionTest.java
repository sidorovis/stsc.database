package stsc.database.service.schemas.optimizer;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import liquibase.exception.LiquibaseException;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.storages.optimizer.OptimizerDatabaseStorage;

public class OrmliteOptimizerExecutionTest {

	@Test
	public void testOrmliteOptimizerExecutions() throws SQLException, LiquibaseException, IOException {
		final OptimizerDatabaseSettings settings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final OptimizerDatabaseStorage storage = new OptimizerDatabaseStorage(settings);
		Assert.assertNotNull(storage);
		{
			final OrmliteOptimizerExperiment experiment = new OrmliteOptimizerExperiment("optimizer", "description for optimization experiment");
			Assert.assertEquals(1, storage.saveExperiment(experiment).getNumLinesChanged());
			final OrmliteOptimizerExecution execution = new OrmliteOptimizerExecution(experiment.getId(), 1);
			execution.setExecutionName("exec1");
			execution.setAlgorithmName("SuperProfitableAlgo");
			execution.setAlgorithmType("STOCK or EOD");
			Assert.assertEquals(1, storage.saveExecution(execution).getNumLinesChanged());
		}
		{
			final OrmliteOptimizerExperiment experimentCopy = storage.loadExperiment(1);
			final OrmliteOptimizerExecution copy = storage.loadExecutions(experimentCopy).get(0);
			Assert.assertEquals("exec1", copy.getExecutionName());
			Assert.assertEquals("SuperProfitableAlgo", copy.getAlgorithmName());
			Assert.assertEquals("STOCK or EOD", copy.getAlgorithmType());
			Assert.assertEquals(1, copy.getExperimentId().intValue());
			Assert.assertNotNull(copy.getCreatedAt());
			Assert.assertNotNull(copy.getUpdatedAt());
		}
		settings.dropAll();
	}

}
