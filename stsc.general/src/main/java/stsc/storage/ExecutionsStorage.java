package stsc.storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import stsc.algorithms.BadAlgorithmException;
import stsc.algorithms.EodAlgorithm;
import stsc.algorithms.EodExecution;
import stsc.algorithms.StockAlgorithm;
import stsc.algorithms.StockExecution;
import stsc.common.Day;
import stsc.signals.BadSignalException;
import stsc.trading.Broker;

public class ExecutionsStorage {

	private static class StockAlgorithms {
		// execution name to stock algorithms
		private final HashMap<String, StockAlgorithm> map = new HashMap<>();
		private final ArrayList<StockAlgorithm> orderedAlgorithms = new ArrayList<>();

		void add(final String executionName, final StockAlgorithm algo) {
			if (map.containsKey(executionName))
				return;
			orderedAlgorithms.add(algo);
			map.put(executionName, algo);
		}

		void simulate(final Day newDay) throws BadSignalException {
			for (StockAlgorithm algo : orderedAlgorithms) {
				algo.process(newDay);
			}
		}

		int size() {
			return orderedAlgorithms.size();
		}

	}

	private class StockNameToAlgorithms {
		// stock name to execution map
		private HashMap<String, StockAlgorithms> stockToAlgorithm = new HashMap<>();

		void addExecutionOnStock(String stockName, String executionName, StockAlgorithm algo) {
			StockAlgorithms se = stockToAlgorithm.get(stockName);
			if (se == null) {
				se = new StockAlgorithms();
				stockToAlgorithm.put(stockName, se);
			}
			se.add(executionName, algo);
		}

		void simulate(String stockName, final Day newDay) throws BadSignalException {
			StockAlgorithms e = stockToAlgorithm.get(stockName);
			if (e != null)
				e.simulate(newDay);
		}

		int size() {
			return stockToAlgorithm.size();
		}

	}

	final SignalsStorage signalsStorage = new SignalsStorage();

	private List<StockExecution> stockExecutions = new ArrayList<>();
	private List<EodExecution> eodExecutions = new ArrayList<>();

	private StockNameToAlgorithms stockAlgorithms = new StockNameToAlgorithms();
	private Map<String, EodAlgorithm> tradeAlgorithms = new HashMap<>();

	public ExecutionsStorage() {
	}

	public void addStockExecution(StockExecution execution) throws BadAlgorithmException {
		stockExecutions.add(execution);
	}

	public void addEodExecution(EodExecution execution) throws BadAlgorithmException {
		eodExecutions.add(execution);
	}

	public void initialize(Broker broker) throws BadAlgorithmException {
		final StockStorage stocks = broker.getStockStorage();
		for (StockExecution execution : stockExecutions) {
			for (String stockName : stocks.getStockNames()) {
				final StockAlgorithm algo = execution.getInstance(stockName, signalsStorage);
				stockAlgorithms.addExecutionOnStock(stockName, execution.getName(), algo);
			}
		}
		for (EodExecution execution : eodExecutions) {
			final EodAlgorithm algo = execution.getInstance(broker, signalsStorage);
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

	public int getEodAlgorithmsSize() {
		return tradeAlgorithms.size();
	}

	public EodAlgorithm getEodAlgorithm(final String key) {
		return tradeAlgorithms.get(key);
	}

	public Set<String> getEodAlgorithmNames() {
		return tradeAlgorithms.keySet();
	}

	public int getStockAlgorithmsSize() {
		if (stockAlgorithms.stockToAlgorithm.isEmpty())
			return 0;
		else
			return stockAlgorithms.stockToAlgorithm.entrySet().iterator().next().getValue().size();
	}

	public StockAlgorithm getStockAlgorithm(final String executionName, final String stockName) {
		StockAlgorithms e = stockAlgorithms.stockToAlgorithm.get(stockName);
		if (e != null)
			return e.map.get(executionName);
		return null;
	}

	public SignalsStorage getSignalsStorage() {
		return signalsStorage;
	}

	@Override
	public String toString() {
		return "Stocks: " + Integer.toString(stockExecutions.size()) + " EodAlgos: "
				+ Integer.toString(tradeAlgorithms.size()) + " StockAlgos:" + Integer.toString(stockAlgorithms.size());
	}

	public String stringHashCode() {
		final StringBuilder sb = new StringBuilder();
		for (StockExecution se : stockExecutions) {
			se.stringHashCode(sb);
		}
		for (EodExecution ee : eodExecutions) {
			ee.stringHashCode(sb);
		}
		return sb.toString();
	}
}