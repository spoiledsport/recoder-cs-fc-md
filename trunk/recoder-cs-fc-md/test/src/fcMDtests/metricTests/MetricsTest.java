package fcMDtests.metricTests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import metricsdata.AbstractMetricAttribute;

import org.apache.log4j.Logger;

import recodercs.CrossReferenceServiceConfiguration;
import recodercs.service.CrossReferenceSourceInfo;

/**
 * All testcases for metrics inherit from this class. It provides
 * CrossReferenceSourceInfo and CrossReferenceServiceConfiguration to the
 * testcases. Furthermore it provides methods to extract the expected results
 * for a metric from it's testFile.
 * 
 * all metric unit tests are parametrized unit tests, where the getTestFiles()
 * method supplies the files to test. The Input directory of the test files is
 * defined in the attribute section of the class. Each test goes like this: 1)
 * method initRecoder() creates metrics to calculate and stores result of metric
 * calculation. 2) actual test method test() is run. 2.1) expected metric
 * results are extracted from test file. 2.2) next we iterate through expected
 * results and assert that the expected results equal the calculated results.
 * 
 */
public class MetricsTest {
	CrossReferenceServiceConfiguration cs;
	CrossReferenceSourceInfo si;

	HashMap<String, HashMap<String, AbstractMetricAttribute>> resultSet = null;

	String testFile;

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(MetricsTest.class);

	/**
	 * opens a file and returns its contents as a String.
	 * 
	 * @param filePath
	 *            the name of the file to open.
	 * @return hm a HashMap with metric names and expected values for them
	 */
	protected HashMap<String, HashMap<String, String>> extractMetrics() {
		String fileContent = "";
		try {
			fileContent = readFileAsString(testFile);
		} catch (IOException e) {
			log.error("Could not read file with test code. statck trace: "
					+ e.getStackTrace());
		}

		// extract expected metric values section
		// TODO: do this better
		String extracted = fileContent.substring(fileContent
				.indexOf("<EXPECTED_METRICS>") + 18, fileContent
				.indexOf("</EXPECTED_METRICS>"));
		log.debug("Extracted metrics:" + extracted);

		assert extracted != "";

		// split lines
		String[] lines = extracted.split("[\\r\\n]+");

		HashMap<String, HashMap<String, String>> hm = new HashMap<String, HashMap<String, String>>();

		// loop over all lines, split between key:value and put them in a hash
		// map
		HashMap<String, String> tempMap = new HashMap<String, String>();
		for (int i = 0; i < lines.length; i++) {
			if (!lines[i].equals("")) {
				String[] keyValue = lines[i].split(":");
				tempMap.put(keyValue[0], keyValue[1]);
			}
		}

		// later we are going to use the file name in the hasmep to match the
		// expected to the calculated results. filename for calculated resluts
		// starts with 'FILE:'
		this.testFile = "FILE:" + testFile;

		hm.put(testFile, tempMap);
		return hm;
	}

	/**
	 * opens a file and returns its contents as a String.
	 * 
	 * @param filePath
	 *            the name of the file to open.
	 * @return String the contents of filePath as a String
	 */
	private static String readFileAsString(String filePath)
			throws java.io.IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			fileData.append(buf, 0, numRead);
		}
		reader.close();
		return fileData.toString();
	}

}
