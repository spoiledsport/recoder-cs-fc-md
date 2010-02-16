package metrics.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import metricsdata.AbstractMetricAttribute;

import org.apache.log4j.Logger;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import recoder.abstraction.ClassType;
import recoder.abstraction.Field;
import recoder.abstraction.Method;
import recoder.convenience.TreeWalker;
import recoder.csharp.CompilationUnit;
import recoder.csharp.ProgramElement;
import recoder.csharp.Reference;
import recoder.csharp.StatementBlock;
import recoder.csharp.declaration.EnumDeclaration;
import recoder.csharp.declaration.MemberDeclaration;
import recoder.csharp.declaration.MethodDeclaration;
import recoder.csharp.declaration.TypeDeclaration;
import recoder.csharp.expression.operator.Conditional;
import recoder.csharp.expression.operator.New;
import recoder.csharp.reference.ConstructorReference;
import recoder.csharp.reference.FieldReference;
import recoder.csharp.reference.MemberReference;
import recoder.csharp.reference.MethodReference;
import recoder.csharp.reference.SuperConstructorReference;
import recoder.csharp.reference.ThisConstructorReference;
import recoder.csharp.reference.ThisReference;
import recoder.csharp.reference.TypeReference;
import recoder.csharp.reference.VariableReference;
import recoder.csharp.statement.Catch;
import recoder.csharp.statement.Do;
import recoder.csharp.statement.For;
import recoder.csharp.statement.Foreach;
import recoder.csharp.statement.If;
import recoder.csharp.statement.Return;
import recoder.csharp.statement.Switch;
import recoder.csharp.statement.Try;
import recoder.csharp.statement.While;
import recoder.list.FieldList;
import recoder.list.MethodList;
import recoder.service.CrossReferenceSourceInfo;

/**
 * This class holds methods that are commonly used throughout metrics framework
 * 
 * @author Jan Schumacher, jansch@gmail.com
 */
public final class MetricUtils {

	/**
	 * the log4j logger
	 */
	static Logger log = Logger.getLogger(MetricUtils.class);

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
	 * Checking, if the specificed method is a getter/setter only checks if
	 * method begins with getSet, is longer then 3 chars
	 * 
	 * @param method
	 * @return boolean
	 */
	public static boolean isGetterSetterSimple(Method method) {
		boolean res = false;

		String name = method.getName();

		if (name.length() >= 3) {
			if (name.toLowerCase().startsWith("get")) {
				res = true;
			} else if (name.toLowerCase().startsWith("set")) {
				return true;
			}
		}
		return res;
	}

	/**
	 * Checking, if the specificed method is a getter/setter
	 * 
	 * @param method
	 * @return boolean
	 */
	public static boolean isGetterSetter(Method method) {
		boolean res = false;

		if (method.getName().length() >= 3) {
			if (isGetter(method) || isSetter(method)) {
				res = true;
			}
		}
		return res;
	}

	/**
	 * It is a util method for the method isGetterSetter, check if a method is
	 * setter
	 * 
	 * @param m
	 * @return boolean
	 */
	public static boolean isSetter(Method m) {
		String sub = m.getName().substring(0, 3);
		if ((sub.equals("set")) && setterUtility1(m)) {

			if (setterUtility1(m) && setterUtility2(m)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Util method for the method isSetter, check if a method is setter
	 * 
	 * @param m
	 * @return boolean
	 */
	private static boolean setterUtility1(Method m) {
		// List<FieldSpecification> classAttributes = (List<FieldSpecification>)
		// m.getContainingClassType().getFields();
		FieldList classAttributes = m.getContainingClassType().getFields();

		if (m.getReturnType() == null && m.getSignature().size() == 1) {

			TreeWalker walker = new TreeWalker((ProgramElement) m);
			walker.next();

			while (walker.next()) {
				try {
					ProgramElement pe = walker.getProgramElement();

					if (pe instanceof FieldReference) {

						for (int i = 0; i < classAttributes.size(); i++) {
							Field fsp = classAttributes.getField(i);

							if (fsp.getName().equals(
									((FieldReference) pe).getName())) {

								if (fsp.getType() == m.getSignature()
										.getType(0)) {
									return true;
								}
							}
						}
					}
				} catch (NullPointerException e) {
					System.out
							.println("Warning - Util(7) - NullPointer - continue");
					continue;
				}
			}
		}
		return false;
	}

	/**
	 * Util method for the method isSetter, check if a method is setter
	 * 
	 * @param m
	 * @return boolean
	 */
	private static boolean setterUtility2(Method m) {

		TreeWalker walker = new TreeWalker((ProgramElement) m);
		walker.next();

		while (walker.next()) {
			try {
				ProgramElement pe = walker.getProgramElement();

				if (pe instanceof StatementBlock) {
					walker.next();
					walker.next();
					pe = walker.getProgramElement();

					if (pe instanceof FieldReference) {

						walker.next();
						pe = walker.getProgramElement();

						if (pe instanceof ThisReference) {
							walker.next();
							walker.next();
							pe = walker.getProgramElement();

							if (pe instanceof VariableReference) {
								walker.next();
								walker.next();
								pe = walker.getProgramElement();

								if (pe == null) {
									return true;
								}
							}

						} else
							walker.next();
						pe = walker.getProgramElement();
						if (pe instanceof VariableReference) {
							return true;
						}
					}
				}
			} catch (NullPointerException e) {
				System.out
						.println("Warning - Util(8) - NullPointer - continue");
				continue;
			}
		}
		return false;
	}

	/**
	 * It is a util method for the method isGetterSetter, check if a method is a
	 * getter
	 * 
	 * @param m
	 * @return boolean
	 */
	public static boolean isGetter(Method m) {

		String sub = m.getName().substring(0, 3);

		if ((sub.equals("get")) && m.getSignature().isEmpty()
				&& getterUtility1(m)) {
			return true;
		}
		return false;
	}

	/**
	 * Util method for the method isGetter, check if a method is getter
	 * 
	 * @param m
	 * @return boolean
	 */
	private static boolean getterUtility1(Method m) {
		TreeWalker walker = new TreeWalker((ProgramElement) m);
		walker.next();
		while (walker.next()) {
			try {
				ProgramElement pe = walker.getProgramElement();
				if (pe instanceof StatementBlock) {
					walker.next();
					pe = walker.getProgramElement();
					if (pe instanceof Return) {
						if (getterUtility2(m)) {
							walker.next();
							pe = walker.getProgramElement();
							if (pe instanceof FieldReference) {
								return true;
							}
						}
					}
				}
			} catch (NullPointerException e) {
				System.out
						.println("Warning - Util(4) - NullPointer - continue");
				continue;
			}
		}
		return false;
	}

	/**
	 * Util method for the method isGetter, check if a method is getter
	 * 
	 * @param m
	 * @return boolean
	 */
	private static boolean getterUtility2(Method m) {

		// List<FieldSpecification> classAttributes = (List<FieldSpecification>)
		// m.getContainingClassType().getFields();
		FieldList classAttributes = m.getContainingClassType().getFields();

		TreeWalker walker = new TreeWalker((ProgramElement) m);
		walker.next();

		while (walker.next()) {
			try {
				ProgramElement pe = walker.getProgramElement();
				if (pe instanceof FieldReference) {
					for (int i = 0; i < classAttributes.size(); i++) {
						Field fsp = classAttributes.getField(i);
						if (fsp.getName().equals(
								((FieldReference) pe).getName())) {

							if (fsp.getType() == m.getReturnType()) {
								return true;
							}

						}
					}
				}
			} catch (NullPointerException e) {
				System.out
						.println("Warning - Util(5) - NullPointer - continue");
				continue;
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
	 * 
	 * @param mthdref
	 * @return TypeDeclaration
	 */
	public static TypeDeclaration getParentClassDeclaration(
			MemberReference mthdref) {
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

	/**
	 * Get the parent member declaration from a Method Reference
	 * 
	 * @param mthdref
	 * @return MemberDeclaration
	 */
	public static MemberDeclaration getParentMemberDeclaration(Reference mthdref) {
		ProgramElement p = (ProgramElement) mthdref;
		do {
			p = p.getASTParent();
			if (p instanceof MethodDeclaration) {
				return (MemberDeclaration) p;
			}
		} while (p != null);

		return null;
	}

	/**
	 * extracts all Methodrefernces from a Method. does not consider
	 * ConstructorReferences from related classes: SuperConstructorReference,
	 * ThisConstructorReference Used by: CDISP, CINT
	 * 
	 * @param Method
	 *            method
	 * @return List<Reference>
	 */
	public static ArrayList<Reference> getMethodReferences(Method method) {
		ArrayList<Reference> disMethods = new ArrayList<Reference>();

		TreeWalker walker = new TreeWalker((ProgramElement) method);
		// dont count the methods itself
		walker.next();
		while (walker.next()) {
			try {
				ProgramElement pe = walker.getProgramElement();
				if (pe instanceof ConstructorReference
						&& !(pe instanceof SuperConstructorReference)
						&& !(pe instanceof ThisConstructorReference)) {

					disMethods.add((((New) pe).getTypeReference()));
				} else if ((pe instanceof MethodReference)) {
					disMethods.add((MethodReference) pe);
				}
			} catch (NullPointerException e) {
				System.out
						.println("Warning - Util(6) - NullPointer - continue");
				continue;
			}
		}
		return disMethods;
	}

	/**
	 * Return all the method and constructurs referens, which are related with
	 * the measured class or in the same hierarchy as the measured class
	 * 
	 * @param ArrayList
	 *            <Reference>
	 * @param List
	 *            <Method>
	 * @return ArrayList<Reference>
	 */
	protected static ArrayList<Reference> getRelatedMethods(
			ArrayList<Reference> foreignmethods, List<? extends Method> list) {
		ArrayList<Reference> puffer = new ArrayList<Reference>();

		for (Reference foreign : foreignmethods) {

			for (Method parentMethod : list) {

				if (foreign instanceof MethodReference) {

					if (parentMethod.getName().equals(
							((MethodReference) foreign).getName())) {
						puffer.add(foreign);
					}
				} else if (parentMethod.getName().equals(
						((TypeReference) foreign).getName())) {

					puffer.add(((TypeReference) foreign));
				}

			}
		}
		return puffer;
	}

	/**
	 * Return all the method and constructurs referens, which are related with
	 * the measured class or in the same hierarchy as the measured class
	 * 
	 * @param ArrayList
	 *            <Reference>
	 * @param List
	 *            <Method>
	 * @return ArrayList<Reference>
	 */
	public static ArrayList<Reference> getRelatedMethods(
			ArrayList<Reference> foreignmethods, MethodList superTypeMethods) {
		ArrayList<Reference> puffer = new ArrayList<Reference>();

		for (Reference foreign : foreignmethods) {

			for (int i = 0; i < superTypeMethods.size(); i++) {
				Method parentMethod = superTypeMethods.getMethod(i);

				if (foreign instanceof MethodReference) {

					if (parentMethod.getName().equals(
							((MethodReference) foreign).getName())) {
						puffer.add(foreign);
					}
				} else if (parentMethod.getName().equals(
						((TypeReference) foreign).getName())) {

					puffer.add(((TypeReference) foreign));
				}
			}
		}
		return puffer;
	}

	/**
	 * Debug and Test Output of the metric results (mainly for stand-alone
	 * usage)
	 * @throws IOException 
	 */
	public static void csvOutput(
			HashMap<String, HashMap<String, AbstractMetricAttribute>> metricResults, File outPutFile, Boolean classSums) throws IOException {
		
		// setup writer for csv file
		ICsvListWriter writer = new CsvListWriter(new FileWriter(outPutFile), CsvPreference.EXCEL_PREFERENCE);


		try {
			// figure out how many columns we are going to need
			Entry<String, HashMap<String, AbstractMetricAttribute>> firstResult = metricResults
					.entrySet().iterator().next();
			HashMap<String, AbstractMetricAttribute> firstMetricResult = firstResult
					.getValue();

			int columnsSize = firstMetricResult.size() + 1;

			// the String[] with the names of the header
			String[] header = new String[columnsSize];

			// first column holds the file name
			String firstColumn = "FILE";
			header[0] = firstColumn;

			// get the names of the metrics calculated
			int headerPosition = 1;
			Iterator<String> it = firstMetricResult.keySet().iterator();
			while (it.hasNext()) {
				header[headerPosition++] = it.next();
			}

			// write csv file header
			writer.writeHeader(header);
			
			// loop over files calculated
			Iterator<String> fileNameItr = metricResults.keySet().iterator();
			while (fileNameItr.hasNext()) {
				String[] row = new String[columnsSize];
				int rowPosition = 0;
				String fileName = fileNameItr.next();
				row[rowPosition++] = fileName;

				// get metrics for fileName
				HashMap<String, AbstractMetricAttribute> metrics = metricResults
						.get(fileName);
				for (String metricName : header) {
					if (!(metricName.equals(firstColumn))) {
						row[rowPosition++] = maxOfMetricString(metrics.get(metricName).toString()) + "";
						//row[rowPosition++] = metrics.get(metricName).toString();

						
					}
					
				}
				// write row
				writer.write(Arrays.asList(row));
				
			}
		} finally {
			writer.close();
		}

	}
	
	public static String maxOfMetricString(String metrics) {
		
		log.debug("Metrics: " + metrics);
		
			metrics = metrics.replace("[", "");
			metrics = metrics.replace("]", "");


		log.debug("Metrics replaced : " + metrics);
		StringTokenizer tok = new StringTokenizer(metrics,",");
		List<Double> maxor= new ArrayList<Double>();
		
		// add values
		while (tok.hasMoreTokens()) {
			String next = tok.nextToken();
			if (!next.equals("Infinity") && !next.equals(null) && !next.equals("")) {
				maxor.add(Double.parseDouble(next));
			}
			
		}
		
		log.debug("Metrics List: " + maxor);
		
		// determin max
		double currentMax = Double.MIN_VALUE;
		for (Double d : maxor) {
			if (d > currentMax)
				currentMax = d;
		}
		
		log.debug("MAX: " + currentMax);
		
		
		return currentMax + "";
	}
	
}
