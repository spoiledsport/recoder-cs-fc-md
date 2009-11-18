package metrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import metrics.metrics.ATFD;
import metrics.metrics.CC;
import metrics.metrics.DSMetricCalculator;
import metrics.metrics.LOCC;
import metrics.metrics.LOCM;
import metrics.metrics.TCC;
import metrics.metrics.WMC;
import metrics.util.MetricUtils;
import metricsdata.AbstractMetricAttribute;
import recoder.CrossReferenceServiceConfiguration;
import recoder.ParserException;
import recoder.csharp.CompilationUnit;
import recoder.csharp.ProgramElement;
import recoder.list.CompilationUnitList;
import recoder.service.CrossReferenceSourceInfo;

/**
 * @author AL
 */
public class MetricsFramework {

	CrossReferenceServiceConfiguration cs;
	CrossReferenceSourceInfo si;
	CompilationUnitList units;

	// resultSet, that holds results of all metric calculations
	HashMap<String, HashMap<String, AbstractMetricAttribute>> resultSet = new HashMap<String, HashMap<String, AbstractMetricAttribute>>();

	ArrayList<DSMetricCalculator> metricsCalc = null;

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(MetricsFramework.class);

	public MetricsFramework(ArrayList<DSMetricCalculator> metrics, String[] args) {
		// init the metrics that are to be calculated
		this.metricsCalc = metrics;

		// set input path
		String inputPath = "";
		for (String arg : args) {
			if (inputPath.equals(""))
				inputPath = inputPath + arg;
			else
				inputPath = inputPath + System.getProperty("path.separator")  + arg;
		}

		// make sure input-path is not empty
		assert !inputPath.equals("");

		// set input path with the files to be analyzed
		System.getProperties().put("input.path", inputPath);

		// setup cross reference service configuration and source info
		this.cs = new CrossReferenceServiceConfiguration();
		this.si = cs.getCrossReferenceSourceInfo();

		// set custom error handler
		this.cs.getProjectSettings().setErrorHandler(
				new PlainAnalysisErrorHandler(3));

		// run recoder program
		RecoderProgram.setup(cs, MetricsFramework.class, args);

		// get compilation units
		this.units = cs.getSourceFileRepository().getCompilationUnits();
	}

	public static void main(String[] args) throws IOException, ParserException,
			Exception {

		String[] myArgs = new String[] { "test/minicorlib",
				"test/personExp" };

		ArrayList<DSMetricCalculator> myMetrics = new ArrayList<DSMetricCalculator>();

		// create metrics to be calculated
		myMetrics.add(new LOCC());
		myMetrics.add(new LOCM());
		myMetrics.add(new WMC());
		myMetrics.add(new ATFD());
		myMetrics.add(new TCC());
		myMetrics.add(new CC());

		MetricsFramework rt = new MetricsFramework(myMetrics, myArgs);
		rt.applyMetric();
	}

	public HashMap<String, HashMap<String, AbstractMetricAttribute>> applyMetric() {

		// init metrics
		createMatrics(si);

		// loop over all cus
		for (int i = 0, s = this.units.size(); i < s; i += 1) {
			// resultSet for the current CU
			HashMap<String, AbstractMetricAttribute> cuRes = new HashMap<String, AbstractMetricAttribute>();

			// get next compilation unit
			CompilationUnit cu = this.units.getCompilationUnit(i);

			// collect class types from cu
			ArrayList<ProgramElement> cuTypes = MetricUtils.collectClasses(cu);

			// set CUs in metrics
			setMetricCu(cuTypes);

			// calculate metrics
			calculateMetrics();

			// add metric results for current CU
			getMetricsResult(cuRes);

			// put metric results for current CU in resultSet
			resultSet.put(cu.getName(), cuRes);
		}
		log.debug(metrics.util.MetricUtils.debugOutput(resultSet));
		return resultSet;
	}

	private void getMetricsResult(HashMap<String, AbstractMetricAttribute> cuRes) {
		for (DSMetricCalculator metric : this.metricsCalc) {
			cuRes.put(metric.getShortcut(), metric.getResult());
		}
	}

	private void calculateMetrics() {
		for (DSMetricCalculator metric : this.metricsCalc) {
			metric.calculate();
		}
	}

	private void setMetricCu(ArrayList<ProgramElement> cuTypes) {
		for (DSMetricCalculator metric : this.metricsCalc) {
			metric.setTypes(cuTypes);
		}
	}

	private void createMatrics(CrossReferenceSourceInfo si2) {
		for (DSMetricCalculator metric : this.metricsCalc) {
			metric.setSi(si2);
		}
	}

}
