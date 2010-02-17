package metrics.metrics;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import metrics.util.Filters;
import metricsdata.IntegerArray2ValueMetric;
import recodercs.abstraction.ClassType;
import recodercs.abstraction.Constructor;
import recodercs.abstraction.Method;
import recodercs.convenience.TreeWalker;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.SourceElement;
import recodercs.csharp.StatementBlock;
import recodercs.service.CrossReferenceSourceInfo;

/**
 * A metric that calculates the lines of code per method of a class
 */
public class LOCM extends DSMetricCalculator {


	/**
	 * Constructur
	 * 
	 * @param types an ArrayList of CompilationUnits to calculate
	 * @param si the CrossReferenceSourceInfo
	 */
	public LOCM(ArrayList<ProgramElement> types, CrossReferenceSourceInfo si) {
		this.si = si;
		this.types = types;
		setInfo();
	}

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(LOCM.class);
	
	/**
	 * constructor
	 * automatically sets the information for metric:
	 * shortcut, fullName, description
	 */
	public LOCM() {
		setInfo();
	}

	/**
	 * sets the information for metric:
	 * shortcut, fullName, description
	 */
	private void setInfo() {
		this.shortcut = "DS_LOCM";
		this.fullName = "Lines of Code (Method)";
		this.description = "Number of lines of code of a Method, including blank lines, comments, parenthesis, and method header. But without java doc header.";
	}

	/**
	 * Main calculation for this metric.
	 */
	public void calculate() {
		log.debug("Calculating metric: LOCM!");
		
		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
		int line = 0;
		int cnt = 0;

		// loop over all classes in current CU
		for (ProgramElement clazz : types) {
			assert clazz instanceof ClassType;
			TreeWalker luke = new TreeWalker(clazz);

			ArrayList<Integer> typeRes = new ArrayList<Integer>();

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
							typeRes.add(cnt);

							log.debug("CONSTRUCTOR! TYPE: "
									+ ((ClassType) clazz).getName()
									+ " :: METHOD: " + ((Method) pe).getName()
									+ " :: LOCM: " + cnt + " :: START: "
									+ start.getLine() + " :: END: "
									+ end.getLine());
							cnt = 0;
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
					typeRes.add(cnt);
					
					log.debug("TYPE: " + ((ClassType) clazz).getName()
							+ " :: METHOD: " + ((Method) pe).getName()
							+ " :: LOCM: " + cnt + " :: START: "
							+ start.getLine() + " :: END: " + end.getLine());
					cnt = 0;
				}
				
			}
			if (typeRes.size() == 0) typeRes.add(0); 
			res.add(typeRes);

		}
		// set result for metric
		this.result = new IntegerArray2ValueMetric(res, shortcut, fullName,
				description);

	}
}