package stsc.general.statistic.cost.function;

import java.text.ParseException;

import stsc.common.Settings;
import stsc.general.statistic.Statistics;
import stsc.general.testhelper.TestStatisticsHelper;
import junit.framework.TestCase;

public class CostWeightedSumFunctionTest extends TestCase {
	public void testCostWeightedSumFunction() throws ParseException {
		final Statistics statistics = TestStatisticsHelper.getStatistics();

		final CostWeightedSumFunction function = new CostWeightedSumFunction();
		final Double result = function.calculate(statistics);
		assertEquals(-0.389662, result, Settings.doubleEpsilon);

		function.addParameter("getPeriod", 0.5);
		final Double result2 = function.calculate(statistics);
		assertEquals(0.610337, result2, Settings.doubleEpsilon);

		function.addParameter("getKelly", 0.3);
		final Double result3 = function.calculate(statistics);
		assertEquals(0.610337, result3, Settings.doubleEpsilon);

		function.addParameter("getMaxLoss", 0.7);
		final Double result4 = function.calculate(statistics);
		assertEquals(127.310337, result4, Settings.doubleEpsilon);
	}
}
