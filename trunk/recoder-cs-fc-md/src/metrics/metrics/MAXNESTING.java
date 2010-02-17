package metrics.metrics;

import java.util.ArrayList;

import metrics.util.Filters;
import metricsdata.IntegerArray2ValueMetric;

import org.apache.log4j.Logger;

import recodercs.abstraction.ClassType;
import recodercs.abstraction.Method;
import recodercs.convenience.TreeWalker;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.StatementBlock;
import recodercs.csharp.declaration.MethodDeclaration;
import recodercs.csharp.expression.operator.Conditional;
import recodercs.csharp.statement.Do;
import recodercs.csharp.statement.For;
import recodercs.csharp.statement.Foreach;
import recodercs.csharp.statement.If;
import recodercs.csharp.statement.Switch;
import recodercs.csharp.statement.Then;
import recodercs.csharp.statement.While;
import recodercs.service.CrossReferenceSourceInfo;

/**
 * A metric that calculates MAXNESTING
 */
public class MAXNESTING extends DSMetricCalculator {

	/**
	 * Constructur
	 * 
	 * @param types
	 *            an ArrayList of CompilationUnits to calculate
	 * @param si
	 *            the CrossReferenceSourceInfo
	 */
	public MAXNESTING(ArrayList<ProgramElement> types,
			CrossReferenceSourceInfo si) {
		this.si = si;
		this.types = types;
		setInfo();
	}

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(MAXNESTING.class);

	/**
	 * constructor automatically sets the information for metric: shortcut,
	 * fullName, description
	 */
	public MAXNESTING() {
		setInfo();
	}

	/**
	 * sets the information for metric: shortcut, fullName, description
	 */
	private void setInfo() {
		this.shortcut = "DS_MAXNESTING";
		this.fullName = "Maximum Nesting Level";
		this.description = "The maximum nesting level of control structures within an operation.";
	}

	/**
	 * Main calculation for this metric.
	 */
	public void calculate() {
		log.debug("Calculating metric: MAXNESTING!");

		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
		int cal = 0;

		// loop over all classes in current CU
		for (ProgramElement clazz : types) {
			assert clazz instanceof ClassType;
			TreeWalker luke = new TreeWalker(clazz);

			ArrayList<Integer> puffer = new ArrayList<Integer>();

			// walk all PEs in CU that are non-abstract methods
			while (luke.next(Filters.METHOD_FILTER_EXCL_ABSTRACT)) {
				ProgramElement pe = luke.getProgramElement();
				assert pe instanceof Method;

				Method method = (Method) pe;

				cal = calculateMaxLevel(method);

				if (cal == 0) {
					puffer.add(0);
				} else
					puffer.add(cal);
			}
			
			res.add(puffer);

		}
		// set result for metric
		this.result = new IntegerArray2ValueMetric(res, shortcut, fullName,
				description);

	}

	private int calculateMaxLevel(Method m) {
		TreeWalker walkerMethod = new TreeWalker((ProgramElement) m);
		int cnt = 0;
		int maxnesting = 0;
		while (walkerMethod.next()) {
			ProgramElement pe = walkerMethod.getProgramElement();
			if (isLoop(pe) && !(pe instanceof StatementBlock)
					&& !(pe instanceof MethodDeclaration)
					&& !(pe instanceof Then)) {
				TreeWalker walkerLoop = new TreeWalker((ProgramElement) pe);
				while (walkerLoop.next()) {
					try {

						ProgramElement peLoop = walkerLoop.getProgramElement();

						if (isLoop(peLoop)
								&& !(peLoop instanceof MethodDeclaration)
								&& !(peLoop instanceof Then)
								&& !(peLoop instanceof StatementBlock)) {
							if (!inLoop(peLoop)) {
								cnt++;
								walkerLoop = new TreeWalker(
										(ProgramElement) peLoop);
								walkerLoop.next();
							} else {
								cnt++;
								walkerLoop = new TreeWalker(
										(ProgramElement) peLoop);
								walkerLoop.next();
							}
						}
					} catch (NullPointerException e) {
						System.out
								.println("Warning - MAXNESTING - NullPointer - continue");
						continue;
					}

				}
				if (maxnesting < cnt) {
					maxnesting = cnt;
				}
				cnt = 0;
			}
		}
		return maxnesting;
	}

	public boolean inLoop(ProgramElement pe) {
		TreeWalker wa = new TreeWalker((ProgramElement) pe);
		TreeWalker puf = wa;

		while (wa.next()) {
			try {
				puf.next();
				ProgramElement loop = (ProgramElement) puf.getProgramElement();

				if (isLoop(loop) && !(loop instanceof StatementBlock)
						&& !(loop instanceof MethodDeclaration)
						&& !(loop instanceof Then)) {
					return true;
				}
			} catch (NullPointerException e) {
				System.out
						.println("Warning - MAXNESTING - NullPointer - continue");
				continue;
			}
		}
		return false;
	}

	private boolean isLoop(ProgramElement pe) {
		if (pe instanceof For) {
			return true;
		} else if (pe instanceof Foreach) {
			return true;
		} else if (pe instanceof While) {
			return true;
		} else if (pe instanceof Do) {
			return true;
		} else if (pe instanceof If) {
			return true;
		} else if (pe instanceof Switch) {
			return true;
		} else if (pe instanceof Conditional) {
			return true;
		}

		return false;
	}
}