package metrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import metrics.metrics.LOCC;
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
	HashMap<String, HashMap<String, AbstractMetricAttribute>> resultSet = new HashMap<String, HashMap<String,AbstractMetricAttribute>>();
	
	// metrics
	LOCC locc = new LOCC();

	protected MetricsFramework(String[] args) {
		System
				.getProperties()
				.put(
						"input.path",
						"test/minicorlib:/Users/janschumacher/Dropbox/WORK/fc-md/wsp/recoder-cs-fc-md/test/personExp");

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
		MetricsFramework rt = new MetricsFramework(args);
		rt.applyMetric();
	}

	public void applyMetric() {
		
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
		debugOutput(resultSet);
	}
	
	private void getMetricsResult(HashMap<String, AbstractMetricAttribute> cuRes) {
		cuRes.put(locc.getShortcut(), locc.getResult());
		
	}

	private void calculateMetrics() {
		locc.calculate();
		
	}

	private void setMetricCu(ArrayList<ProgramElement> cuTypes) {
		locc.setTypes(cuTypes);
		
	}

	private void createMatrics(CrossReferenceSourceInfo si2) {
		this.locc.setSi(si2);
		
	}

	/**
	 * Debug and Test Output of the metric results (mainly for stand-alone usage) 
	 */    
    public void debugOutput(HashMap<String, HashMap<String, AbstractMetricAttribute>> metricResults) {
    	for(String cuName : metricResults.keySet()) {
    		System.out.println("\nCalculated results for class: "+cuName);
    		for(String metricName : metricResults.get(cuName).keySet()) {
    			System.out.println(metricName +": "+ metricResults.get(cuName).get(metricName));
    		}
    	}
    }

}
