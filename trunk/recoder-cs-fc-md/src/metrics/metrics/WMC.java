package metrics.metrics;

import java.util.ArrayList;

import metrics.util.Filters;
import metricsdata.IntegerArrayValueMetric;

import org.apache.log4j.Logger;

import recoder.abstraction.ClassType;
import recoder.abstraction.DefaultConstructor;
import recoder.abstraction.Method;
import recoder.convenience.TreeWalker;
import recoder.csharp.ProgramElement;
import recoder.service.CrossReferenceSourceInfo;

/**
 * A metric that calculates the weighted method count of a class
 */
public class WMC extends DSMetricCalculator {

	/**
	 * Constructur
	 * 
	 * @param types
	 *            an ArrayList of CompilationUnits to calculate
	 * @param si
	 *            the CrossReferenceSourceInfo
	 */
	public WMC(ArrayList<ProgramElement> types, CrossReferenceSourceInfo si) {
		this.si = si;
		this.types = types;
		setInfo();
	}

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(LOCM.class);

	/**
	 * constructor automatically sets the information for metric: shortcut,
	 * fullName, description
	 */
	public WMC() {
		setInfo();
	}

	/**
	 * sets the information for metric: shortcut, fullName, description
	 */
	private void setInfo() {
		this.shortcut = "DS_WMC";
		this.fullName = "Weighted Method Count";
		this.description = "The sum of the statical complexity of all methods of a class. The CYCLO metric is used to quantify the method's complexity.";
	}

	/**
	 * calculate metric result
	 */
	public void calculate() {
		ArrayList<Integer> res = new ArrayList<Integer>();

		
		
		// loop over all classes in current CU
		for (ProgramElement clazz : types) {
			int wmc =0;
			
			assert clazz instanceof ClassType;
			TreeWalker luke = new TreeWalker(clazz);

			// walk all PEs in CU that are non-abstract methods
			while (luke.next(Filters.METHOD_FILTER_EXCL_ABSTRACT)) {
				ProgramElement pe = luke.getProgramElement();
				assert pe instanceof Method;
				
				// if method is not a DefaultConstructor, calculate CYCLO for it
				if (!(pe instanceof DefaultConstructor)){
					wmc += metrics.util.MetricUtils.cycloMethod(pe);
				}
			}
			res.add(wmc);
		}
		
		if(res.size() == 0) {
			res.add(0);
		}
		
		// set result for metric
		this.result = new IntegerArrayValueMetric(res, shortcut, fullName,
				description);
		

	}

}
