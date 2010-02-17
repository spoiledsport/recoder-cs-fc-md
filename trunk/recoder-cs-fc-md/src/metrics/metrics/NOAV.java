package metrics.metrics;

import java.util.ArrayList;
import java.util.List;

import metrics.util.Filters;
import metrics.util.MetricUtils;
import metricsdata.IntegerArray2ValueMetric;

import org.apache.log4j.Logger;

import recodercs.abstraction.ClassType;
import recodercs.abstraction.Field;
import recodercs.abstraction.Method;
import recodercs.convenience.TreeWalker;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.reference.FieldReference;
import recodercs.csharp.reference.MethodReference;
import recodercs.csharp.reference.VariableReference;
import recodercs.service.CrossReferenceSourceInfo;

/**
 * A metric that calculates the number of variables accessed per operation
 */
public class NOAV extends DSMetricCalculator {

	// private IntegerArrayValueMetric result;

	/**
	 * Constructur
	 * 
	 * @param types
	 *            an ArrayList of CompilationUnits to calculate
	 * @param si
	 *            the CrossReferenceSourceInfo
	 */
	public NOAV(ArrayList<ProgramElement> types, CrossReferenceSourceInfo si) {
		this.si = si;
		this.types = types;
		setInfo();
	}

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(NOAV.class);

	/**
	 * constructor automatically sets the information for metric: shortcut,
	 * fullName, description
	 */
	public NOAV() {
		setInfo();
	}

	/**
	 * sets the information for metric: shortcut, fullName, description
	 */
	private void setInfo() {
		this.shortcut = "DS_NOAV";
		this.fullName = "Number of Accessed Variables";
		this.description = "The total number of variables accessed directly from the measured operation";
	}

	/**
	 * calculate metric result
	 */
	public void calculate() {
		log.debug("Calculating metric: NOAV!");

		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();

		// loop over all classes in current CU
		for (ProgramElement clazz : types) {
			assert clazz instanceof ClassType;
			ClassType myClass = (ClassType) clazz;
			TreeWalker luke = new TreeWalker(clazz);

			ArrayList<Integer> puffer = new ArrayList<Integer>();

			// walk all PEs in CU that are non-abstract methods
			while (luke.next(Filters.METHOD_FILTER_EXCL_ABSTRACT)) {
				ProgramElement pe = luke.getProgramElement();
				assert pe instanceof Method;
				Method m = (Method) pe;

				int cnt = calculateVariables(m, myClass);
				if (cnt == 0) {
					puffer.add(0);
				} else {
					puffer.add(cnt);
				}

			}
			res.add(puffer);
		}
		// set result of metric
		this.result = new IntegerArray2ValueMetric(res, this.shortcut,
				this.fullName, this.description);

	}

	private int calculateVariables(Method mr, ClassType currentClassType) {
		TreeWalker walker = new TreeWalker((ProgramElement) mr);

		ArrayList<Field> foreignFields = new ArrayList<Field>();
		ArrayList<VariableReference> variableRef = new ArrayList<VariableReference>();

		while (walker.next()) {
			try {
				ProgramElement pe = walker.getProgramElement();
				if (pe instanceof FieldReference) {
					if (!foreignFields.contains(si
							.getField((FieldReference) pe))
							&& !si.getField((FieldReference) pe).isReadOnly()) {
						foreignFields.add(si.getField((FieldReference) pe));
					}
				} else if (pe instanceof VariableReference) {

					variableRef.add((VariableReference) pe);
				} else if (pe instanceof MethodReference
						&& MetricUtils.isGetterSetterCall((MethodReference) pe,
								currentClassType, si)) {
					Field accessedField = MetricUtils.getAccessedField(
							(MethodReference) pe, currentClassType, si);
					if (!foreignFields.contains(accessedField)) {
						foreignFields.add(accessedField);
					}
				}
			} catch (NullPointerException e) {
				System.out.println("Warning - Null pointer - NOAV contiue");
				continue;
			}

		}
		return foreignFields.size() + isSameVariablesIn(variableRef);
	}

	private int isSameVariablesIn(ArrayList<VariableReference> variableRef) {

		List<String> names = new ArrayList<String>();
		if (variableRef.size() == 0) {
			return 0;
		} else {
			for (VariableReference ref : variableRef) {
				if (!names.contains(ref.getName())) {
					names.add(ref.getName());
				}
			}
			return names.size();
		}
	}
}