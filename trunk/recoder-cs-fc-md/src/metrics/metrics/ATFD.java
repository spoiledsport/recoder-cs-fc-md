package metrics.metrics;

import java.util.ArrayList;

import metrics.util.Filters;
import metrics.util.MetricUtils;
import metricsdata.IntegerArrayValueMetric;

import org.apache.log4j.Logger;

import recodercs.abstraction.ClassType;
import recodercs.abstraction.Field;
import recodercs.abstraction.Method;
import recodercs.convenience.TreeWalker;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.reference.FieldReference;
import recodercs.csharp.reference.MethodReference;
import recodercs.service.CrossReferenceSourceInfo;

/**
 * A metric that calculates the access to foreign data of a class.
 */
public class ATFD extends DSMetricCalculator {

	/**
	 * Constructur
	 * 
	 * @param types
	 *            an ArrayList of CompilationUnits to calculate
	 * @param si
	 *            the CrossReferenceSourceInfo
	 */
	public ATFD(ArrayList<ProgramElement> types, CrossReferenceSourceInfo si) {
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
	public ATFD() {
		setInfo();
	}

	/**
	 * sets the information for metric: shortcut, fullName, description
	 */
	private void setInfo() {
		this.shortcut = "DS_ATFD";
		this.fullName = "Access to Foreign Data";
		this.description = "The number of attributes from unrelated classes that are accessed directly or by invoking accessor methods.";
	}

	/**
	 * calculate metric result
	 */
	public void calculate() {
		log.debug("Calculating metric: ATFD!");
		
		ArrayList<Integer> res = new ArrayList<Integer>();

		// the list of foreign fields accessed by the class
		ArrayList<Field> foreignFields = new ArrayList<Field>();

		// loop over all classes in current CU
		for (ProgramElement clazz : types) {

			// clear foreignFields from last ClassType
			foreignFields.clear();

			assert clazz instanceof ClassType;
			ClassType myClazz = (ClassType) clazz;

//			log
//					.debug("Looking for ATFD in ClassType: "
//							+ myClazz.getFullName());

			TreeWalker luke = new TreeWalker(clazz);

			// walk all PEs in CU that are non-abstract methods
			while (luke.next(Filters.FIELDREFERENCE_GETTERSETTER_FILTER)) {
				ProgramElement pe = luke.getProgramElement();

				// a FieldReference
				if (pe instanceof FieldReference) {
					log.debug("found new FieldReference:\n" + pe.toSource());
					FieldReference fr = (FieldReference) pe;

					// the field the refrence points to
					Field f = si.getField(fr);

					// make sure, the FieldReference is neither static nor a
					// field of the class under consideration.
					if (!f.isReadOnly()
							&& !f.getContainingClassType().equals(myClazz)) {

						// if we found a new field, add it
						if (!foreignFields.contains(f)) {
							foreignFields.add(f);
							log.debug("Found FieldReference candidate: "
									+ fr.getName() + "\non line: "
									+ pe.getStartPosition()
									+ "\nin ClassType: "
									+ myClazz.getFullName()
									+ "\nreferencing Field : "
									+ f.getFullName() + "\nIn CLassType: "
									+ f.getContainingClassType().getFullName());
						} else {
							log.debug("Ignoring known FieldRereference: "
									+ fr.getName() + "\non line: "
									+ pe.getStartPosition()
									+ "\nin ClassType: "
									+ myClazz.getFullName()
									+ "\nreferencing Field : "
									+ f.getFullName() + "\nIn CLassType: "
									+ f.getContainingClassType().getFullName());
						}
					}
				}

				// a MethodReference
				else if (pe instanceof MethodReference) {
					log.debug("found new GetterSetter Candidate:\n"
							+ pe.toSource());
					MethodReference mr = (MethodReference) pe;

					// the method the reference points to
					Method m = si.getMethod(mr);
					log.debug("GetterSetter Candidate points to method: "
							+ m.getFullName());

					// check if method is a getter/setter and not static
					if (MetricUtils.isGetterSetterCall(mr, myClazz, si)
							&& !m.isStatic()) {
						// get the field, that is accessed by the method
						// reference
						Field accessedField = MetricUtils.getAccessedField(
								(MethodReference) pe, myClazz, si);

						// if we found a new field, add it
						if (!foreignFields.contains(accessedField)) {
							foreignFields.add(accessedField);
							log.debug("Found MethodReference candidate: "
									+ mr.getName()
									+ "\non line: "
									+ pe.getStartPosition()
									+ "\nin ClassType: "
									+ myClazz.getFullName()
									+ "\nreferencing field: "
									+ accessedField.getFullName()
									+ "\nin ClassType: "
									+ accessedField.getContainingClassType()
											.getFullName());
						} else {
							log.debug("Ignoring known MethodReference: "
									+ mr.getName()
									+ "\non line: "
									+ pe.getStartPosition()
									+ "\nin ClassType: "
									+ myClazz.getFullName()
									+ "\nreferencing field: "
									+ accessedField.getFullName()
									+ "\nin ClassType: "
									+ accessedField.getContainingClassType()
											.getFullName());
						}
					}
				}
			}
			// for all supertypes of the class under consideration check, if the
			// accessedFields found really do not belong to any of theses
			// classes. Access to data in related classes does not count!
			
			
			for (int i = 0; i < myClazz.getAllSupertypes().size(); i++) {
				ClassType superType = myClazz.getAllSupertypes().getClassType(i);
				
				// ignore the case, when myClazz == superType
				if (!(myClazz == superType)) {
					foreignFields = MetricUtils.getUnrelatedFields(foreignFields,
							superType);
				}

			}

			res.add(foreignFields.size());
		}

		if (res.size() == 0) {
			res.add(0);
		}

		// set result for metric
		this.result = new IntegerArrayValueMetric(res, shortcut, fullName,
				description);

	}
}
