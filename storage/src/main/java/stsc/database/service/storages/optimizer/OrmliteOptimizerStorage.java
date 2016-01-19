package stsc.database.service.storages.optimizer;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.lang3.Validate;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import stsc.database.migrations.OptimizerDatabaseSettings;
import stsc.database.service.schemas.optimizer.OrmliteOptimizerExperiments;

public final class OrmliteOptimizerStorage {

	private final ConnectionSource source;

	private final Dao<OrmliteOptimizerExperiments, Integer> experiments;

	public OrmliteOptimizerStorage(final OptimizerDatabaseSettings databaseSettings) throws IOException, SQLException {
		this.source = new JdbcConnectionSource(databaseSettings.getJdbcUrl(), databaseSettings.getLogin(), databaseSettings.getPassword());
		this.experiments = DaoManager.createDao(source, OrmliteOptimizerExperiments.class);
		Validate.isTrue(experiments.isTableExists(), "OrmliteSimulatorStorage table should exists");
		// statistics = DaoManager.createDao(source, OrmliteYahooDownloaderStatistics.class);
		// Validate.isTrue(statistics.isTableExists(), "OrmliteSimulatorStorage table should exists");
	}

	public CreateOrUpdateStatus setExperiments(OrmliteOptimizerExperiments newCategory) throws SQLException {
		newCategory.setCreatedAt();
		newCategory.setUpdatedAt();
		return experiments.createOrUpdate(newCategory);
	}

	public OrmliteOptimizerExperiments getExperiment(Integer id) throws SQLException {
		return experiments.queryForId(id);
	}

}
