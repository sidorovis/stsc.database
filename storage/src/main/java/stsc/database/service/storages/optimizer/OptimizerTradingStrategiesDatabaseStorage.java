package stsc.database.service.storages.optimizer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerDoubleArgument;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerDoubleMetric;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerEquityCurveValue;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerExecutionInstance;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerIntegerArgument;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerIntegerMetric;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerMetricsTuple;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerStringArgument;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerSubExecutionArgument;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerTradingStrategy;

public final class OptimizerTradingStrategiesDatabaseStorage {

	private final Dao<OrmliteOptimizerTradingStrategy, Integer> tradingStrategies;
	private final Dao<OrmliteOptimizerExecutionInstance, Integer> executionInstances;
	private final Dao<OrmliteOptimizerStringArgument, Integer> stringArguments;
	private final Dao<OrmliteOptimizerSubExecutionArgument, Integer> subExecutionArguments;
	private final Dao<OrmliteOptimizerIntegerArgument, Integer> integerArguments;
	private final Dao<OrmliteOptimizerDoubleArgument, Integer> doubleArguments;

	private final Dao<OrmliteOptimizerMetricsTuple, Integer> metricsTuples;
	private final Dao<OrmliteOptimizerDoubleMetric, Integer> doubleMetrics;
	private final Dao<OrmliteOptimizerIntegerMetric, Integer> integerMetrics;
	private final Dao<OrmliteOptimizerEquityCurveValue, Integer> equityCurveValues;

	public OptimizerTradingStrategiesDatabaseStorage(final ConnectionSource source) throws IOException, SQLException {
		this.tradingStrategies = DaoManager.createDao(source, OrmliteOptimizerTradingStrategy.class);
		this.executionInstances = DaoManager.createDao(source, OrmliteOptimizerExecutionInstance.class);
		this.stringArguments = DaoManager.createDao(source, OrmliteOptimizerStringArgument.class);
		this.subExecutionArguments = DaoManager.createDao(source, OrmliteOptimizerSubExecutionArgument.class);
		this.integerArguments = DaoManager.createDao(source, OrmliteOptimizerIntegerArgument.class);
		this.doubleArguments = DaoManager.createDao(source, OrmliteOptimizerDoubleArgument.class);

		this.metricsTuples = DaoManager.createDao(source, OrmliteOptimizerMetricsTuple.class);
		this.doubleMetrics = DaoManager.createDao(source, OrmliteOptimizerDoubleMetric.class);
		this.integerMetrics = DaoManager.createDao(source, OrmliteOptimizerIntegerMetric.class);
		this.equityCurveValues = DaoManager.createDao(source, OrmliteOptimizerEquityCurveValue.class);

		Validate.isTrue(tradingStrategies.isTableExists(), "OrmliteOptimizerTradingStrategy table should exists");
		Validate.isTrue(executionInstances.isTableExists(), "OrmliteOptimizerExecutionInstance table should exists");
		Validate.isTrue(stringArguments.isTableExists(), "OrmliteOptimizerStringArguments table should exists");
		Validate.isTrue(subExecutionArguments.isTableExists(), "OrmliteOptimizerSubExecutionArguments table should exists");
		Validate.isTrue(integerArguments.isTableExists(), "OrmliteOptimizerIntegerArguments table should exists");
		Validate.isTrue(doubleArguments.isTableExists(), "OrmliteOptimizerDoubleArguments table should exists");

		Validate.isTrue(metricsTuples.isTableExists(), "OrmliteOptimizerMetricsTuples table should exists");
		Validate.isTrue(doubleMetrics.isTableExists(), "OrmliteOptimizerDoubleMetric table should exists");
		Validate.isTrue(integerMetrics.isTableExists(), "OrmliteOptimizerIntegerMetric table should exists");
		Validate.isTrue(equityCurveValues.isTableExists(), "OrmliteOptimizerEquityCurveValue table should exists");
	}

	public CreateOrUpdateStatus saveTradingStrategy(final OrmliteOptimizerTradingStrategy tradingStrategy) throws SQLException {
		tradingStrategy.setCreatedAt();
		tradingStrategy.setUpdatedAt();
		return tradingStrategies.createOrUpdate(tradingStrategy);
	}

	public List<OrmliteOptimizerTradingStrategy> loadTradingStrategies() throws SQLException {
		return tradingStrategies.queryForAll();
	}

	public OrmliteOptimizerTradingStrategy loadTradingStrategy(Integer id) throws SQLException {
		return tradingStrategies.queryForId(id);
	}

	public CreateOrUpdateStatus saveExecutionInstance(final OrmliteOptimizerExecutionInstance executionInstance) throws SQLException {
		executionInstance.setCreatedAt();
		executionInstance.setUpdatedAt();
		return executionInstances.createOrUpdate(executionInstance);
	}

	public List<OrmliteOptimizerExecutionInstance> loadExecutionInstance(final OrmliteOptimizerTradingStrategy tradingStrategy) throws SQLException {
		return executionInstances.queryForEq("trading_strategy_id", tradingStrategy.getId());
	}

	public CreateOrUpdateStatus saveStringArgument(final OrmliteOptimizerStringArgument stringArgument) throws SQLException {
		stringArgument.setCreatedAt();
		stringArgument.setUpdatedAt();
		return stringArguments.createOrUpdate(stringArgument);
	}

	public List<OrmliteOptimizerStringArgument> loadStringArguments(final OrmliteOptimizerExecutionInstance executionInstance) throws SQLException {
		return stringArguments.queryForEq("execution_instance_id", executionInstance.getId());
	}

	public CreateOrUpdateStatus saveSubExecutionArgument(final OrmliteOptimizerSubExecutionArgument subExecutionArgument) throws SQLException {
		subExecutionArgument.setCreatedAt();
		subExecutionArgument.setUpdatedAt();
		return subExecutionArguments.createOrUpdate(subExecutionArgument);
	}

	public List<OrmliteOptimizerSubExecutionArgument> loadSubExecutionArguments(final OrmliteOptimizerExecutionInstance executionInstance) throws SQLException {
		return subExecutionArguments.queryForEq("execution_instance_id", executionInstance.getId());
	}

	public CreateOrUpdateStatus saveIntegerArgument(final OrmliteOptimizerIntegerArgument integerArgument) throws SQLException {
		integerArgument.setCreatedAt();
		integerArgument.setUpdatedAt();
		return integerArguments.createOrUpdate(integerArgument);
	}

	public List<OrmliteOptimizerIntegerArgument> loadIntegerArguments(final OrmliteOptimizerExecutionInstance executionInstance) throws SQLException {
		return integerArguments.queryForEq("execution_instance_id", executionInstance.getId());
	}

	public CreateOrUpdateStatus saveDoubleArgument(final OrmliteOptimizerDoubleArgument doubleArgument) throws SQLException {
		doubleArgument.setCreatedAt();
		doubleArgument.setUpdatedAt();
		return doubleArguments.createOrUpdate(doubleArgument);
	}

	public List<OrmliteOptimizerDoubleArgument> loadDoubleArguments(final OrmliteOptimizerExecutionInstance executionInstance) throws SQLException {
		return doubleArguments.queryForEq("execution_instance_id", executionInstance.getId());
	}

	public CreateOrUpdateStatus saveMetricsTuple(final OrmliteOptimizerMetricsTuple metricsTuple) throws SQLException {
		metricsTuple.setCreatedAt();
		metricsTuple.setUpdatedAt();
		return metricsTuples.createOrUpdate(metricsTuple);
	}

	public List<OrmliteOptimizerMetricsTuple> loadMetricsTuples(final OrmliteOptimizerTradingStrategy tradingStrategy) throws SQLException {
		return metricsTuples.queryForEq("trading_strategy_id", tradingStrategy.getId());
	}

	public CreateOrUpdateStatus saveDoubleMetric(final OrmliteOptimizerDoubleMetric doubleMetric) throws SQLException {
		doubleMetric.setCreatedAt();
		doubleMetric.setUpdatedAt();
		return doubleMetrics.createOrUpdate(doubleMetric);
	}

	public List<OrmliteOptimizerDoubleMetric> loadDoubleMetrics(final OrmliteOptimizerMetricsTuple metricsTuple) throws SQLException {
		return doubleMetrics.queryForEq("metrics_tuple_id", metricsTuple.getId());
	}

	public CreateOrUpdateStatus saveIntegerMetric(final OrmliteOptimizerIntegerMetric integerMetric) throws SQLException {
		integerMetric.setCreatedAt();
		integerMetric.setUpdatedAt();
		return integerMetrics.createOrUpdate(integerMetric);
	}

	public List<OrmliteOptimizerIntegerMetric> loadIntegerMetrics(final OrmliteOptimizerMetricsTuple metricsTuple) throws SQLException {
		return integerMetrics.queryForEq("metrics_tuple_id", metricsTuple.getId());
	}

	public CreateOrUpdateStatus saveEquityCurveValue(final OrmliteOptimizerEquityCurveValue equityCurveValue) throws SQLException {
		equityCurveValue.setCreatedAt();
		equityCurveValue.setUpdatedAt();
		return equityCurveValues.createOrUpdate(equityCurveValue);
	}

	public List<OrmliteOptimizerEquityCurveValue> loadEquityCurveValues(final OrmliteOptimizerMetricsTuple metricsTuple) throws SQLException {
		final QueryBuilder<OrmliteOptimizerEquityCurveValue, Integer> queryBuilder = equityCurveValues.queryBuilder();
		queryBuilder.setWhere(equityCurveValues.queryBuilder(). //
				where().eq("metrics_tuple_id", metricsTuple.getId()));
		queryBuilder.orderBy("timestamp", true);
		return equityCurveValues.query(queryBuilder.prepare());
	}

}
