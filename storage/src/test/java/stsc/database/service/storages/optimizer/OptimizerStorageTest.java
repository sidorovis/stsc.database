package stsc.database.service.storages.optimizer;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import liquibase.exception.LiquibaseException;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.schemas.optimizer.OrmliteOptimizerExecution;
import stsc.database.service.schemas.optimizer.OrmliteOptimizerExperiment;
import stsc.database.service.schemas.optimizer.OrmliteOptimizerStringParameter;
import stsc.database.service.schemas.optimizer.OrmliteOptimizerSubExecutionParameter;

public class OptimizerStorageTest {

	@Test
	public void testOptimizerStorage() throws SQLException, LiquibaseException, IOException {
		final OptimizerDatabaseSettings databaseSettings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final OptimizerStorage storage = new OptimizerStorage(databaseSettings);
		Assert.assertNotNull(storage);

		final OrmliteOptimizerExperiment ooe = new OrmliteOptimizerExperiment("experiment A", "this is a test experiment");
		Assert.assertEquals(1, storage.setExperiments(ooe).getNumLinesChanged());

		final OrmliteOptimizerExecution execution = new OrmliteOptimizerExecution(ooe.getId(), 0);
		execution.setAlgorithmName("algo name");
		execution.setExecutionName("execution name");
		execution.setExecutionType("execution type");
		Assert.assertEquals(1, storage.setExecutions(execution).getNumLinesChanged());

		final OrmliteOptimizerStringParameter stringParameter = new OrmliteOptimizerStringParameter(execution.getId());
		stringParameter.setParameterDomen("the domen");
		stringParameter.setParameterName("the name");
		Assert.assertEquals(1, storage.setStringParameters(stringParameter).getNumLinesChanged());

		final OrmliteOptimizerSubExecutionParameter subExecutionParameter = new OrmliteOptimizerSubExecutionParameter(execution.getId());
		subExecutionParameter.setParameterDomen("the domen");
		subExecutionParameter.setParameterName("the name");
		Assert.assertEquals(1, storage.setSubExecutionParameters(subExecutionParameter).getNumLinesChanged());

		databaseSettings.dropAll();
	}

}
