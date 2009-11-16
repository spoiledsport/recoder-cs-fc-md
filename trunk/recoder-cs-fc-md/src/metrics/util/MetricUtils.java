package metrics.util;

import java.util.ArrayList;
import java.util.HashMap;

import metricsdata.AbstractMetricAttribute;

import org.apache.log4j.Logger;

import recoder.abstraction.ClassType;
import recoder.abstraction.Field;
import recoder.abstraction.Method;
import recoder.convenience.TreeWalker;
import recoder.csharp.CompilationUnit;
import recoder.csharp.ProgramElement;
import recoder.csharp.declaration.EnumDeclaration;
import recoder.csharp.declaration.TypeDeclaration;
import recoder.csharp.expression.operator.Conditional;
import recoder.csharp.reference.MemberReference;
import recoder.csharp.reference.MethodReference;
import recoder.csharp.statement.Catch;
import recoder.csharp.statement.Do;
import recoder.csharp.statement.For;
import recoder.csharp.statement.Foreach;
import recoder.csharp.statement.If;
import recoder.csharp.statement.Switch;
import recoder.csharp.statement.Try;
import recoder.csharp.statement.While;
import recoder.list.FieldList;
import recoder.service.CrossReferenceSourceInfo;
import fcMDtests.metricTests.DS_WMC_Test;

/**
 * This class holds methods that are commonly used throughout metrics framework
 * 
 * @author Jan Schumacher, jansch@gmail.com
 */
public final class MetricUtils {

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(DS_WMC_Test.class);

	/**
	 * Walk CU and return all ClassTypes, ordinary classes only
	 * 
	 * @param cu
	 *            the CU to search for types
	 * @return ArrayList<ClassType> - an array list with the ClassTypes found
	 * 
	 * @author Jan Schumacher, jansch@gmail.com
	 */
	public static ArrayList<ProgramElement> collectClasses(CompilationUnit cu) {

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

	/**
	 * Debug and Test Output of the metric results (mainly for stand-alone
	 * usage)
	 */
	public static String debugOutput(
			HashMap<String, HashMap<String, AbstractMetricAttribute>> metricResults) {
		String result = "";
		for (String cuName : metricResults.keySet()) {
			result += "Calculated results for class: " + cuName + "\n";
			for (String metricName : metricResults.get(cuName).keySet()) {
				result += metricName + ": "
						+ metricResults.get(cuName).get(metricName) + "\n";
			}
		}
		return result;
	}

	/**
	 * Check, if the specified method reference is a setter/getter
	 * 
	 * @param methodReference
	 * @param cu
	 * @param si
	 * @return boolean
	 */
	public static boolean isGetterSetterCall(MethodReference mr, ClassType cu,
			CrossReferenceSourceInfo si) {
		ClassType originClassType = (si.getMethod(mr)).getContainingClassType();

		if (originClassType.equals(cu)) {
			return false;
		}

		FieldList fsl = originClassType.getFields();

		for (int i = 0; i < fsl.size(); i++) {
			if (mr.getName().toLowerCase().matches(
					"get" + fsl.getField(i).getName().toLowerCase())) {
				return true;
			} else if (mr.getName().toLowerCase().matches(
					"set" + fsl.getField(i).getName().toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns the field accessed by a MethodReference that is a getter setter
	 * 
	 * @param method
	 *            reference
	 * @param classType
	 * @param sourceinfo
	 * @return Field
	 */
	public static Field getAccessedField(MethodReference mr, ClassType cu,
			CrossReferenceSourceInfo si) {

		ClassType originClassType = (si.getMethod(mr)).getContainingClassType();

		FieldList fsl = originClassType.getFields();

		// loop over fields
		for (int i = 0; i < fsl.size(); i++) {
			if (mr.getName().toLowerCase().matches(
					"get" + fsl.getField(i).getName().toLowerCase())) {
				return (Field) fsl.getField(i);
			} else if (mr.getName().toLowerCase().matches(
					"set" + fsl.getField(i).getName().toLowerCase())) {
				return (Field) fsl.getField(i);
			}
		}

		return null;
	}

	/**
	 * Check if the fields in foreignFields are not defined by SuperType
	 * superType
	 * 
	 * @param foreignFields
	 * @param fieldList
	 * @return ArrayList<Field>
	 */
	public static ArrayList<Field> getUnrelatedFields(
			ArrayList<Field> foreignFields, ClassType superType) {
		FieldList fieldList = superType.getFields();

		ArrayList<Field> puffer = new ArrayList<Field>();

		// loop over all foreign fields found
		for (Field foreign : foreignFields) {
			// foreign in relative classes found?
			boolean related = false;

			// loop over all fields in list
			for (int i = 0; i < fieldList.size(); i++) {
				if (fieldList.getField(i) == foreign)
					related = true;
			}
			if (!related) {
				puffer.add(foreign);
			}
		}
		return puffer;
	}
	
	/**
	 * Get the parent class declaration from a Method Reference
	 * @param mthdref
	 * @return TypeDeclaration
	 */
	public static TypeDeclaration getParentClassDeclaration(MemberReference mthdref) {
		ProgramElement p = (ProgramElement) mthdref;
	
		do {
		    p = p.getASTParent();
			
		    if (p instanceof ClassType || p instanceof EnumDeclaration) {
		    	return (TypeDeclaration) p;
		    }
		} while (p != null);
		return null;
	}

	/**
	 * calculate the cyclomatic complexity of a method
	 * 
	 * @param e
	 *            the method to calculate the cyclo for
	 */
	public static int cycloMethod(ProgramElement e) {
		int cyclo = 0;

		// is this really a method
		assert e instanceof Method;
		String method = ((Method) e).getName();
		log.debug("calculating CYCLO for method: " + method);

		TreeWalker methodwalker = new TreeWalker(e);

		while (methodwalker.next()) {
			try {
				ProgramElement pe = methodwalker.getProgramElement();
				if (pe instanceof For) {
					log.debug("found for");
					cyclo++;
				} else if (pe instanceof While) {
					log.debug("found while");
					cyclo++;
				} else if (pe instanceof Do) {
					log.debug("found do");
					cyclo++;
				} else if (pe instanceof If) {
					log.debug("found if");
					cyclo++;
				} else if (pe instanceof Switch) {
					log.debug("found switch");
					cyclo++;
				} else if (pe instanceof Conditional) {
					log.debug("found conditional");
					cyclo++;
				} else if (pe instanceof Try) {
					log.debug("found try");
					cyclo++;
				} else if (pe instanceof Catch) {
					log.debug("found catch");
					cyclo++;
				} else if (pe instanceof Foreach) {
					log.debug("found foreach");
					cyclo++;
				}
			} catch (NullPointerException ex) {
				System.out
						.println("Warning - Util(3) - NullPointer - continue");
				continue;
			}
		}
		log.debug("CYCLO for method: " + method + " is " + cyclo);
		return cyclo + 1;
	}
}
