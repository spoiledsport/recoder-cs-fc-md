package metrics.metrics;

import java.util.ArrayList;

import metrics.util.Filters;
import metricsdata.IntegerArrayValueMetric;

import org.apache.log4j.Logger;

import recoder.abstraction.ClassType;
import recoder.abstraction.Constructor;
import recoder.abstraction.Method;
import recoder.convenience.TreeWalker;
import recoder.csharp.ProgramElement;
import recoder.csharp.SourceElement;
import recoder.csharp.StatementBlock;
import recoder.service.CrossReferenceSourceInfo;

/**
 * A metric that calculates the lines of code per class.
 */
public class LOCC extends DSMetricCalculator {

	//private IntegerArrayValueMetric result;
	
	/**
	 * Constructur
	 * 
	 * @param types an ArrayList of CompilationUnits to calculate
	 * @param si the CrossReferenceSourceInfo
	 */
	public LOCC(ArrayList<ProgramElement> types, CrossReferenceSourceInfo si) {
		this.si = si;
		this.types = types;
		setInfo();
	}
	
	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(LOCC.class);


	/**
	 * constructor
	 * automatically sets the information for metric:
	 * shortcut, fullName, description
	 */
	public LOCC() {
		setInfo();
	}


	/**
	 * sets the information for metric:
	 * shortcut, fullName, description
	 */
	private void setInfo() {
		this.shortcut = "DS_LOCC";
		this.fullName = "Lines of Code (Class)";
		this.description = "Number of lines of code of a class, including blank lines, comments, parenthesis, and method header. But without java doc header.";
	}
	
	/**
	 * calculate metric result
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
				/*
				 * if the current method is a constructor, we need to handle
				 * things diffenretly. since recoder can not correctly find the
				 * starting line for a method if it is a constructor. instead
				 * the beginning line of the methods statement block +1 is used.
				 * this should be correct in most cases.
				 */
				if (pe instanceof Constructor) {
					TreeWalker jhonnieWalker = new TreeWalker(pe);

					while (jhonnieWalker.next()) {
						ProgramElement constructorChild = jhonnieWalker
								.getProgramElement();
						if (constructorChild instanceof StatementBlock) {
							// start with the line of the method head
							SourceElement.Position start = (constructorChild)
									.getStartPosition();
							/*
							 * here we assume, that the opening of the methods
							 * StatementBlock is on the line after the method
							 * definition and thus substract 1.
							 */
							start.setLine(start.getLine() - 1);
							// end with the last curly brace of method.
							SourceElement.Position end = (constructorChild)
									.getEndPosition();
							line = (end.getLine() - start.getLine()) + 1;
							cnt = line + cnt;

							log.debug("CONSTRUCTOR! TYPE: "
									+ ((ClassType) clazz).getName()
									+ " :: METHOD: " + ((Method) pe).getName()
									+ " :: LOCM: " + cnt + " :: START: "
									+ start.getLine() + " :: END: "
									+ end.getLine());
						}
					}
					// not a constructor but a normal method.
				} else {

					// start with the line of the method head
					SourceElement.Position start = (pe).getStartPosition();
					// end with the last curly brace of method.
					SourceElement.Position end = (pe).getEndPosition();
					line = (end.getLine() - start.getLine()) + 1;
					cnt = line + cnt;

					log.debug("TYPE: " + ((ClassType) clazz).getName()
							+ " :: METHOD: " + ((Method) pe).getName()
							+ " :: LOCM: " + cnt + " :: START: "
							+ start.getLine() + " :: END: " + end.getLine());
				}
			}
			res.add(cnt);
		}
		// set result of metric
		this.result = new IntegerArrayValueMetric(res, this.shortcut, this.fullName, this.description);

	}
}