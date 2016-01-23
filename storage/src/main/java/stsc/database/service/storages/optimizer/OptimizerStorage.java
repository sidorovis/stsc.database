package stsc.database.service.storages.optimizer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.schemas.optimizer.OrmliteOptimizerExecution;
import stsc.database.service.schemas.optimizer.OrmliteOptimizerExperiment;

public final class OptimizerStorage {

	private final ConnectionSource source;

	private final Dao<OrmliteOptimizerExperiment, Integer> experiments;
	private final Dao<OrmliteOptimizerExecution, Integer> executions;

	public OptimizerStorage(final OptimizerDatabaseSettings databaseSettings) throws IOException, SQLException {
		this.source = new JdbcConnectionSource(databaseSettings.getJdbcUrl(), databaseSettings.getLogin(), databaseSettings.getPassword());
		this.experiments = DaoManager.createDao(source, OrmliteOptimizerExperiment.class);
		this.executions = DaoManager.createDao(source, OrmliteOptimizerExecution.class);
		Validate.isTrue(experiments.isTableExists(), "OrmliteOptimizerExperiments table should exists");
		Validate.isTrue(executions.isTableExists(), "OrmliteOptimizerExecutions table should exists");
	}

	public CreateOrUpdateStatus setExperiments(final OrmliteOptimizerExperiment newExperiment) throws SQLException {
		newExperiment.setCreatedAt();
		newExperiment.setUpdatedAt();
		return experiments.createOrUpdate(newExperiment);
	}

	public CreateOrUpdateStatus setExecutions(OrmliteOptimizerExecution newExecution) throws SQLException {
		newExecution.setCreatedAt();
		newExecution.setUpdatedAt();
		return executions.createOrUpdate(newExecution);
	}

	public OrmliteOptimizerExperiment getExperiment(Integer id) throws SQLException {
		return experiments.queryForId(id);
	}

	public List<OrmliteOptimizerExecution> getExecutions(OrmliteOptimizerExperiment experiment) throws SQLException {
		return executions.queryForEq("experiment_id", experiment.getId());
	}

}
