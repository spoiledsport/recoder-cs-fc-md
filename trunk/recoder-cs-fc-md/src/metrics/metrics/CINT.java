package metrics.metrics;

import java.util.ArrayList;
import java.util.Iterator;

import metrics.util.Filters;
import metrics.util.MetricUtils;
import metricsdata.IntegerArray2ValueMetric;

import org.apache.log4j.Logger;

import recoder.abstraction.ClassType;
import recoder.abstraction.Constructor;
import recoder.abstraction.Method;
import recoder.abstraction.Type;
import recoder.convenience.TreeWalker;
import recoder.csharp.ProgramElement;
import recoder.csharp.Reference;
import recoder.csharp.declaration.ClassDeclaration;
import recoder.csharp.reference.ConstructorReference;
import recoder.csharp.reference.MethodReference;
import recoder.csharp.reference.TypeReference;
import recoder.list.ClassTypeList;
import recoder.list.MethodList;
import recoder.service.CrossReferenceSourceInfo;

/**
 * A metric that calculates the coupling intensity of a method
 */
public class CINT extends DSMetricCalculator {

	/**
	 * Constructur
	 * 
	 * @param types
	 *            an ArrayList of CompilationUnits to calculate
	 * @param si
	 *            the CrossReferenceSourceInfo
	 */
	public CINT(ArrayList<ProgramElement> types, CrossReferenceSourceInfo si) {
		this.si = si;
		this.types = types;
		setInfo();
	}

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(CINT.class);

	/**
	 * constructor automatically sets the information for metric: shortcut,
	 * fullName, description
	 */
	public CINT() {
		setInfo();
	}

	/**
	 * sets the information for metric: shortcut, fullName, description
	 */
	private void setInfo() {
		this.shortcut = "DS_CDISP";
		this.fullName = "Coupling Dispersion";
		this.description = "The number of classes in which the operation called from the measured operation are defined, divided by CINT";
	}

	/**
	 * Main calculation for this metric.
	 */
	public void calculate() {
		log.debug("Calculating metric: CINT!");

		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();

		// loop over all classes in current CU
		for (ProgramElement clazz : types) {
			assert clazz instanceof ClassType;
			ClassType myCLass = (ClassType) clazz;
			TreeWalker luke = new TreeWalker(clazz);

			ArrayList<Integer> typeRes = new ArrayList<Integer>();
			int cnt = 0;

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
							
							ClassDeclaration c = (ClassDeclaration) si.getType(ref);
							if (myCLass == c) itr.remove();
							
						} else if (ref instanceof MethodReference) {
							
							Method m = si.getMethod((MethodReference) ref);
							ClassType containingClass = m.getContainingClassType();

							// if the containingClass type as myClass, remove the
							// referencea
							if (myCLass == containingClass) itr.remove();
							
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
						cnt = mrl.size() - relatedMethods.size();
					} else {
						cnt = 0;
					}

					if (cnt == 0) {
						typeRes.add(0);
					} else {
						typeRes.add(cnt);
					}

				}

			}
			if (typeRes.size() == 0)
				typeRes.add(0);
			res.add(typeRes);

		}
		// set result for metric
		this.result = new IntegerArray2ValueMetric(res, shortcut, fullName,
				description);

	}
}