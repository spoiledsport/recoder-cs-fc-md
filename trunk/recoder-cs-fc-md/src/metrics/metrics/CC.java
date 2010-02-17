package metrics.metrics;

import java.util.ArrayList;
import java.util.List;

import metrics.util.Filters;
import metrics.util.MetricUtils;
import metricsdata.IntegerArray2ValueMetric;

import org.apache.log4j.Logger;

import recodercs.abstraction.ClassType;
import recodercs.abstraction.Method;
import recodercs.convenience.TreeWalker;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.declaration.ClassDeclaration;
import recodercs.csharp.declaration.TypeDeclaration;
import recodercs.csharp.expression.operator.New;
import recodercs.csharp.reference.ConstructorReference;
import recodercs.csharp.reference.MemberReference;
import recodercs.csharp.reference.MethodReference;
import recodercs.list.MemberReferenceList;
import recodercs.service.CrossReferenceSourceInfo;

/**
 * A metric that calculates the lines of code per class.
 */
public class CC extends DSMetricCalculator {

	// private IntegerArrayValueMetric result;

	/**
	 * Constructur
	 * 
	 * @param types
	 *            an ArrayList of CompilationUnits to calculate
	 * @param si
	 *            the CrossReferenceSourceInfo
	 */
	public CC(ArrayList<ProgramElement> types, CrossReferenceSourceInfo si) {
		this.si = si;
		this.types = types;
		setInfo();
	}

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(CC.class);

	/**
	 * constructor automatically sets the information for metric: shortcut,
	 * fullName, description
	 */
	public CC() {
		setInfo();
	}

	/**
	 * sets the information for metric: shortcut, fullName, description
	 */
	private void setInfo() {
		this.shortcut = "DS_CC";
		this.fullName = "Changing Classes";
		this.description = "The number of classes in which the methods that call the measured method are defined in.";
	}

	/**
	 * calculate metric result
	 */
	public void calculate() {
		log.debug("Calculating metric: CC!");

		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
		ClassDeclaration curClDec = null;

		// loop over all classes in current CU
		for (ProgramElement clazz : types) {
			assert clazz instanceof ClassType;
			ClassType myCLass = (ClassType) clazz;

			log.debug("ClassType: " + myCLass.getFullName());

			ArrayList<Integer> puffer = new ArrayList<Integer>();

			// we are only interested in ClassDeclerations
			if (clazz instanceof ClassDeclaration) {

				// remeber class
				curClDec = (ClassDeclaration) clazz;

				TreeWalker luke = new TreeWalker(clazz);

				// walk all PEs in CU that are non-abstract methods
				while (luke.next(Filters.METHOD_FILTER)) {
					// number of classes calling this method
					int cnt = 0;

					ProgramElement pe = luke.getProgramElement();
					assert pe instanceof Method;
					Method method = (Method) pe;

					// get all references to this method
					MemberReferenceList memrefs = si.getReferences(method);

					// a list of TypeDeclerations, that call this method.
					List<TypeDeclaration> distinctCallers = new ArrayList<TypeDeclaration>();

					for (int i = 0; i < memrefs.size(); i++) {
						TypeDeclaration callerClass;
						MemberReference memref = memrefs.getMemberReference(i);

						// dtermin calling class
						if ((memref instanceof New)) {
							callerClass = MetricUtils
									.getParentClassDeclaration(memref);
						} else if ((memref instanceof ConstructorReference)) {
							callerClass = MetricUtils
									.getParentClassDeclaration((ConstructorReference) memref);
						} else {
							callerClass = MetricUtils
									.getParentClassDeclaration((MethodReference) memref);
						}

						log.debug("In CLassType: " + curClDec.getFullName()
								+ "\nin method: " + method.getFullName()
								+ "\nfound MemberReference: "
								+ memref.toSource()
								+ "\nin calling ClassType: "
								+ callerClass.getFullName());
						// dont calculate the measured class.
						if (curClDec != callerClass
								&& !distinctCallers.contains(callerClass)) {
							distinctCallers.add(callerClass);
							cnt++;
						}
					}

					puffer.add(cnt);

				}
				if (puffer.size() == 0)
					puffer.add(0);
				res.add(puffer);

			}
			// set result for metric
			this.result = new IntegerArray2ValueMetric(res, shortcut, fullName,
					description);
		}
	}
}