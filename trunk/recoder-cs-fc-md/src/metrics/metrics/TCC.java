package metrics.metrics;

import java.util.ArrayList;

import metrics.util.Filters;
import metricsdata.DoubleArrayValueMetric;

import org.apache.log4j.Logger;

import recodercs.abstraction.ClassType;
import recodercs.abstraction.Field;
import recodercs.abstraction.Method;
import recodercs.convenience.TreeWalker;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.reference.FieldReference;
import recodercs.list.MethodList;
import recodercs.service.CrossReferenceSourceInfo;

/**
 * A metric that calculates Tight CLass Cohesion of a class
 */
public class TCC extends DSMetricCalculator {

	// private IntegerArrayValueMetric result;

	/**
	 * Constructur
	 * 
	 * @param types
	 *            an ArrayList of CompilationUnits to calculate
	 * @param si
	 *            the CrossReferenceSourceInfo
	 */
	public TCC(ArrayList<ProgramElement> types, CrossReferenceSourceInfo si) {
		this.si = si;
		this.types = types;
		setInfo();
	}

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(TCC.class);

	/**
	 * constructor automatically sets the information for metric: shortcut,
	 * fullName, description
	 */
	public TCC() {
		setInfo();
	}

	/**
	 * sets the information for metric: shortcut, fullName, description
	 */
	private void setInfo() {
		this.shortcut = "DS_TCC";
		this.fullName = "Tight Class Cohesion";
		this.description = "The relative number of method pairs of a class that access in common at least one attribute of the measured class.";
	}

	/**
	 * calculate metric result
	 */
	public void calculate() {
		log.debug("Calculating metric: TCC!");
		ArrayList<Double> res = new ArrayList<Double>();

		// number of direct connections
		double ndc = 0;
		// maximum number of possible connections
		double np = 0;

		// loop over all classes in current CU
		for (ProgramElement clazz : types) {
			assert clazz instanceof ClassType;
			ClassType myClazz = (ClassType) clazz;

			// we are not interested in abstract classes
			if (myClazz.isAbstract()) {
				res.add(null);
				continue;
			} else {
				// get myClazz's methods
				MethodList methods = myClazz.getMethods();
				int numberOfMethods = methods.size();

				// calculate maximum number of possible connections
				np = (numberOfMethods * (numberOfMethods - 1)) / 2.;

				// loop over methods
				for (int i = 0; i < numberOfMethods - 1; i++) {

					log.debug("In ClassType: " + myClazz.getFullName()
							+ "\nfound Method: "
							+ methods.getMethod(i).getFullName());

					// get a list of fields from ClassType myClazz, that are
					// accessed by methods.getMethod(i)
					ArrayList<Field> fieldCandidates = getFieldsAccessedByMethod(methods
							.getMethod(i));

					for (int j = i + 1; j < methods.size(); j++) {
						if (fieldIntersectionExists(fieldCandidates,
								getFieldsAccessedByMethod(methods.getMethod(j)))) {
							ndc++;
						}
					}
				}

				if (np == 0.) {
					res.add(0.0);
				} else {
					res.add(ndc / np);
				}

			}

		}
		if (res.size() == 0) {
			res.add(0.0);
		}

		// set result of metric
		this.result = new DoubleArrayValueMetric(res, this.shortcut,
				this.fullName, this.description);

	}

	/**
	 * 
	 * 
	 * @param list1
	 * @param list2
	 * @return boolean
	 * 
	 */
	private boolean fieldIntersectionExists(ArrayList<Field> list1,
			ArrayList<Field> list2) {
		for (Field field : list1) {
			if (list2.contains(field))
				return true;
		}

		return false;
	}

	/**
	 * Returns a list of Fields from the containing class of m, that are
	 * accessed by m.
	 * 
	 * @param m
	 * @return ArrayList<Field>
	 */
	private ArrayList<Field> getFieldsAccessedByMethod(Method m) {
		ArrayList<Field> res = new ArrayList<Field>();

		// get class, that contains Method m
		ClassType ct = m.getContainingClassType();

		// walk Method m and look for FieldReferences
		TreeWalker walker = new TreeWalker((ProgramElement) m);
		while (walker.next(Filters.FIELDREFERENCE_FILTER)) {
			ProgramElement pe = walker.getProgramElement();

			assert pe instanceof FieldReference;
			FieldReference fr = (FieldReference) pe;

			// get referenced Field and containing ClassType for FieldReference
			// fr
			Field referencedField = si.getField(fr);
			ClassType containigClass = referencedField.getContainingClassType();

			// if referencedField is in the same ClassType as Method m, add
			// Field referncedField to result res.
			if (containigClass == ct) {
				res.add(referencedField);

				log.debug("Method: " + m.getFullName() + "\nin ClassType: "
						+ ct.getFullName() + "\ncontains Fieldreference: "
						+ fr.getName() + "\nreferencing Field: "
						+ referencedField.getFullName() + "\nin ClassType: "
						+ containigClass.getFullName());

			}
		}

		return res;
	}
}