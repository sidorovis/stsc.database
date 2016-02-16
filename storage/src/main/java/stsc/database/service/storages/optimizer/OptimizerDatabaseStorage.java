package stsc.database.service.storages.optimizer;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;

/**
 * {@link OptimizerDatabaseStorage} composite for all Optimizer database storages.
 */
public final class OptimizerDatabaseStorage {

	private final ConnectionSource source;

	private final OptimizerExperimentsDatabaseStorage optimizerExperimentsDatabaseStorage;
	private final OptimizerTradingStrategiesDatabaseStorage optimizerTradingStrategiesDatabaseStorage;

	public OptimizerDatabaseStorage(final OptimizerDatabaseSettings databaseSettings) throws IOException, SQLException {
		this.source = new JdbcConnectionSource(databaseSettings.getJdbcUrl(), databaseSettings.getLogin(), databaseSettings.getPassword());
		optimizerExperimentsDatabaseStorage = new OptimizerExperimentsDatabaseStorage(source);
		optimizerTradingStrategiesDatabaseStorage = new OptimizerTradingStrategiesDatabaseStorage(source);
	}

	public OptimizerExperimentsDatabaseStorage getExperiments() {
		return optimizerExperimentsDatabaseStorage;
	}

	public OptimizerTradingStrategiesDatabaseStorage getOptimizerTradingStrategiesDatabaseStorage() {
		return optimizerTradingStrategiesDatabaseStorage;
	}

}
