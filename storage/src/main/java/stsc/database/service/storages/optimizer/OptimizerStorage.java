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
import stsc.database.service.schemas.optimizer.OrmliteOptimizerStringParameter;
import stsc.database.service.schemas.optimizer.OrmliteOptimizerSubExecutionParameter;

public final class OptimizerStorage {

	private final ConnectionSource source;

	private final Dao<OrmliteOptimizerExperiment, Integer> experiments;
	private final Dao<OrmliteOptimizerExecution, Integer> executions;
	private final Dao<OrmliteOptimizerStringParameter, Integer> stringParameters;
	private final Dao<OrmliteOptimizerSubExecutionParameter, Integer> subExecutionParameters;

	public OptimizerStorage(final OptimizerDatabaseSettings databaseSettings) throws IOException, SQLException {
		this.source = new JdbcConnectionSource(databaseSettings.getJdbcUrl(), databaseSettings.getLogin(), databaseSettings.getPassword());
		this.experiments = DaoManager.createDao(source, OrmliteOptimizerExperiment.class);
		this.executions = DaoManager.createDao(source, OrmliteOptimizerExecution.class);
		this.stringParameters = DaoManager.createDao(source, OrmliteOptimizerStringParameter.class);
		this.subExecutionParameters = DaoManager.createDao(source, OrmliteOptimizerSubExecutionParameter.class);
		Validate.isTrue(experiments.isTableExists(), "OrmliteOptimizerExperiments table should exists");
		Validate.isTrue(executions.isTableExists(), "OrmliteOptimizerExecutions table should exists");
		Validate.isTrue(stringParameters.isTableExists(), "OrmliteOptimizerStringParameters table should exists");
		Validate.isTrue(subExecutionParameters.isTableExists(), "OrmliteOptimizerSubExecutionParameters table should exists");
	}

	public CreateOrUpdateStatus setExperiments(final OrmliteOptimizerExperiment newExperiment) throws SQLException {
		newExperiment.setCreatedAt();
		newExperiment.setUpdatedAt();
		return experiments.createOrUpdate(newExperiment);
	}

	public OrmliteOptimizerExperiment getExperiment(Integer id) throws SQLException {
		return experiments.queryForId(id);
	}

	public CreateOrUpdateStatus setExecutions(OrmliteOptimizerExecution newExecution) throws SQLException {
		newExecution.setCreatedAt();
		newExecution.setUpdatedAt();
		return executions.createOrUpdate(newExecution);
	}

	public List<OrmliteOptimizerExecution> getExecutions(OrmliteOptimizerExperiment experiment) throws SQLException {
		return executions.queryForEq("experiment_id", experiment.getId());
	}

	public CreateOrUpdateStatus setStringParameters(OrmliteOptimizerStringParameter newStringParameter) throws SQLException {
		newStringParameter.setCreatedAt();
		newStringParameter.setUpdatedAt();
		return stringParameters.createOrUpdate(newStringParameter);
	}

	public List<OrmliteOptimizerStringParameter> getStringParameters(OrmliteOptimizerExecution execution) throws SQLException {
		return stringParameters.queryForEq("execution_id", execution.getId());
	}

	public CreateOrUpdateStatus setSubExecutionParameters(OrmliteOptimizerSubExecutionParameter newSubExecutionParameter) throws SQLException {
		newSubExecutionParameter.setCreatedAt();
		newSubExecutionParameter.setUpdatedAt();
		return subExecutionParameters.createOrUpdate(newSubExecutionParameter);
	}

	public List<OrmliteOptimizerSubExecutionParameter> getSubExecutionParameters(OrmliteOptimizerExecution execution) throws SQLException {
		return subExecutionParameters.queryForEq("execution_id", execution.getId());
	}

}
