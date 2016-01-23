package stsc.database.service.storages.optimizer;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import liquibase.exception.LiquibaseException;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.schemas.optimizer.OrmliteOptimizerExperiment;

public class OptimizerStorageTest {

	@Test
	public void testOptimizerStorage() throws SQLException, LiquibaseException, IOException {
		final OptimizerDatabaseSettings databaseSettings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final OptimizerStorage storage = new OptimizerStorage(databaseSettings);
		Assert.assertNotNull(storage);
		{
			final OrmliteOptimizerExperiment ooe = new OrmliteOptimizerExperiment("experiment A", "this is a test experiment");
			Assert.assertEquals(1, storage.setExperiments(ooe).getNumLinesChanged());
		}
		databaseSettings.dropAll();
	}

}
