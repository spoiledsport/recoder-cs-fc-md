package examples.fcMdExamples;

import java.io.IOException;

import recoder.CrossReferenceServiceConfiguration;
import recoder.ParserException;
import recoder.abstraction.ClassType;
import recoder.abstraction.Field;
import recoder.abstraction.Method;
import recoder.convenience.TreeWalker;
import recoder.csharp.CSharpSourceElement;
import recoder.csharp.CompilationUnit;
import recoder.csharp.ProgramElement;
import recoder.csharp.StatementBlock;
import recoder.csharp.SourceElement.Position;
import recoder.csharp.declaration.ClassDeclaration;
import recoder.csharp.declaration.EnumDeclaration;
import recoder.csharp.declaration.MethodDeclaration;
import recoder.csharp.declaration.TypeDeclaration;
import recoder.csharp.expression.operator.Conditional;
import recoder.csharp.reference.FieldReference;
import recoder.csharp.reference.MemberReference;
import recoder.csharp.reference.MethodReference;
import recoder.csharp.statement.Catch;
import recoder.csharp.statement.Do;
import recoder.csharp.statement.For;
import recoder.csharp.statement.If;
import recoder.csharp.statement.Switch;
import recoder.csharp.statement.Then;
import recoder.csharp.statement.Try;
import recoder.csharp.statement.While;
import recoder.list.ClassTypeList;
import recoder.list.CompilationUnitList;
import recoder.list.FieldList;
import recoder.list.MemberReferenceList;
import recoder.list.MethodList;
import recoder.service.CrossReferenceSourceInfo;
import simpleExp.PlainAnalysisErrorHandler;
import simpleExp.RecoderProgram;

/**
 * @author AL
 */
public class ReferenceTests {

	CrossReferenceServiceConfiguration cs;
	CrossReferenceSourceInfo si;
	CompilationUnitList units;

	protected ReferenceTests(String[] args) {
		System.getProperties().put("input.path",
				"test/personExp:test/minicorlib");

		// setup cross reference service configuration and source info
		// TODO: what is the difference, what do they do?
		this.cs = new CrossReferenceServiceConfiguration();
		this.si = cs.getCrossReferenceSourceInfo();

		// set custom error handler
		this.cs.getProjectSettings().setErrorHandler(
				new PlainAnalysisErrorHandler());

		// run recoder program
		RecoderProgram.setup(cs, ReferenceTests.class, args);

		// get compilation units
		this.units = cs.getSourceFileRepository().getCompilationUnits();
	}

	public static void main(String[] args) throws IOException, ParserException,
			Exception {
		ReferenceTests rt = new ReferenceTests(args);
		rt.walkTree();
	}

	public void walkTree() {
		// loop over all cus
		for (int i = 0, s = this.units.size(); i < s; i += 1) {
			// get next compilation unit
			CompilationUnit cu = this.units.getCompilationUnit(i);

			// tree walker to walk the AST of cu
			TreeWalker johnnieWalker = new TreeWalker(cu);

			// let tree walker walt the AST of cu
			while (johnnieWalker.next()) {
				// get current tree node
				ProgramElement e = johnnieWalker.getProgramElement();

				if (e instanceof FieldReference) {
					// this.fieldRefFound(e);
				} else if (e instanceof MethodReference) {
					// this.methodRefFound(e);
				} else if (e instanceof ClassType) {
					this.classFound(e);
				} else if (e instanceof Method) {
					// this.methodFound(e);
				}
			}

		}
	}

	// this is run, whenever a method is found
	private void methodFound(ProgramElement e) {
		Method m = (Method) e;
		System.out.println("found method: " + m.getFullName());
		// System.out.println("LOC: " + this.methodLoc(e));
		// System.out.println("CYCLO: " + this.cycloMethod(e));
		// System.out.println("overriding method is class: ");
		// getOveriddenSupTMethodos(e);
		// System.out.println("changing classes: ");
		// this.changingClass(e);
		// System.out.println("max nesting for method: ");
		// System.out.println(this.calculateMaxLevel((Method)e));
		System.out.println("added service? " + this.addedService(e));

	}

	// is a method an edded service
	public boolean addedService(ProgramElement e) {
		Method m = (Method) e;

		for (int i = 1; i < m.getContainingClassType().getAllSupertypes()
				.size(); i++) {
			// allParentMethods are all methods of the parentclass from measured
			// class
			MethodList allParentMethods = m.getContainingClassType()
					.getAllSupertypes().getClassType(i).getMethods();

			// check if method overrides something in super type
			for (int j = 0; j < allParentMethods.size(); j++) {
				if (this.checkParameter(m, allParentMethods.getMethod(j)))
					return false;
			}
		}
		return true;
	}

	// determin max nesting lvl for a method
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

	// helper for max nesting
	// are we still in the loop
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

	// helper for max nesting
	// are we gettin ginto a loop
	private boolean isLoop(ProgramElement pe) {
		if (pe instanceof For) {
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

	// for a method determin the classes it is called from
	public void changingClass(ProgramElement e) {
		Method m = (Method) e;

		// get memeber references
		MemberReferenceList ml = this.si.getReferences(m);

		// interate over all memeber references
		for (int i = 0; i < ml.size(); i++) {
			MemberReference mr = ml.getMemberReference(i);
			System.out.println("found memeber reference: " + mr.toSource());

			// determin parent class
			TypeDeclaration parentClass = this
					.getParentClassDeclaration((ProgramElement) mr);

			System.out.println("Calling from class: "
					+ parentClass.getFullName());

		}

	}

	/**
	 * Get the parent class declaration from a MemberReference
	 * 
	 * @param mthdref
	 * @return TypeDeclaration
	 */
	public TypeDeclaration getParentClassDeclaration(ProgramElement e) {
		do {
			e = e.getASTParent();

			if (e instanceof ClassType || e instanceof EnumDeclaration
					|| e instanceof ClassDeclaration) {
				return (TypeDeclaration) e;
			}
		} while (e != null);
		return null;
	}

	// for a method determin if it overrides methods in the supertype
	public void getOveriddenSupTMethodos(ProgramElement e) {
		Method m = (Method) e;
		ClassType containingClass = this.si.getContainingClassType(e);
		System.out.println("I am Method: " + m.getName()
				+ ", I belong to Class: " + containingClass.getFullName());

		ClassTypeList superTypes = containingClass.getSupertypes();

		System.out.println("number of supertypes for class "
				+ containingClass.getFullName() + ": " + superTypes.size());

		// loop over super types an check if method was overridden
		for (int i = 0; i < superTypes.size(); i++) {
			ClassType superType = superTypes.getClassType(i);
			System.out.println("found super type: " + superType.getFullName());

			// get the supertypes methods
			MethodList supTyMethods = superType.getMethods();
			System.out.println("super type has # methods: "
					+ supTyMethods.size());

			// iterate over super types methods
			for (int j = 0; j < supTyMethods.size(); j++) {
				Method superMethod = supTyMethods.getMethod(j);
				System.out.println("Supertype has method: "
						+ superMethod.getFullName());
				System.out.println("overriding: "
						+ this.checkParameter(m, superMethod));
			}

		}

	}

	/**
	 * 
	 * Check the parameter size and the typs of each parameter. Return true,if
	 * they are same as well in ChildClass as in Parentclass
	 * 
	 * @param Method
	 *            methodlist
	 * @param Method
	 *            paMethod
	 * @return boolean
	 */
	private boolean checkParameter(Method m, Method superMethod) {

		if (m.getName().equals(superMethod.getName())
				&& m.getReturnType() == superMethod.getReturnType()
				&& m.getSignature().size() == superMethod.getSignature().size()) {
			if (m.getSignature().size() == 0) {
				return true;
			}
			for (int i = 0; i < m.getSignature().size(); i++) {

				// TODO: does this work?
				if (m.getSignature().getType(i) == superMethod.getSignature()
						.getType(i)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Check, if the accessed method referens is a getter/setter
	 * 
	 * @param metref
	 * @return boolean
	 */
	private boolean isMethodReferesGetterSetter(MethodReference metref) {

		if (metref.getName().length() < 3) {
			return false;
		}
		String sub = metref.getName().substring(0, 3);
		if ((sub.equals("get")) || (sub.equals("set"))) {
			return true;
		}
		return false;
	}

	// for a method calculate cyclo complex
	private int cycloMethod(ProgramElement e) {
		int cyclo = 0;
		TreeWalker methodwalker = new TreeWalker(e);

		while (methodwalker.next()) {
			try {
				ProgramElement pe = methodwalker.getProgramElement();
				if (pe instanceof For) {
					cyclo++;
				} else if (pe instanceof While) {
					cyclo++;
				} else if (pe instanceof Do) {
					cyclo++;
				} else if (pe instanceof If) {
					cyclo++;
				} else if (pe instanceof Switch) {
					cyclo++;
				} else if (pe instanceof Conditional) {
					cyclo++;
				} else if (pe instanceof Try) {
					cyclo++;
				} else if (pe instanceof Catch) {
					cyclo++;
				}
			} catch (NullPointerException ex) {
				System.out
						.println("Warning - Util(3) - NullPointer - continue");
				continue;
			}
		}

		return cyclo;
	}

	// calculate lines of code for a method
	public int methodLoc(ProgramElement e) {
		// Make sure we really found a Method
		assert e instanceof Method;

		// start with the line of the method head
		Position start = ((CSharpSourceElement) e).getStartPosition();
		// end with the last curly brace of method.
		Position end = ((CSharpSourceElement) e).getEndPosition();

		return (end.getLine() - start.getLine()) + 1;
	}

	private void classFound(ProgramElement e) {
		ClassType clazz = (ClassType) e;
		System.out.println("\nfound class : " + clazz.getName());
		System.out.println("interface: " + clazz.isInterface());
		System.out.println("class has supertypes: ");
		this.classSupertypes(e);
		System.out.println("fields in the class");
		this.getClassFields(e);

	}

	// print all fields of a class
	private void getClassFields(ProgramElement e) {
		ClassType clazz = (ClassType) e;
		FieldList fields = this.si.getAllFields(clazz);

//		// walk tree and look for properties
//		TreeWalker propertywalker = new TreeWalker(e);
//
//		while (propertywalker.next()) {
//			try {
//				ProgramElement pe = propertywalker.getProgramElement();
//				if (pe instanceof Property) {
//					Property pro = (Property) e;
//					System.out.println("property: " + pro.getName());
//				}
//			} catch (NullPointerException ex) {
//				System.out
//						.println("Warning - Util(3) - NullPointer - continue");
//				continue;
//			}
//		}
		for (int i = 0; i < fields.size(); i++) {
			System.out.println("field: " + fields.getField(i).getName());
		}
	}

	// print out super types for a class
	private void classSupertypes(ProgramElement e) {
		ClassType claszz = (ClassType) e;
		ClassTypeList ctl = claszz.getSupertypes();

		for (int i = 0; i < ctl.size(); i++) {
			ClassType ct = ctl.getClassType(i);
			System.out.println("found superType: " + ct.getFullName());
		}

	}

	// this is run, when a method reference is found
	private void methodRefFound(ProgramElement e) {
		MethodReference mr = (MethodReference) e;
		System.out.println("found methRef: " + mr.getName());
		System.out.println("getter/setter? ... "
				+ isMethodReferesGetterSetter(mr));

		// System.out.println("method is private? "
		// + si.getMethod((MethodReference) e).isPrivate());
		// System.out.println("method is public? "
		// + si.getMethod((MethodReference) e).isPublic());
		// System.out.println("method is static? "
		// + si.getMethod((MethodReference) e).isStatic());
		// // this does not work
		// ProgramElement parent = e.getASTParent();
		// System.out.println("this method belongs to class: " + e.toSource());
	}

	public void fieldRefFound(ProgramElement e) {
		FieldReference fr = (FieldReference) e;
		System.out.println(fr.getName());
		Field mf = si.getField(fr);

		System.out.println("type of reference: "
				+ mf.getContainingClassType().getName());
		// System.out.println("found fieldRef: " + e.toSource());

	}

}
