package fcMDtests.metricTests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import metrics.PlainAnalysisErrorHandler;
import metricsdata.AbstractMetricAttribute;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;

import recoder.CrossReferenceServiceConfiguration;
import recoder.service.CrossReferenceSourceInfo;


public class MetricsTest {
	CrossReferenceServiceConfiguration cs;
	CrossReferenceSourceInfo si;

	HashMap<String, HashMap<String, AbstractMetricAttribute>> resultSet =  null;
	
	String testFile;

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(MetricsTest.class);

	@BeforeClass
	public static void setUp() {

		
		String s = System.getProperty("file.separator");
		System
				.getProperties()
				.put(
						"input.path",
						"test" + s + "metricsTestDate" + s + "DS_LOCC");
						//"test" + s + "minicorlib:test" + s + "metricsTestDate" + s + "DS_LOCC");

	}

	protected HashMap<String, HashMap<String,String>> extractMetrics() {
		String fileContent = "";
		try {
			fileContent = readFileAsString(testFile);
		} catch (IOException e) {
			log.error("Could not read file with test code. statck trace: "
					+ e.getStackTrace());
		}

		// extract expected metric values section
		String extracted = fileContent.substring(fileContent
				.indexOf("<EXPECTED_METRICS>") + 18, fileContent
				.indexOf("</EXPECTED_METRICS>"));
		log.debug("Extracted metrics:" + extracted);

		assert extracted != "";

		// split lines
		String[] lines = extracted.split("[\\r\\n]+");

		HashMap<String, HashMap<String,String>> hm = new HashMap<String, HashMap<String,String>>();

		// loop over all lines, split between key:value and put them in a hash
		// map
		HashMap<String, String> tempMap = new HashMap<String, String>();
		for (int i = 0; i < lines.length; i++) {
			if (!lines[i].equals("")) {
				String[] keyValue = lines[i].split(":");
				tempMap.put(keyValue[0], keyValue[1]);
			}
		}
		return hm;
	}

	/**
	 * @param filePath
	 *            the name of the file to open. Not sure if it can accept URLs
	 *            or just filenames. Path handling could be better, and buffer
	 *            sizes are hardcoded
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

