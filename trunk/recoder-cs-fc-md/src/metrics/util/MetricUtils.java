package metrics.util;

import java.util.ArrayList;

import metricsdata.AbstractMetricAttribute;

import fcMDtests.util.FileSlurper;

import recoder.abstraction.DeclaredType;
import recoder.abstraction.ClassType;
import recoder.abstraction.Type;
import recoder.convenience.TreeWalker;
import recoder.csharp.CompilationUnit;
import recoder.csharp.ProgramElement;

/**
 * This class holds methods that re acommonly used throughout metrics framework
 * 
 * @author Jan Schumacher, jansch@gmail.com
 */
public final class MetricUtils {
	
	/**
	 * Walk CU and return all ClassTypes, ordinary classes only
	 * 
	 * @param cu
	 *            the CU to search for types
	 * @return ArrayList<ClassType> - an array list with the ClassTypes found
	 * 
	 * @author Jan Schumacher, jansch@gmail.com
	 */
	public static ArrayList<ProgramElement> collectClasses(
			CompilationUnit cu) {

		ArrayList<ProgramElement> res = new ArrayList<ProgramElement>();

		TreeWalker johnnieWalker = new TreeWalker(cu);

		// walk tree and look for pes in cus that that re ClassTypes
		while (johnnieWalker.next(Filters.CLASS_FILTER_EXCL_INTERFACE)) {
			ProgramElement e = johnnieWalker.getProgramElement();
			assert e instanceof ClassType;
			res.add(e);
		}
		return res;
	}
	
	public static void printMatricResult (AbstractMetricAttribute result) {
		
	}
}
