package metrics.metrics;

import java.util.ArrayList;

import metrics.util.Filters;
import metricsdata.IntegerArrayValueMetric;

import org.apache.log4j.Logger;

import recoder.abstraction.ClassType;
import recoder.abstraction.Method;
import recoder.convenience.TreeWalker;
import recoder.csharp.ProgramElement;
import recoder.service.CrossReferenceSourceInfo;

/**
 * A metric that calculates the number of accessor methods
 */
public class NOAM extends DSMetricCalculator {

	// private IntegerArrayValueMetric result;

	/**
	 * Constructur
	 * 
	 * @param types
	 *            an ArrayList of CompilationUnits to calculate
	 * @param si
	 *            the CrossReferenceSourceInfo
	 */
	public NOAM(ArrayList<ProgramElement> types, CrossReferenceSourceInfo si) {
		this.si = si;
		this.types = types;
		setInfo();
	}

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(NOAM.class);

	/**
	 * constructor automatically sets the information for metric: shortcut,
	 * fullName, description
	 */
	public NOAM() {
		setInfo();
	}

	/**
	 * sets the information for metric: shortcut, fullName, description
	 */
	private void setInfo() {
		this.shortcut = "DS_NOAM";
		this.fullName = "Number of Accessor Methods";
		this.description = "NOAM: Number of get/set methods of a class.Don't measured for Abstract classes. Here is summed the number of only public get/set methods(Constructur(-),Static(-), Abstract(-)";
	}

	/**
	 * calculate metric result
	 */
	public void calculate() {
		log.debug("Calculating metric: NOAM!");

		ArrayList<Integer> res = new ArrayList<Integer>();

		// loop over all classes in current CU
		for (ProgramElement clazz : types) {
			// the number of suitbale Fields
			int cnt = 0;

			assert clazz instanceof ClassType;
			ClassType myClass = (ClassType) clazz;

			// we are only interested in non-abstract classes
			if (myClass.isAbstract()) {
				res.add(null);
				continue;
			} else {
				TreeWalker luke = new TreeWalker(clazz);

				// walk all PEs in CU that are non-static, non-readonly public
				// Fields
				while (luke
						.next(Filters.PUBLIC_GETTERSETTER_FILTER_EXCL_CONSTR_STATIC_ABSTR)) {
					ProgramElement pe = luke.getProgramElement();
					assert pe instanceof Method;
					Method m = (Method) pe;

					// new suitable Method found
					cnt++;

					log.debug("In ClassType: " + myClass.getFullName()
							+ "\nfound getter/setter method: "
							+ m.getFullName()
							+ "\ntotal getter/setter method: " + cnt);

				}
				res.add(cnt);
			}

		}
		// set result of metric
		this.result = new IntegerArrayValueMetric(res, this.shortcut,
				this.fullName, this.description);

	}
}