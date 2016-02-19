package stsc.database.service.schemas.optimizer.experiments;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import liquibase.exception.LiquibaseException;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerExecution;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerExperiment;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerStringParameter;
import stsc.database.service.storages.optimizer.OptimizerExperimentsDatabaseStorage;

public class OrmliteOptimizerStringParameterTest {

	@Test
	public void testOrmliteOptimizerStringParameters() throws SQLException, LiquibaseException, IOException, URISyntaxException {
		final OptimizerDatabaseSettings settings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final ConnectionSource source = new JdbcConnectionSource(settings.getJdbcUrl(), settings.getLogin(), settings.getPassword());
		final OptimizerExperimentsDatabaseStorage storage = new OptimizerExperimentsDatabaseStorage(source);
		Assert.assertNotNull(storage);
		{
			final OrmliteOptimizerExperiment experiment = new OrmliteOptimizerExperiment("optimizer", "description for optimization experiment");
			Assert.assertEquals(1, storage.saveExperiment(experiment).getNumLinesChanged());
			final OrmliteOptimizerExecution execution = new OrmliteOptimizerExecution(experiment.getId(), 1);
			execution.setExecutionName("exec1");
			execution.setAlgorithmName("SuperProfitableAlgo");
			execution.setAlgorithmType("STOCK or EOD");
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
