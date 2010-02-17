package metrics.metrics;

import java.util.ArrayList;

import metrics.util.Filters;
import metricsdata.IntegerArrayValueMetric;

import org.apache.log4j.Logger;

import recodercs.abstraction.ClassType;
import recodercs.abstraction.Field;
import recodercs.convenience.TreeWalker;
import recodercs.csharp.ProgramElement;
import recodercs.service.CrossReferenceSourceInfo;

/**
 * A metric that calculates the number of public attributes of a class
 */
public class NOPA extends DSMetricCalculator {

	// private IntegerArrayValueMetric result;

	/**
	 * Constructur
	 * 
	 * @param types
	 *            an ArrayList of CompilationUnits to calculate
	 * @param si
	 *            the CrossReferenceSourceInfo
	 */
	public NOPA(ArrayList<ProgramElement> types, CrossReferenceSourceInfo si) {
		this.si = si;
		this.types = types;
		setInfo();
	}

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(NOPA.class);

	/**
	 * constructor automatically sets the information for metric: shortcut,
	 * fullName, description
	 */
	public NOPA() {
		setInfo();
	}

	/**
	 * sets the information for metric: shortcut, fullName, description
	 */
	private void setInfo() {
		this.shortcut = "DS_NOPA";
		this.fullName = "Number of Public Attributes";
		this.description = "The Number of Public Attributes, which are not static and constant, of a class.Don't measured for Abstract classes.";
	}

	/**
	 * calculate metric result
	 */
	public void calculate() {
		log.debug("Calculating metric: NOPA!");

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
						.next(Filters.PUBLIC_FIELD_FILTER_EXCL_STATIC_CONSTANT)) {
					ProgramElement pe = luke.getProgramElement();
					assert pe instanceof Field;
					Field f = (Field) pe;

					// new suitable FIeld found
					cnt++;

					log.debug("In ClassType: " + myClass.getFullName()
							+ "\nfound Field: " + f.getFullName()
							+ "\ntotal Fields: " + cnt);

				}
				res.add(cnt);
			}

		}
		// set result of metric
		this.result = new IntegerArrayValueMetric(res, this.shortcut,
				this.fullName, this.description);

	}
}