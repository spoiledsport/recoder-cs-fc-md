package metrics.metrics;

import java.util.ArrayList;

import metrics.util.Filters;
import metrics.util.MetricUtils;
import metricsdata.DoubleArrayValueMetric;

import org.apache.log4j.Logger;

import recoder.abstraction.ClassType;
import recoder.abstraction.Field;
import recoder.abstraction.Method;
import recoder.convenience.TreeWalker;
import recoder.csharp.ProgramElement;
import recoder.service.CrossReferenceSourceInfo;

/**
 * A metric that calculates the number of public attributes of a class
 */
public class WOC extends DSMetricCalculator {

	// private IntegerArrayValueMetric result;

	/**
	 * Constructur
	 * 
	 * @param types
	 *            an ArrayList of CompilationUnits to calculate
	 * @param si
	 *            the CrossReferenceSourceInfo
	 */
	public WOC(ArrayList<ProgramElement> types, CrossReferenceSourceInfo si) {
		this.si = si;
		this.types = types;
		setInfo();
	}

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(WOC.class);

	/**
	 * constructor automatically sets the information for metric: shortcut,
	 * fullName, description
	 */
	public WOC() {
		setInfo();
	}

	/**
	 * sets the information for metric: shortcut, fullName, description
	 */
	private void setInfo() {
		this.shortcut = "DS_WOC";
		this.fullName = "Weight of a Class";
		this.description = "Number of functional public methods divided by the total number of public members.";
	}

	/**
	 * calculate metric result
	 */
	public void calculate() {
		log.debug("Calculating metric: NOPA!");

		ArrayList<Double> res = new ArrayList<Double>();

		// loop over all classes in current CU
		for (ProgramElement clazz : types) {
			// the number of suitbale Fields
			double cntFunctMethods = 0;
			double cntGetSet = 0;
			double cntAttributes = 0;

			assert clazz instanceof ClassType;
			ClassType myClass = (ClassType) clazz;

			// we are only interested in non-abstract classes
			if (myClass.isAbstract()) {
				res.add(null);
				continue;
			} else {

				TreeWalker luke = new TreeWalker(clazz);

				while (luke.next(Filters.WOC_FILTER)) {
					ProgramElement pe = luke.getProgramElement();

					if (pe instanceof Method) {
						Method m = (Method) pe;
						if (MetricUtils.isGetterSetterSimple(m)
								&& !m.isStatic()) {
							cntGetSet += 0.5;
							log.debug("In ClassType: " + myClass.getFullName()
									+ "\nfound GetterSetter: " + m.getFullName()
									+ "\ntotal GetterSetter: " + cntGetSet);
						} else if (!MetricUtils.isGetterSetterSimple(m)
								&& !m.isAbstract()) {
							cntFunctMethods++;
							log.debug("In ClassType: " + myClass.getFullName()
									+ "\nfound functional Method: " + m.getFullName()
									+ "\ntotal FunctMethods: " + cntFunctMethods);
						}
					} else if (pe instanceof Field) {
						Field f = (Field) pe;
						cntAttributes++;
						log.debug("In ClassType: " + myClass.getFullName()
								+ "\nfound GetterSetter: " + f.getFullName()
								+ "\ntotal Fields: " + cntAttributes);
					}
				}	
			}
			res.add((cntFunctMethods+1)/(cntGetSet+cntAttributes+1));
		}
		
		if(res.size() == 0) {
			res.add(0.0);
		}
		// set result of metric
		this.result = new DoubleArrayValueMetric(res, this.shortcut,
				this.fullName, this.description);

	}
}