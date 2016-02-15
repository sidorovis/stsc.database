package stsc.database.service.storages.optimizer;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.lang3.Validate;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import stsc.database.service.schemas.optimizer.trading.strategies.OrmliteOptimizerTradingStrategy;

public final class OptimizerTradingStrategiesDatabaseStorage {

	private final Dao<OrmliteOptimizerTradingStrategy, Integer> tradingStrategies;

	public OptimizerTradingStrategiesDatabaseStorage(final ConnectionSource source) throws IOException, SQLException {
		this.tradingStrategies = DaoManager.createDao(source, OrmliteOptimizerTradingStrategy.class);
		Validate.isTrue(tradingStrategies.isTableExists(), "OrmliteOptimizerTradingStrategy table should exists");
	}

	public CreateOrUpdateStatus saveTradingStrategy(final OrmliteOptimizerTradingStrategy tradingStrategy) throws SQLException {
		tradingStrategy.setCreatedAt();
		tradingStrategy.setUpdatedAt();
		return tradingStrategies.createOrUpdate(tradingStrategy);
	}

	public OrmliteOptimizerTradingStrategy loadTradingStrategy(Integer id) throws SQLException {
		return tradingStrategies.queryForId(id);
	}

}
