package stsc.database.service.storages.optimizer;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import liquibase.exception.LiquibaseException;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.schemas.optimizer.OrmliteOptimizerDoubleParameter;
import stsc.database.service.schemas.optimizer.OrmliteOptimizerExecution;
import stsc.database.service.schemas.optimizer.OrmliteOptimizerExperiment;
import stsc.database.service.schemas.optimizer.OrmliteOptimizerIntegerParameter;
import stsc.database.service.schemas.optimizer.OrmliteOptimizerStringParameter;
import stsc.database.service.schemas.optimizer.OrmliteOptimizerSubExecutionParameter;

public class OptimizerDatabaseStorageTest {

	@Test
	public void testOptimizerDatabaseStorage() throws SQLException, LiquibaseException, IOException {
		final OptimizerDatabaseSettings databaseSettings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final OptimizerDatabaseStorage storage = new OptimizerDatabaseStorage(databaseSettings);
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

		final OrmliteOptimizerIntegerParameter integerParameter = new OrmliteOptimizerIntegerParameter(execution.getId());
		integerParameter.setParameterName("integer parameter");
		integerParameter.setFrom(1);
		integerParameter.setStep(2);
		integerParameter.setTo(14);
		Assert.assertEquals(1, storage.setIntegerParameters(integerParameter).getNumLinesChanged());

		final OrmliteOptimizerDoubleParameter doubleParameter = new OrmliteOptimizerDoubleParameter(execution.getId());
		doubleParameter.setParameterName("double parameter");
		doubleParameter.setFrom(1.0);
		doubleParameter.setStep(2.0);
		doubleParameter.setTo(14.0);
		Assert.assertEquals(1, storage.setDoubleParameters(doubleParameter).getNumLinesChanged());
		
		databaseSettings.dropAll();
	}

}
