package stsc.database.migrations.optimizer;

import java.io.IOException;
import java.sql.SQLException;

import liquibase.exception.LiquibaseException;

import org.junit.Assert;
import org.junit.Test;

public class OptimizerDatabaseSettingsTest {

	@Test
	public void testOptimizerDatabaseSettings() throws IOException {
		final OptimizerDatabaseSettings ds = OptimizerDatabaseSettings.test();
		Assert.assertEquals("org.h2.Driver", ds.getJdbcDriver());
		Assert.assertEquals("jdbc:h2:mem:stsc_optimizer;DB_CLOSE_DELAY=-1", ds.getJdbcUrl());
	}

	@Test
	public void testOptimizerCreateConnectionToLiquibase() throws SQLException, IOException, LiquibaseException {
		final OptimizerDatabaseSettings ds = OptimizerDatabaseSettings.test();
		ds.migrate();
	}
}
