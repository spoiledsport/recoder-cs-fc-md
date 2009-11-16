package fcMDtests.metricTests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import metrics.MetricsFramework;
import metrics.metrics.CM;
import metrics.metrics.DSMetricCalculator;
import metricsdata.AbstractMetricAttribute;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import recoder.ParserException;
import fcMDtests.util.FileSlurper;

/**
 * unit test for metric LOOC
 */
@RunWith(value = Parameterized.class)
public class DS_CM_Test extends MetricsTest {

	private MetricsFramework mf;

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(DS_CM_Test.class);

	// configuration options
	static String testDir = "test" + System.getProperty("file.separator")
			+ "metricsTestData" + System.getProperty("file.separator")
			+ "DS_CM";
	static String regex = ".*cs$";;

	public DS_CM_Test(String testFile) {
		this.testFile = testFile;
	}

	/**
	 * The method that supplies the files for the parametrized test case
	 * {@link #test()}. It uses a {@link FileSlurper} to fill the field testFile
	 * with the file to be tested.
	 */
	@Parameters
	public static LinkedList<String[]> getTestFiles() {
		File[] testFiles = null;
		LinkedList<String[]> params = new LinkedList<String[]>();
		try {
			// get list of files matching regEx
			testFiles = new FileSlurper(testDir, regex).slurp();
			for (File f : testFiles) {
				// add files to the list used for the parametrized test
				params.add(new String[] { f.getPath() });
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return params;
	}

	@Before
	public void initRecoder() {
		ArrayList<DSMetricCalculator> metrics = new ArrayList<DSMetricCalculator>();
		metrics.add(new CM());
		// we are using strings so we need the minicorlib
		String miniCorLib = "test" + System.getProperty("file.separator") + "minicorlib";
		this.mf = new MetricsFramework(metrics, new String[] { testDir, miniCorLib });
		resultSet = mf.applyMetric();
	}

	/**
	 * The paremetrized test case itself. It is data driven and gets the files
	 * to parse from {@link #getTestFiles()}.
	 * 
	 * @throws ParserException
	 */
	@Test
	public void test() throws IOException, ParserException {
		// get expected metric results
		HashMap<String, HashMap<String, String>> expected = extractMetrics();

		// iterate over keys in expected results
		Set<String> expKeys = expected.keySet();
		Iterator<String> expKeysItr = expKeys.iterator();
		while (expKeysItr.hasNext()) {
			String file = expKeysItr.next();

			// get hashmap with expeted results and calculated results for
			// hashmap
			HashMap<String, String> expectedValues = expected.get(file);
			HashMap<String, AbstractMetricAttribute> actualResults = resultSet.get(file);
			
			// iterate over expected metrics keys
			Set<String> metricKeys = expectedValues.keySet();
			Iterator<String> expectedValuesIts = metricKeys.iterator();
			while (expectedValuesIts.hasNext()) {
				String metricName = expectedValuesIts.next();
				
				assertEquals(expectedValues.get(metricName), actualResults.get(metricName).toString());
			}
		}
	}
}
