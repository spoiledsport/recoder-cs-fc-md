package metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import metrics.metrics.ATFD;
import metrics.metrics.CC;
import metrics.metrics.CDISP;
import metrics.metrics.CINT;
import metrics.metrics.CM;
import metrics.metrics.CYCLO;
import metrics.metrics.DSMetricCalculator;
import metrics.metrics.LOCC;
import metrics.metrics.LOCM;
import metrics.metrics.MAXNESTING;
import metrics.metrics.NOAM;
import metrics.metrics.NOAV;
import metrics.metrics.NOPA;
import metrics.metrics.TCC;
import metrics.metrics.WMC;
import metrics.metrics.WOC;
import metrics.util.MetricUtils;
import metricsdata.AbstractMetricAttribute;
import recodercs.CrossReferenceServiceConfiguration;
import recodercs.ParserException;
import recodercs.csharp.CompilationUnit;
import recodercs.csharp.ProgramElement;
import recodercs.list.CompilationUnitList;
import recodercs.service.CrossReferenceSourceInfo;

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

	/**
	 * Main class' constructor responsible for setting up the metric calculation.
	 * @param metrics
	 *        A list of {@link DSMetricCalculator} that should be applied.
	 * @param args
	 *        An array of directories (given as String) that should be searched for source code files.
	 */
	public MetricsFramework(ArrayList<DSMetricCalculator> metrics, String[] args) {
		// init the metrics that are to be calculated
		this.metricsCalc = metrics;

		// set input path
		String inputPath = "";
		for (String arg : args) {
			if (inputPath.equals(""))
				inputPath = inputPath + arg;
			else
				inputPath = inputPath + System.getProperty("path.separator")
						+ arg;
		}

		// make sure input-path is not empty
		assert !inputPath.equals("");
		
		// HACK
		inputPath += System.getProperty("path.separator")+"../recoder-cs-fc-md/test"+System.getProperty("file.separator")+"minicorlib";

		// set input path with the files to be analyzed
		System.getProperties().put("input.path", inputPath);

		// setup cross reference service configuration and source info
		this.cs = new CrossReferenceServiceConfiguration();
		this.si = cs.getCrossReferenceSourceInfo();

		// set custom error handler
		this.cs.getProjectSettings().setErrorHandler(
				new PlainAnalysisErrorHandler(6666));

		// run recoder program
		RecoderProgram.setup(cs, MetricsFramework.class, args);

		// get compilation units
		this.units = cs.getSourceFileRepository().getCompilationUnits();
	}

	/**
	 * Main method.
	 * @param args
	 *        First argument is the path of the csv file. 
	 *        Second and following arguments are the paths that should be searched for source code files.
	 * @throws IOException
	 * @throws ParserException
	 * @throws Exception
	 */
	public static void main(String[] args) throws IOException, ParserException,
			Exception {


		if(args.length<2){
			System.err.println("Main method arguments invalid. See javadoc of main method.");
			return;
		}
		
		//first argument: path of csvFile
		String csvFile = args[0];
		
		//Following the directories that should be searched for source files
		String[] myArgs = new String[args.length-1];
		for(int i=1;i<args.length;i++){
			myArgs[i-1]=args[i];
		}
		
//		String[] myArgs = new String[] { "test\\minicorlib",
//				"C:\\Users\\jschumacher\\Documents\\My Dropbox\\WORK\\fc-md\\wsp\\keymind\\ana" };


		// String[] myArgs = new String[] { "test\\personExp",
		// "test\\minicorlib" };

		ArrayList<DSMetricCalculator> myMetrics = new ArrayList<DSMetricCalculator>();


		 //create metrics to be calculated
		 myMetrics.add(new LOCC());
		 myMetrics.add(new LOCM());
		 myMetrics.add(new WMC());
		 myMetrics.add(new ATFD());
		 myMetrics.add(new TCC());
		 myMetrics.add(new CC());
		 myMetrics.add(new CM());
		 myMetrics.add(new CYCLO());
		 myMetrics.add(new MAXNESTING());
		 myMetrics.add(new NOAM());
		 myMetrics.add(new NOAV());
		 myMetrics.add(new NOPA());
		 myMetrics.add(new WOC());


		MetricsFramework rt = new MetricsFramework(myMetrics, myArgs);
		//rt.applyMetric("C:\\Users\\jschumacher\\Desktop\\metrics.csv");
		rt.applyMetric(csvFile);
	}

	public HashMap<String, HashMap<String, AbstractMetricAttribute>> applyMetric(String csvOutput) {

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
		//log.debug(metrics.util.MetricUtils.debugOutput(resultSet));

		if (csvOutput.equals(""))
			return resultSet;

		File csfFile = new File(csvOutput);
		
		try {
			// rsults, outputFile, onlyMaximumValue?
			MetricUtils.csvOutput(resultSet, csfFile, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultSet;
	}
	
	public HashMap<String, HashMap<String, AbstractMetricAttribute>> applyMetric() {
		return this.applyMetric("");
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
