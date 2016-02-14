package stsc.database.service.schemas.optimizer;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import liquibase.exception.LiquibaseException;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.storages.optimizer.OptimizerExperimentsDatabaseStorage;

public class OrmliteOptimizerExperimentTest {

	@Test
	public void testOrmliteOptimizerExperiments() throws SQLException, LiquibaseException, IOException {
		final OptimizerDatabaseSettings settings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final ConnectionSource source = new JdbcConnectionSource(settings.getJdbcUrl(), settings.getLogin(), settings.getPassword());
		final OptimizerExperimentsDatabaseStorage storage = new OptimizerExperimentsDatabaseStorage(source);
		Assert.assertNotNull(storage);
		{
			final OrmliteOptimizerExperiment experiment = new OrmliteOptimizerExperiment("optimizer", "description for optimization experiment");
			Assert.assertEquals(1, storage.saveExperiment(experiment).getNumLinesChanged());
		}
		{
			final OrmliteOptimizerExperiment copy = storage.loadExperiment(1);
			Assert.assertEquals("optimizer", copy.getTitle());
			Assert.assertEquals("description for optimization experiment", copy.getDescription());
			Assert.assertNotNull(copy.getPeriodFrom());
			Assert.assertNotNull(copy.getPeriodTo());
			Assert.assertNotNull(copy.getCreatedAt());
			Assert.assertNotNull(copy.getUpdatedAt());
		}
		settings.dropAll();
	}

}
