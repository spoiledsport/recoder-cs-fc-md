package metrics.metrics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import metrics.util.Filters;
import metrics.util.MetricUtils;
import metricsdata.DoubleArray2ValueMetric;

import org.apache.log4j.Logger;

import recoder.abstraction.ClassType;
import recoder.abstraction.Method;
import recoder.convenience.TreeWalker;
import recoder.csharp.ProgramElement;
import recoder.csharp.Reference;
import recoder.csharp.declaration.ClassDeclaration;
import recoder.csharp.reference.MethodReference;
import recoder.csharp.reference.TypeReference;
import recoder.list.ClassTypeList;
import recoder.list.MethodList;
import recoder.service.CrossReferenceSourceInfo;

/**
 * A metric that calculates the coupling intensity of a method
 */
public class CDISP extends DSMetricCalculator {

	/**
	 * Constructur
	 * 
	 * @param types
	 *            an ArrayList of CompilationUnits to calculate
	 * @param si
	 *            the CrossReferenceSourceInfo
	 */
	public CDISP(ArrayList<ProgramElement> types, CrossReferenceSourceInfo si) {
		this.si = si;
		this.types = types;
		setInfo();
	}

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(CDISP.class);

	/**
	 * constructor automatically sets the information for metric: shortcut,
	 * fullName, description
	 */
	public CDISP() {
		setInfo();
	}

	/**
	 * sets the information for metric: shortcut, fullName, description
	 */
	private void setInfo() {
		this.shortcut = "DS_CDISP";
		this.fullName = "Coupling Intensity";
		this.description = "The number of classes in which the operation called from the measured operation are defined, divided by CINT.";
	}

	/**
	 * Main calculation for this metric.
	 */
	public void calculate() {
		log.debug("Calculating metric: CDISP!");

		ArrayList<ArrayList<Double>> res = new ArrayList<ArrayList<Double>>();

		// loop over all classes in current CU
		for (ProgramElement clazz : types) {
			assert clazz instanceof ClassType;
			ClassType myCLass = (ClassType) clazz;
			TreeWalker luke = new TreeWalker(clazz);

			ArrayList<Double> typeRes = new ArrayList<Double>();
			int cint = 0;
			int cdisp = 0;

			// walk all PEs in CU that are non-abstract methods
			while (luke.next(Filters.METHOD_FILTER)) {
				ProgramElement pe = luke.getProgramElement();
				assert pe instanceof Method;
				Method method = (Method) pe;

				// we are not considering abstract methods
				if (method.isAbstract()) {
					typeRes.add(null);
				} else {
					ArrayList<Reference> relatedMethods = new ArrayList<Reference>();

					// extract all MethodReferences in method
					ArrayList<Reference> mrl = MetricUtils
							.getMethodReferences(method);

					// first we want to ignore MethodReferences, that point to
					// methods from the same class
					Iterator<Reference> itr = mrl.iterator();
					while (itr.hasNext()) {
						Reference ref = itr.next();
						// get the containing ClassType of the Method the
						// Reference points to. Constructors are TypeReferences
						if (ref instanceof TypeReference) {

							ClassDeclaration c = (ClassDeclaration) si
									.getType(ref);
							if (myCLass == c)
								itr.remove();

						} else if (ref instanceof MethodReference) {

							Method m = si.getMethod((MethodReference) ref);
							ClassType containingClass = m
									.getContainingClassType();

							// if the containingClass type as myClass, remove
							// the
							// reference
							if (myCLass == containingClass)
								itr.remove();

						}
					}

					// get SuperTypes of ClassType containing method
					ClassTypeList superTypes = myCLass.getSupertypes();

					log
							.debug("In ClassType:" + myCLass.getFullName()
									+ "\nfound Method: " + method.getFullName()
									+ "\ncontaining MethodReferences: "
									+ mrl.size()
									+ "\nSuperTypes of ClassType: "
									+ superTypes.size());

					// loop over superTypes
					for (int i = 0; i < superTypes.size(); i++) {

						if (superTypes.getClassType(i) != null) {
							// get superTypes Methods
							MethodList superTypeMethods = superTypes
									.getClassType(i).getMethods();

							for (Reference mr : MetricUtils.getRelatedMethods(
									mrl, superTypeMethods)) {
								relatedMethods.add(mr);
							}

						}

					}

					// all method referens without unrelated methods
					if (mrl.size() >= relatedMethods.size()) {
						cint = mrl.size() - relatedMethods.size();
					} else {
						cint = 0;
					}
					log.debug("Calculated CINT for method: "
							+ method.getFullName() + "\nis: " + cint);
					// end of calculation of CINT

					// start calculating CDSIP
					List<Reference> unrelatedReferences = new ArrayList<Reference>();

					// extract method references from unrelated classes
					for (Reference ref : mrl) {
						if (!relatedMethods.contains(ref)) {
							log.debug("Found reference candidate for CDISP: "
									+ ref.toSource());
							unrelatedReferences.add(ref);
						}
					}

					cdisp = calculateCDISP(unrelatedReferences);
					log
							.debug("Calculated CDISP for method (before division by CINT): "
									+ method.getFullName() + "\nis: " + cdisp);
					if (cdisp == 0) {
						typeRes.add(0.0);
					} else {
						typeRes.add((double) cdisp / (double) cint);
					}

				}

			}

			res.add(typeRes);

		}
		// set result for metric
		this.result = new DoubleArray2ValueMetric(res, shortcut, fullName,
				description);

	}

	/**
	 * Sort the method reference in the specificed methods, so they will only
	 * found one time in the List
	 * 
	 * @param methods
	 * @return
	 */
	private int calculateCDISP(List<Reference> methods) {
		List<String> res = new ArrayList<String>();
		for (Reference method : methods) {
			if (!(method instanceof TypeReference)) {
				if (res.isEmpty()) {
					res.add(si.getMethod((MethodReference) method)
							.getContainingClassType().getName());
				} else if (isAlreadyIn(res, si.getMethod(
						(MethodReference) method).getContainingClassType()
						.getName())) {
					res.add(si.getMethod((MethodReference) method)
							.getContainingClassType().getName());
				}
			}
		}
		return res.size();
	}

	/**
	 * Check, the class is already accessed from another methods.
	 * 
	 * @param ClassNames
	 * @param name
	 * @return
	 */
	private boolean isAlreadyIn(List<String> ClassNames, String name) {
		for (String n : ClassNames) {
			try {
				if (n.equals(name)) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}