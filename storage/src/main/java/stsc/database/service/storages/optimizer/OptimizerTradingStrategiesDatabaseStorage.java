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
import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerTradingStrategy;

public final class OptimizerTradingStrategiesDatabaseStorage {

	private final Dao<OrmliteOptimizerTradingStrategy, Integer> tradingStrategies;
	private final Dao<OrmliteOptimizerExecutionInstance, Integer> executionInstances;

	public OptimizerTradingStrategiesDatabaseStorage(final ConnectionSource source) throws IOException, SQLException {
		this.tradingStrategies = DaoManager.createDao(source, OrmliteOptimizerTradingStrategy.class);
		this.executionInstances = DaoManager.createDao(source, OrmliteOptimizerExecutionInstance.class);
		Validate.isTrue(tradingStrategies.isTableExists(), "OrmliteOptimizerTradingStrategy table should exists");
		Validate.isTrue(executionInstances.isTableExists(), "OrmliteOptimizerExecutionInstance table should exists");
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

}
