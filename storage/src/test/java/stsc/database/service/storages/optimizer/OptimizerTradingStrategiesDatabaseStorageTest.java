package stsc.database.service.storages.optimizer;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import liquibase.exception.LiquibaseException;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerTradingStrategy;

public class OptimizerTradingStrategiesDatabaseStorageTest {

	private OrmliteOptimizerTradingStrategy addTradingStrategy(final OptimizerTradingStrategiesDatabaseStorage storage) throws SQLException {
		final OrmliteOptimizerTradingStrategy tradingStrategy = new OrmliteOptimizerTradingStrategy();
		Assert.assertEquals(1, storage.saveTradingStrategy(tradingStrategy).getNumLinesChanged());

		return tradingStrategy;
	}

	@Test
	public void testOptimizerDatabaseStorage() throws SQLException, LiquibaseException, IOException {
		final OptimizerDatabaseSettings databaseSettings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final ConnectionSource source = new JdbcConnectionSource(databaseSettings.getJdbcUrl(), databaseSettings.getLogin(), databaseSettings.getPassword());
		final OptimizerTradingStrategiesDatabaseStorage storage = new OptimizerTradingStrategiesDatabaseStorage(source);
		Assert.assertNotNull(storage);

		addTradingStrategy(storage);

		databaseSettings.dropAll();
	}

}
