package stsc.database.service.storages.optimizer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerExecutionInstance;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerIntegerArgument;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerStringArgument;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerSubExecutionArgument;
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerTradingStrategy;

public final class OptimizerTradingStrategiesDatabaseStorage {

	private final Dao<OrmliteOptimizerTradingStrategy, Integer> tradingStrategies;
	private final Dao<OrmliteOptimizerExecutionInstance, Integer> executionInstances;
	private final Dao<OrmliteOptimizerStringArgument, Integer> stringArguments;
	private final Dao<OrmliteOptimizerSubExecutionArgument, Integer> subExecutionArguments;
	private final Dao<OrmliteOptimizerIntegerArgument, Integer> integerArguments;

	public OptimizerTradingStrategiesDatabaseStorage(final ConnectionSource source) throws IOException, SQLException {
		this.tradingStrategies = DaoManager.createDao(source, OrmliteOptimizerTradingStrategy.class);
		this.executionInstances = DaoManager.createDao(source, OrmliteOptimizerExecutionInstance.class);
		this.stringArguments = DaoManager.createDao(source, OrmliteOptimizerStringArgument.class);
		this.subExecutionArguments = DaoManager.createDao(source, OrmliteOptimizerSubExecutionArgument.class);
		this.integerArguments = DaoManager.createDao(source, OrmliteOptimizerIntegerArgument.class);
		Validate.isTrue(tradingStrategies.isTableExists(), "OrmliteOptimizerTradingStrategy table should exists");
		Validate.isTrue(executionInstances.isTableExists(), "OrmliteOptimizerExecutionInstance table should exists");
		Validate.isTrue(stringArguments.isTableExists(), "OrmliteOptimizerStringArguments table should exists");
		Validate.isTrue(subExecutionArguments.isTableExists(), "OrmliteOptimizerSubExecutionArguments table should exists");
		Validate.isTrue(integerArguments.isTableExists(), "OrmliteOptimizerIntegerArguments table should exists");
	}

	public CreateOrUpdateStatus saveTradingStrategy(final OrmliteOptimizerTradingStrategy tradingStrategy) throws SQLException {
		tradingStrategy.setCreatedAt();
		tradingStrategy.setUpdatedAt();
		return tradingStrategies.createOrUpdate(tradingStrategy);
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

}
