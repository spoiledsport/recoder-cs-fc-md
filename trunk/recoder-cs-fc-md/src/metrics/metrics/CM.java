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
import recodercs.csharp.declaration.ClassDeclaration;
import recodercs.csharp.declaration.MemberDeclaration;
import recodercs.csharp.declaration.TypeDeclaration;
import recodercs.csharp.expression.operator.New;
import recodercs.csharp.reference.ConstructorReference;
import recodercs.csharp.reference.MemberReference;
import recodercs.csharp.reference.MethodReference;
import recodercs.list.MemberReferenceList;
import recodercs.service.CrossReferenceSourceInfo;


public class CM extends DSMetricCalculator {

	// private IntegerArrayValueMetric result;

	/**
	 * Constructur
	 * 
	 * @param types
	 *            an ArrayList of CompilationUnits to calculate
	 * @param si
	 *            the CrossReferenceSourceInfo
	 */
	public CM(ArrayList<ProgramElement> types, CrossReferenceSourceInfo si) {
		this.si = si;
		this.types = types;
		setInfo();
	}

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(CM.class);

	/**
	 * constructor automatically sets the information for metric: shortcut,
	 * fullName, description
	 */
	public CM() {
		setInfo();
	}

	/**
	 * sets the information for metric: shortcut, fullName, description
	 */
	private void setInfo() {
		this.shortcut = "DS_CM";
		this.fullName = "Changing Methods";
		this.description = "The number of dinstinct methods that call the measured method.";
	}

	/**
	 * calculate metric result
	 */
	public void calculate() {
		log.debug("Calculating metric: CM!");

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

					Integer maxVal = 0;

					ProgramElement pe = luke.getProgramElement();
					assert pe instanceof Method;
					Method method = (Method) pe;

					// get all references to this method
					MemberReferenceList memrefs = si.getReferences(method);

					for (int i = 0; i < memrefs.size(); i++) {

						assert memrefs.getMemberReference(i) instanceof MemberReference;
						MemberReference memref = (MemberReference) memrefs
								.getMemberReference(i);

						TypeDeclaration callerClass;
						MemberDeclaration callerOperation;
						if ((memref instanceof New)) {

							callerClass = MetricUtils
									.getParentClassDeclaration((New) memref);
							callerOperation = MetricUtils
									.getParentMemberDeclaration((New) memref);
						} else if ((memref instanceof ConstructorReference)) {
							callerClass = MetricUtils
									.getParentClassDeclaration((ConstructorReference) memref);
							callerOperation = MetricUtils
									.getParentMemberDeclaration((ConstructorReference) memref);
						} else {
							callerClass = MetricUtils
									.getParentClassDeclaration((MethodReference) memref);
							callerOperation = MetricUtils
									.getParentMemberDeclaration((MethodReference) memref);
						}

						if (callerClass != null && callerOperation != null) {
							maxVal++;
						}

					}
					puffer.add(maxVal);

					// log.debug("In CLassType: " + curClDec.getFullName()
					// + "\nin method: " + method.getFullName()
					// + "\nfound MemberReference: "
					// + memref.toSource()
					// + "\nin calling ClassType: "
					// + callerClass.getFullName());

					// dont calculate the measured class.
				}
				if (puffer.size() == 0)
					puffer.add(0);
				res.add(puffer);
			}


		}
		// set result for metric
		this.result = new IntegerArray2ValueMetric(res, shortcut, fullName,
				description);
	}
}
