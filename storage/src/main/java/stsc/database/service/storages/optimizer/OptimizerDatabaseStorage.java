package stsc.database.service.storages.optimizer;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;

public final class OptimizerDatabaseStorage {

	private final ConnectionSource source;

	private final OptimizerExperimentsDatabaseStorage optimizerExperimentsDatabaseStorage;

	public OptimizerDatabaseStorage(final OptimizerDatabaseSettings databaseSettings) throws IOException, SQLException {
		this.source = new JdbcConnectionSource(databaseSettings.getJdbcUrl(), databaseSettings.getLogin(), databaseSettings.getPassword());
		optimizerExperimentsDatabaseStorage = new OptimizerExperimentsDatabaseStorage(source);
	}

	public OptimizerExperimentsDatabaseStorage getExperiments() {
		return optimizerExperimentsDatabaseStorage;
	}

}
