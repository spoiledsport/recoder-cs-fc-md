package metrics.metrics;

//import static dataSystem.MetricNamesInterface.DS_LOCC;

import java.util.ArrayList;

import metrics.util.Filters;
import metricsdata.IntegerArrayValueMetric;
import recoder.abstraction.ClassType;
import recoder.abstraction.Method;
import recoder.convenience.TreeWalker;
import recoder.csharp.CSharpSourceElement;
import recoder.csharp.ProgramElement;
import recoder.csharp.SourceElement;
import recoder.service.CrossReferenceSourceInfo;

public class LOCC extends DSMetricCalculator {

	private IntegerArrayValueMetric result;
	
	/**
	 * Constructur
	 * 
	 * @param cu
	 * @param si
	 */
	public LOCC(ArrayList<ProgramElement> types, CrossReferenceSourceInfo si) {
		this.si = si;
		this.types = types;
		setInfo();
	}

	public LOCC() {
		setInfo();
	}

	/**
	 * Return the result of this Metric
	 * 
	 * @return IntegerArrayValueMetric
	 */
	public IntegerArrayValueMetric getResult() {
		return result;
	}

	private void setInfo() {
		this.shortcut = "DS_LOCC";
		this.fullName = "Lines of Code (Class)";
		this.description = "Number of lines of code of a class, including blank lines, comments, parenthesis, and method header. But without java doc header.";
	}
	
	/**
	 * Main calculation for this metric.
	 */
	public void calculate() {
		ArrayList<Integer> res = new ArrayList<Integer>();
		int line = 0;
		int cnt = 0;

		// loop over all classes in current CU
		for (ProgramElement clazz : types) {
			assert clazz instanceof ClassType;
			TreeWalker luke = new TreeWalker(clazz);

			// walk all PEs in CU that are non-abstract methods
			while (luke.next(Filters.METHOD_FILTER_EXCL_ABSTRACT)) {
				ProgramElement pe = luke.getProgramElement();
				assert pe instanceof Method;
				Method method = (Method) pe;

				// start with the line of the method head
				SourceElement.Position start = ((CSharpSourceElement) method)
						.getStartPosition();
				// end with the last curly brace of method.
				SourceElement.Position end = ((CSharpSourceElement) method)
						.getEndPosition();
				line = (end.getLine() - start.getLine()) + 1;
				cnt = line + cnt;
			}
			res.add(cnt);
		}

		this.result = new IntegerArrayValueMetric(res, this.shortcut, this.fullName, this.description);

	}
}