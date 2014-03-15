package stsc.storage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stsc.algorithms.AlgorithmSettings;
import stsc.algorithms.BadAlgorithmException;
import stsc.algorithms.EodAlgorithm;
import stsc.algorithms.EodAlgorithmExecution;
import stsc.algorithms.StockAlgorithm;
import stsc.algorithms.StockAlgorithmExecution;
import stsc.common.Day;
import stsc.trading.Broker;

public class ExecutionsStorage {

	private static class Executions {
		// execution name to stock algorithms
		private final HashMap<String, StockAlgorithm> map = new HashMap<>();

		void add( final String executionName, final StockAlgorithm algo) {
			map.put(executionName, algo);
		}
		
		void simulate(final Day newDay) throws BadSignalException {
			for (Map.Entry<String, StockAlgorithm> sPair : map.entrySet()) {
				sPair.getValue().process(newDay);
			}
		}
	}

	private class StockExecutions {
		// stock name to execution map
		private HashMap<String, Executions> stockToExecution = new HashMap<>();

		void addExecutionOnStock(String stockName, String executionName, StockAlgorithm algo) {
			Executions se = stockToExecution.get(stockName);
			if (se == null) {
				se = new Executions();
				stockToExecution.put(stockName, se);
			}
			se.add(executionName, algo);
		}

		void simulate(String stockName, final Day newDay) throws BadSignalException {
			Executions e = stockToExecution.get(stockName);
			if (e != null)
				e.simulate(newDay);
		}
	}

	private StockExecutions stockAlgorithms = new StockExecutions();
	public HashMap<String, EodAlgorithm> tradeAlgorithms = new HashMap<>();

	public ExecutionsStorage(final List<StockAlgorithmExecution> stockExecutions,
			final List<EodAlgorithmExecution> eodExecutions, final List<String> stocks, final Broker broker,
			final SignalsStorage signals) throws BadAlgorithmException {

		for (StockAlgorithmExecution execution : stockExecutions) {
			for (String stockName : stocks) {
				StockAlgorithm algo = execution.getInstance(stockName, signals, new AlgorithmSettings());
				stockAlgorithms.addExecutionOnStock(stockName, execution.getName(), algo);
			}
		}
		for (EodAlgorithmExecution execution : eodExecutions) {
			EodAlgorithm algo = execution.getInstance(broker, signals, new AlgorithmSettings());
			tradeAlgorithms.put(execution.getName(), algo);
		}
	}

	public void runStockAlgorithms(final String stockName, final Day stockDay) throws BadSignalException {
		stockAlgorithms.simulate(stockName, stockDay);
	}

	public void runEodAlgorithms(final Date today, final HashMap<String, Day> datafeed) throws BadSignalException {
		for (Map.Entry<String, EodAlgorithm> i : tradeAlgorithms.entrySet()) {
			i.getValue().process(today, datafeed);
		}
	}
}