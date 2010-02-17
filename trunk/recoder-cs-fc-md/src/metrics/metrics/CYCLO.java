package metrics.metrics;

import java.util.ArrayList;

import metrics.util.Filters;
import metrics.util.MetricUtils;
import metricsdata.IntegerArray2ValueMetric;

import org.apache.log4j.Logger;

import recodercs.abstraction.ClassType;
import recodercs.abstraction.Method;
import recodercs.convenience.TreeWalker;
import recodercs.csharp.ProgramElement;
import recodercs.service.CrossReferenceSourceInfo;

/**
 * A metric that calculates the lines of code per class.
 */
public class CYCLO extends DSMetricCalculator {

	//private IntegerArrayValueMetric result;
	
	/**
	 * Constructur
	 * 
	 * @param types an ArrayList of CompilationUnits to calculate
	 * @param si the CrossReferenceSourceInfo
	 */
	public CYCLO(ArrayList<ProgramElement> types, CrossReferenceSourceInfo si) {
		this.si = si;
		this.types = types;
		setInfo();
	}
	
	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(CYCLO.class);


	/**
	 * constructor
	 * automatically sets the information for metric:
	 * shortcut, fullName, description
	 */
	public CYCLO() {
		setInfo();
	}


	/**
	 * sets the information for metric:
	 * shortcut, fullName, description
	 */
	private void setInfo() {
		this.shortcut = "DS_CYCLO";
		this.fullName = "MCCabe Cyclomatic Complexity";
		this.description = "Cyclomatic Complexity directly measures the number of linearly independent paths through a program's source code by counting branch statements.";
	}
	
	/**
	 * calculate metric result
	 */
	public void calculate() {
		log.debug("Calculating metric: CYCLO!");
		
		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();

		// loop over all classes in current CU
		for (ProgramElement clazz : types) {
			assert clazz instanceof ClassType;
			TreeWalker luke = new TreeWalker(clazz);
			
			ArrayList<Integer> puffer= new ArrayList<Integer>();

			// walk all PEs in CU that are non-abstract methods
			while (luke.next(Filters.METHOD_FILTER_EXCL_ABSTRACT)) {
				ProgramElement pe = luke.getProgramElement();
				assert pe instanceof Method;

				
				 int cnt= MetricUtils.cycloMethod(pe);
				 if(cnt==0)
				 {
					 puffer.add(0);
				 }
				 else {puffer.add(cnt);}

			}
			res.add(puffer);
		}
		// set result of metric
		this.result = new IntegerArray2ValueMetric(res, this.shortcut, this.fullName, this.description);

	}
}