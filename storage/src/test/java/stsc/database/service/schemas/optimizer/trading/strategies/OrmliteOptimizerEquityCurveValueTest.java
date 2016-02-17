package stsc.database.service.schemas.optimizer.trading.strategies;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import liquibase.exception.LiquibaseException;
import stsc.common.Day;
import stsc.common.Settings;
import stsc.database.migrations.optimizer.OptimizerDatabaseSettings;
import stsc.database.service.storages.optimizer.OptimizerTradingStrategiesDatabaseStorage;

public class OrmliteOptimizerEquityCurveValueTest {

	@Test
	public void testOrmliteOptimizerEquityCurveValue() throws SQLException, LiquibaseException, IOException, ParseException {
		final OptimizerDatabaseSettings settings = OptimizerDatabaseSettings.test().dropAll().migrate();
		final ConnectionSource source = new JdbcConnectionSource(settings.getJdbcUrl(), settings.getLogin(), settings.getPassword());
		final OptimizerTradingStrategiesDatabaseStorage storage = new OptimizerTradingStrategiesDatabaseStorage(source);
		Assert.assertNotNull(storage);
		{
			final OrmliteOptimizerTradingStrategy tradingStrategy = new OrmliteOptimizerTradingStrategy();
			Assert.assertEquals(1, storage.saveTradingStrategy(tradingStrategy).getNumLinesChanged());

			final OrmliteOptimizerMetricsTuple metricsTuple = new OrmliteOptimizerMetricsTuple(tradingStrategy.getId());
			Assert.assertEquals(1, storage.saveMetricsTuple(metricsTuple).getNumLinesChanged());

			final OrmliteOptimizerEquityCurveValue ecv1 = new OrmliteOptimizerEquityCurveValue(metricsTuple.getId());
			ecv1.setTimestamp(Day.createDate("24-07-1987"));
			ecv1.setValue(563.72);
			Assert.assertEquals(1, storage.saveEquityCurveValue(ecv1).getNumLinesChanged());
			
			final OrmliteOptimizerEquityCurveValue ecv2 = new OrmliteOptimizerEquityCurveValue(metricsTuple.getId());
			ecv2.setTimestamp(Day.createDate("22-07-1987"));
			ecv2.setValue(76.3);
			Assert.assertEquals(1, storage.saveEquityCurveValue(ecv2).getNumLinesChanged());
		}
		{
			final OrmliteOptimizerTradingStrategy copy = storage.loadTradingStrategy(1);
			final OrmliteOptimizerMetricsTuple metricsTuple = storage.loadMetricsTuples(copy).get(0);
			Assert.assertNotNull(metricsTuple);
			final List<OrmliteOptimizerEquityCurveValue> ecvs = storage.loadEquityCurveValues(metricsTuple);
			
			Assert.assertEquals(76.3, ecvs.get(0).getValue(), Settings.doubleEpsilon);
			Assert.assertEquals(563.72, ecvs.get(1).getValue(), Settings.doubleEpsilon);
		}
		settings.dropAll();
	}

}
