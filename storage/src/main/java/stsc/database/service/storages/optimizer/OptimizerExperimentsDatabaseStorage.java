package stsc.database.service.storages.optimizer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerDoubleParameter;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerExecution;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerExperiment;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerExperimentLock;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerIntegerParameter;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerStringParameter;
import stsc.database.service.schemas.optimizer.experiments.OrmliteOptimizerSubExecutionParameter;

public final class OptimizerExperimentsDatabaseStorage {

	private final Dao<OrmliteOptimizerExperiment, Integer> experiments;
	private final Dao<OrmliteOptimizerExperimentLock, Integer> experimentLocks;

	private final Dao<OrmliteOptimizerExecution, Integer> executions;
	private final Dao<OrmliteOptimizerStringParameter, Integer> stringParameters;
	private final Dao<OrmliteOptimizerSubExecutionParameter, Integer> subExecutionParameters;
	private final Dao<OrmliteOptimizerIntegerParameter, Integer> integerParameters;
	private final Dao<OrmliteOptimizerDoubleParameter, Integer> doubleParameters;

	public OptimizerExperimentsDatabaseStorage(final ConnectionSource source) throws IOException, SQLException {
		this.experiments = DaoManager.createDao(source, OrmliteOptimizerExperiment.class);
		this.experimentLocks = DaoManager.createDao(source, OrmliteOptimizerExperimentLock.class);
		this.executions = DaoManager.createDao(source, OrmliteOptimizerExecution.class);
		this.stringParameters = DaoManager.createDao(source, OrmliteOptimizerStringParameter.class);
		this.subExecutionParameters = DaoManager.createDao(source, OrmliteOptimizerSubExecutionParameter.class);
		this.integerParameters = DaoManager.createDao(source, OrmliteOptimizerIntegerParameter.class);
		this.doubleParameters = DaoManager.createDao(source, OrmliteOptimizerDoubleParameter.class);
		Validate.isTrue(experiments.isTableExists(), "OrmliteOptimizerExperiments table should exists");
		Validate.isTrue(experimentLocks.isTableExists(), "OrmliteOptimizerExperimentLocks table should exists");
		Validate.isTrue(executions.isTableExists(), "OrmliteOptimizerExecutions table should exists");
		Validate.isTrue(stringParameters.isTableExists(), "OrmliteOptimizerStringParameters table should exists");
		Validate.isTrue(subExecutionParameters.isTableExists(), "OrmliteOptimizerSubExecutionParameters table should exists");
		Validate.isTrue(integerParameters.isTableExists(), "OrmliteOptimizerIntegerParameters table should exists");
		Validate.isTrue(doubleParameters.isTableExists(), "OrmliteOptimizerDoubleParameters table should exists");
	}

	/**
	 * automatically delete lock from experiment
	 */
	public CreateOrUpdateStatus saveExperiment(final OrmliteOptimizerExperiment newExperiment) throws SQLException {
		newExperiment.setCreatedAt();
		newExperiment.setUpdatedAt();
		// we should delete experiment lock in case if experiment was processed
		if (newExperiment.getId() != null && newExperiment.isProcessed()) {
			deleteLock(newExperiment);
		}
		return experiments.createOrUpdate(newExperiment);
	}

	public OrmliteOptimizerExperiment loadExperiment(Integer id) throws SQLException {
		return experiments.queryForId(id);
	}

	public Optional<OrmliteOptimizerExperiment> bookExperiment() throws SQLException {
		int counter = 0;
		while (counter < 5) {
			counter += 1;
			final PreparedQuery<OrmliteOptimizerExperiment> commitedExperimentsQuery = experiments.queryBuilder(). //
					where().eq("user_id", 0). //
					and().eq("commited", true). //
					and().eq("processed", false). //
					prepare();

			final OrmliteOptimizerExperiment commitedExperiment = experiments.queryForFirst(commitedExperimentsQuery);
			if (commitedExperiment == null) {
				break;
			}
			try {
				final boolean isBooked = saveExperimentLock(commitedExperiment) == 1;
				if (isBooked) {
					return Optional.of(commitedExperiment);
				}
			} catch (SQLException e) {
			}
		}
		return Optional.empty();
	}

	public void deleteLock(final OrmliteOptimizerExperiment newExperiment) throws SQLException {
		final OrmliteOptimizerExperimentLock queryForEq = experimentLocks.queryForFirst( //
				experimentLocks.queryBuilder().where().eq("experiment_id", newExperiment.getId()).prepare());
		if (queryForEq != null) {
			experimentLocks.delete(queryForEq);
		}
	}

	private int saveExperimentLock(OrmliteOptimizerExperiment commitedExperiment) throws SQLException {
		final OrmliteOptimizerExperimentLock experimentLock = new OrmliteOptimizerExperimentLock(commitedExperiment.getId());
		experimentLock.setCreatedAt();
		experimentLock.setUpdatedAt();
		return experimentLocks.create(experimentLock);
	}

	public CreateOrUpdateStatus saveExecution(OrmliteOptimizerExecution newExecution) throws SQLException {
		newExecution.setCreatedAt();
		newExecution.setUpdatedAt();
		return executions.createOrUpdate(newExecution);
	}

	public List<OrmliteOptimizerExecution> loadExecutions(OrmliteOptimizerExperiment experiment) throws SQLException {
		final QueryBuilder<OrmliteOptimizerExecution, Integer> queryBuilder = executions.queryBuilder();
		queryBuilder.setWhere(executions.queryBuilder(). //
				where().eq("experiment_id", experiment.getId()));
		queryBuilder.orderBy("index_number", true);

		return queryBuilder.query();
	}

	public CreateOrUpdateStatus saveStringParameter(OrmliteOptimizerStringParameter newStringParameter) throws SQLException {
		newStringParameter.setCreatedAt();
		newStringParameter.setUpdatedAt();
		return stringParameters.createOrUpdate(newStringParameter);
	}

	public List<OrmliteOptimizerStringParameter> loadStringParameters(OrmliteOptimizerExecution execution) throws SQLException {
		return stringParameters.queryForEq("execution_id", execution.getId());
	}

	public CreateOrUpdateStatus saveSubExecutionParameter(OrmliteOptimizerSubExecutionParameter newSubExecutionParameter) throws SQLException {
		newSubExecutionParameter.setCreatedAt();
		newSubExecutionParameter.setUpdatedAt();
		return subExecutionParameters.createOrUpdate(newSubExecutionParameter);
	}

	public List<OrmliteOptimizerSubExecutionParameter> loadSubExecutionParameters(OrmliteOptimizerExecution execution) throws SQLException {
		return subExecutionParameters.queryForEq("execution_id", execution.getId());
	}

	public CreateOrUpdateStatus saveIntegerParameter(OrmliteOptimizerIntegerParameter newIntegerParameter) throws SQLException {
		newIntegerParameter.setCreatedAt();
		newIntegerParameter.setUpdatedAt();
		return integerParameters.createOrUpdate(newIntegerParameter);
	}

	public List<OrmliteOptimizerIntegerParameter> loadIntegerParameters(OrmliteOptimizerExecution execution) throws SQLException {
		return integerParameters.queryForEq("execution_id", execution.getId());
	}

	public CreateOrUpdateStatus saveDoubleParameter(OrmliteOptimizerDoubleParameter newDoubleParameter) throws SQLException {
		newDoubleParameter.setCreatedAt();
		newDoubleParameter.setUpdatedAt();
		return doubleParameters.createOrUpdate(newDoubleParameter);
	}

	public List<OrmliteOptimizerDoubleParameter> loadDoubleParameters(OrmliteOptimizerExecution execution) throws SQLException {
		return doubleParameters.queryForEq("execution_id", execution.getId());
	}

}
