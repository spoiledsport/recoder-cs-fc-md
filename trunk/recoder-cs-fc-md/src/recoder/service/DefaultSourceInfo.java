// This file is part of the RECODER library and protected by the LGPL.

package recoder.service;

import recoder.*;
import recoder.abstraction.*;
import recoder.abstraction.Enum;
import recoder.convenience.*;
import recoder.convenience.csharp.MultidimArrayUtils;
import recoder.io.*;
import recoder.csharp.*;
import recoder.csharp.statement.*;
import recoder.csharp.declaration.*;
import recoder.csharp.expression.*;
import recoder.csharp.expression.literal.*;
import recoder.csharp.expression.operator.*;
import recoder.csharp.reference.*;
import recoder.kit.*;
import recoder.list.*;
import recoder.util.*;

import java.io.*;
import java.util.Enumeration;

/** 
    Implements queries for program model elements with concrete syntactical
    representations.
    @author RN, AL
 */
public class DefaultSourceInfo
	extends DefaultProgramModelInfo
	implements SourceInfo, ChangeHistoryListener, Formats {

	private final static boolean DEBUG = true;

	/** 
	Cache mapping (package|type|variable|method|constructor)references to 
	program model elements: <Reference, ProgramModelElement> 
	*/
	MutableMap reference2element = new IdentityHashTable(1024);

	ProgressListenerManager listeners = new ProgressListenerManager(this);

	/**
	   Creates a new service.
	   @param config the configuration this services becomes part of.
	*/
	public DefaultSourceInfo(ServiceConfiguration config) {
		super(config);
	}

	/**
	   Performance cache for predefined primitive types and array types.
	 */
	private final MutableMap name2primitiveType = new NaturalHashTable(64);

	/**
	   Initializes the new service; called by the configuration.
	   @param config the configuration this services becomes part of.
	*/
	public void initialize(ServiceConfiguration cfg) {
		super.initialize(cfg);
		cfg.getChangeHistory().addChangeHistoryListener(this);
		NameInfo ni = getNameInfo();
		name2primitiveType.put("bool", ni.getBooleanType());
		name2primitiveType.put("char", ni.getCharType());
		name2primitiveType.put("int", ni.getIntType());
		name2primitiveType.put("uint", ni.getUintType());
		name2primitiveType.put("float", ni.getFloatType());
		name2primitiveType.put("double", ni.getDoubleType());
		name2primitiveType.put("decimal", ni.getDecimalType());
		name2primitiveType.put("byte", ni.getByteType());
		name2primitiveType.put("sbyte", ni.getSbyteType());
		name2primitiveType.put("short", ni.getShortType());
		name2primitiveType.put("ushort", ni.getUshortType());
		name2primitiveType.put("long", ni.getLongType());
		name2primitiveType.put("ulong", ni.getUlongType());

		name2primitiveType.put("bool[]", ni.createArrayType(ni.getBooleanType(), 1));
		name2primitiveType.put("char[]", ni.createArrayType(ni.getCharType(), 1));
		name2primitiveType.put("int[]", ni.createArrayType(ni.getIntType(), 1));
		name2primitiveType.put("uint[]", ni.createArrayType(ni.getUintType(), 1));
		name2primitiveType.put("float[]", ni.createArrayType(ni.getFloatType(), 1));
		name2primitiveType.put("double[]", ni.createArrayType(ni.getDoubleType(), 1));
		name2primitiveType.put("decimal[]", ni.createArrayType(ni.getDecimalType(), 1));
		name2primitiveType.put("byte[]", ni.createArrayType(ni.getByteType(), 1));
		name2primitiveType.put("sbyte[]", ni.createArrayType(ni.getSbyteType(), 1));
		name2primitiveType.put("short[]", ni.createArrayType(ni.getShortType(), 1));
		name2primitiveType.put("ushort[]", ni.createArrayType(ni.getUshortType(), 1));
		name2primitiveType.put("long[]", ni.createArrayType(ni.getLongType(), 1));
		name2primitiveType.put("ulong[]", ni.createArrayType(ni.getUlongType(), 1));

		name2primitiveType.put("object", ni.getSystemObject());
		name2primitiveType.put("string", ni.getSystemString());

	}

	public void addProgressListener(ProgressListener l) {
		listeners.addProgressListener(l);
	}

	public void removeProgressListener(ProgressListener l) {
		listeners.removeProgressListener(l);
	}

	/**
	   Change notification callback method.
	   @param config the configuration this services becomes part of.
	*/
	public void modelChanged(ChangeHistoryEvent changes) {
		TreeChangeList changed = changes.getChanges();
		int s = changed.size();

		listeners.fireProgressEvent(0, s);
		int c = 0;

		// detached first
		for (int i = 0; i < s; i += 1) {
			TreeChange tc = changed.getTreeChange(i);
			if (!tc.isMinor() && (tc instanceof DetachChange)) {
				processChange(tc);
				listeners.fireProgressEvent(++c);
			}
		}

		for (int i = 0; i < s; i += 1) {
			TreeChange tc = changed.getTreeChange(i);
			if (!tc.isMinor() && (tc instanceof AttachChange)) {
				processChange(tc);
				listeners.fireProgressEvent(++c);
			}
		}
	}

	/** handles the given change by trying not to invalidate too much
	pre computed information.
	@param  attached true if the program elements was attached, false otherwise
	@param  changed  the program element that was changed
	*/
	void processChange(TreeChange change) {
		// the following code implements a very restrictive way to invalidate
		// previously computed information.
		ProgramElement changed = change.getChangeRoot();
		if (isPartOf(changed, NamespaceSpecification.class)
			|| isPartOf(changed, Using.class)
			|| determinesGlobalEntityName(changed)
			|| determinesGlobalEntityType(changed)) {
			// pessimistically clear the caches
			super.reset();
			reference2element.clear();
		}
		if (changed instanceof Identifier) {
			NonTerminalProgramElement par = changed.getASTParent();
			if (change instanceof AttachChange) {
				register(par);
			} else {
				String oldname = ((Identifier) changed).getText();
				if (par instanceof VariableSpecification) {
					unregister((VariableSpecification) par, oldname);
				} else if (par instanceof TypeDeclaration) {
					unregister((TypeDeclaration) par, oldname);
				}
			}
		} else {
			if (change instanceof AttachChange) {
				register(changed);
			} else {
				unregister(changed);
			}
		}
	}

	/** determines whether or not the given element is part of a tree node
	of the given type. Especially, this is true if the program element
	is itself an object of the given class.
	@param pe the program element to be checked
	@param c  the class type of the expected parent
	@return true iff any tree parent (including pe itself) is an
	            instance of c
	*/
	private static boolean isPartOf(ProgramElement pe, Class c) {
		while (pe != null && !c.isInstance(pe)) {
			pe = pe.getASTParent();
		}
		return pe != null;
	}

	/** determines whether or not the given progran element is the name
	of a "globally" visible entity.
	@param pe the program element to be checked
	@return true iff the given element deteremines the name or is part
	            of the name of an entity.
	*/
	private boolean determinesGlobalEntityName(ProgramElement pe) {
		if (pe instanceof Identifier) {
			ProgramElement parent = pe.getASTParent();
			// "global" names belong to members. however, we must assure, that
			// members with a member name rather than just an identifier are
			// also evaluated
			return (parent instanceof MemberDeclaration)
				|| (parent instanceof MemberName
					&& parent.getASTParent() instanceof MemberDeclaration);
		}

		// Names may also change if the type reference of a member name changes.
		if (pe instanceof TypeReference && isPartOf(pe, MemberName.class))
			return true;
		return false;
	}

	/** determines whether or not the given progran element specifies the type
	of a "globally" visible entity.
	@param pe the program element to be checked
	@return true iff the given element deteremines the type or is part
	            of the type specification.
	*/
	private boolean determinesGlobalEntityType(ProgramElement pe) {
		// TODO: rethink of this part once again with the new declarations
		// in mind too.
		if (isPartOf(pe, TypeReference.class)
			&& (isPartOf(pe, FieldDeclaration.class)
				|| isPartOf(pe, InheritanceSpecification.class))) {
			return true;
		}
		return false;
	}

	private ProgramElement getDeclaration(ProgramModelElement pme) {
		return (pme instanceof ProgramElement) ? (ProgramElement) pme : null;
	}

	public final TypeDeclaration getTypeDeclaration(DeclaredType ct) {
		return (ct instanceof TypeDeclaration) ? (TypeDeclaration) ct : null;
	}

	public final MethodDeclaration getMethodDeclaration(Method m) {
		return (m instanceof MethodDeclaration) ? (MethodDeclaration) m : null;
	}

	public final EnumDeclaration getEnumDeclaration(Enum m) {
		return (m instanceof EnumDeclaration) ? (EnumDeclaration) m : null;
	}

	public final DelegateDeclaration getDelegateDeclaration(Delegate d) {
		return (d instanceof DelegateDeclaration) ? (DelegateDeclaration) d : null;
	}

	public final VariableSpecification getVariableSpecification(Variable v) {
		return (v instanceof VariableSpecification) ? (VariableSpecification) v : null;
	}

	public final ConstructorDeclaration getConstructorDeclaration(Constructor c) {
		return (c instanceof ConstructorDeclaration) ? (ConstructorDeclaration) c : null;
	}

	/** Try to load a class with the given name, in the specified namespace. */
	private ClassType getClassTypeFromNamespace(String name, String namespace) {

		// This may not function, since one CU may have multiple namespaces
		String xname = namespace;
		if (xname.length() > 0) {
			xname = Naming.dot(xname, name);
		}
		if (DEBUG)
			Debug.log("Checking unit package type " + xname);

		return getNameInfo().getClassType(xname);
	}

	// DISABLED since C# has no direct imports
	// TODO: Write a similar method for the using alias

	//	// traverse *all* directly imported types
	//	private ClassType getClassTypeFromTypeImports(String name, UsingList il) {
	//		if (DEBUG) Debug.log("Checking " + name + " in type imports");
	//		
	//		ClassType result = null;
	//		NameInfo ni = getNameInfo();
	//		for (int i = il.size() - 1; i >= 0; i -= 1) {
	//			Using imp = il.getUsing(i);
	//			continue;
	//			TypeReference tr = imp.getTypeReference();
	//			ClassType newResult = null;
	//			String trname = tr.getName();
	//			
	//			if (DEBUG) Debug.log(" Checking against " + trname);
	//			
	//			// trname must end with the start of name
	//			if (name.startsWith(trname)) {
	//				int tlen = trname.length();
	//				int nlen = name.length();
	//				// the start of name must be a prefix (ending with '.')
	//				if (tlen == nlen || name.charAt(tlen) == '.') {
	//					ReferencePrefix rp = tr.getReferencePrefix();
	//					if (rp == null) {
	//						// direct import of requested type
	//						trname = name;
	//					} else {
	//						// import of a valid prefix of the requested type
	//						trname = Naming.toPathName(rp, name);
	//					}
	//					newResult = ni.getClassType(trname);
	//					if (DEBUG) {
	//						Debug.log(" Trying " + trname);
	//						Debug.log(Format.toString(" Found %N", newResult));
	//					}
	//				}
	//			} else if (
	//				name.endsWith(trname) && name.equals(trname = Naming.toPathName(tr))) {
	//				newResult = ni.getClassType(trname);
	//			}
	//			if (newResult != null) {
	//				if (result != null && result != newResult) {
	//					getErrorHandler().reportError(
	//						new AmbiguousImportException(
	//							"Ambiguous import "
	//								+ Format.toString(ELEMENT_LONG, imp)
	//								+ " - could be "
	//								+ Format.toString("%N", result)
	//								+ " or "
	//								+ Format.toString("%N", newResult),
	//							imp,
	//							result,
	//							newResult));
	//					// ignore if forced to do so
	//				}
	//				result = newResult;
	//			}
	//		}
	//		return result;
	//	}

	private ClassType getClassTypeFromPackageImports(String name, UsingList il) {

		if (DEBUG)
			Debug.log("Checking " + name + " in package imports");

		ClassType result = null;
		NameInfo ni = getNameInfo();
		for (int i = il.size() - 1; i >= 0; i--) {
			Using imp = il.getUsing(i);
			TypeReferenceInfix ref = imp.getReference();
			String xname = Naming.toPathName(ref, name);

			if (DEBUG)
				Debug.log("Checking wildcard type " + xname);

			ClassType newResult = ni.getClassType(xname);
			// pretend not to have seen package-visible types
			if (newResult != null && !newResult.isPublic()) {
				newResult = null;
			}
			if (newResult != null) {
				if (result != null && result != newResult) {
					getErrorHandler().reportError(
						new AmbiguousImportException(
							"Ambiguous import of type "
								+ name
								+ ": could be "
								+ Format.toString("%N", result)
								+ " or "
								+ Format.toString("%N", newResult),
							imp,
							result,
							newResult));
					// ignore problem to resume
				}
				result = newResult;
			}
		}
		return result;
	}

	/**
	   Searches the given short name as a member type of the given class.
	 */
	private DeclaredType getMemberType(String shortName, DeclaredType dt) {
		if (dt instanceof ClassType) {
			ClassType ct = (ClassType) dt;
			if (DEBUG) {
				Debug.log(
					"Checking for type " + shortName + " within " + ct.getFullName());
			}

			DeclaredTypeList innerTypes = ct.getDeclaredTypes();
			for (int i = innerTypes.size() - 1; i >= 0; i -= 1) {
				DeclaredType candid = innerTypes.getDeclaredType(i);
				if (shortName.equals(candid.getName())) {
					return candid;
				}
			}
		}

		return null;
	}

	/**
	   Searches the given type name in the given scope.
	   The name may be a partial name such as <CODE>A.B</CODE> where
	   <CODE>b</CODE> is a member class of <CODE>A</CODE>.
	 */
	private DeclaredType getLocalType(String name, TypeScope scope) {

		DeclaredType result = null;
		int dotPos = name.indexOf('.');
		String shortName = (dotPos == -1) ? name : name.substring(0, dotPos);

		if (DEBUG) {
			String output =
				"Looking for type "
					+ shortName
					+ " in scope of "
					+ Format.toString("%c[%p]: ", scope);
			DeclaredTypeList ctl = scope.getTypesInScope();
			if (ctl != null && ctl.size() > 0) {
				output += " " + Format.toString("%n", ctl);
			}
			Debug.log(output);
		}

		result = scope.getTypeInScope(shortName);

		while (result != null && dotPos != -1) {
			dotPos += 1;
			int nextDotPos = name.indexOf('.', dotPos);
			shortName =
				(nextDotPos == -1)
					? name.substring(dotPos)
					: name.substring(dotPos, nextDotPos);
			dotPos = nextDotPos;
			result = getMemberType(shortName, result);
		}

		return result;
	}

	/**
	   Searches an inherited member type of the given name.
	   The method does also report locally defined member types of the
	   given type.
	 */
	private DeclaredType getInheritedType(String name, ClassTypeDeclaration ct) {
		int dotPos = name.indexOf('.');
		String shortName = (dotPos == -1) ? name : name.substring(0, dotPos);

		// it does not pay to check if ct has any non-trivial supertypes
		DeclaredTypeList ctl = getAllTypes(ct);

		if (DEBUG)
			Debug.log(
				"Checking type "
					+ shortName
					+ " as inherited member of "
					+ ct.getFullName()
					+ ": "
					+ Format.toString("%N", ctl));

		DeclaredType result = null;
		int nc = ctl.size();

		// starting at i = ct.getTypes().size() would have little to no
		// influence on performance
		for (int i = 0; i < nc; i++) {
			DeclaredType c = ctl.getDeclaredType(i);
			if (shortName.equals(c.getName())) {
				result = c;
				break;
			}
		}
		while (result != null && dotPos != -1) {
			dotPos += 1;
			int nextDotPos = name.indexOf('.', dotPos);
			shortName =
				(nextDotPos == -1)
					? name.substring(dotPos)
					: name.substring(dotPos, nextDotPos);
			dotPos = nextDotPos;
			result = getMemberType(shortName, result);
		}
		return result;
	}

	/** 
	Tries to find a type with the given name using the given
	program element as context. Useful to check for name clashes
	when introducing a new identifier.
	Neither name nor context may be <CODE>null</CODE>.
	@param  name the name for the type to be looked up;
	may or may not be qualified.
	@param  context a program element defining the lookup context (scope).
	@return the corresponding type (may be <CODE>null</CODE>).
	*/
	public Type getType(String name, ProgramElement context) {
		Debug.asserta(name, context);

		// Mutable list for all usings. This list is needed, since in C#
		// we can declare a new using at every namespace declaration, not
		// just in the compilation unit.
		UsingMutableList usings = new UsingArrayList();

		// String, which will hold the namespace of the current context.
		// Since C# has multiple namespaces in a compilation unit, it was needed
		// to assemble the namespace of the given context.
		String namespace = "";

		// TODO: Implement using aliases

		NameInfo ni = getNameInfo();

		// check primitive types, array types of primitive types,
		// and void --- these happen often
		Type t = (Type) name2primitiveType.get(name);
		if (t != null) {
			return t;
		}
		if (name.equals("void")) {
			return null;
		}
		// catch array types
		if (name.endsWith("]")) {

			String baseName = MultidimArrayUtils.getBaseTypeName(name);

			// compute base type
			Type baseType = getType(baseName, context);
			if (baseType == null) {
				return null;
			}

			String indexExprs = MultidimArrayUtils.getIndexExpressions(name);
			// the basetype exists now, so fetch a corresponding array type
			// (if there is none, the name info will create one)
			return ni.getType(baseType.getFullName() + indexExprs);
		}

		if (DEBUG)
			Debug.log(
				"Looking for type " + name + Format.toString(" @%p in %u", context));

		updateModel();

		// in the very special case that we are asking from the point of 
		// view of a supertype reference, we must move to the enclosing unit
		// or parent type
		if (context.getASTParent() instanceof InheritanceSpecification) {
			context = context.getASTParent().getASTParent().getASTParent();
		}

		// Walk up in the tree, until we find a valid type scope
		ProgramElement pe = context;
		while (pe != null && !(pe instanceof TypeScope)) {
			context = pe;
			pe = pe.getASTParent();
		}

		TypeScope scope = (TypeScope) pe;

		if (scope == null) {
			Debug.log(
				"Null scope during type query "
					+ name
					+ " in context "
					+ Format.toString(Formats.ELEMENT_LONG, context));
			Debug.log(Debug.makeStackTrace());
		}

		DeclaredType result = null;

		// do the scope walk
		TypeScope s = scope;
		while (s != null) {

			// This hack was needed because of C#.
			if (s instanceof NamespaceSpecification) {
				// we have found a new namespace

				// first we add the name to the buffer
				NamespaceSpecification ns = (NamespaceSpecification) s;
				if (namespace != null && !namespace.equals(""))
					namespace = Naming.dot(ns.getName(), namespace);
				else
					namespace = ns.getName();

				// we also have to add the usings
				UsingList ul = ns.getUsings();
				if (ul != null)
					usings.add(ul);
			}

			// Try to find the type inside this scope
			result = getLocalType(name, s);

			if (result != null) {
				// must double check this result - rare cases of confusion
				// involving type references before a local class of the
				// corresponding name has been specified

				// TODO: Consider if statement blocks are needed as type scopes...

				if (s instanceof StatementBlock) {
					StatementContainer cont = (StatementBlock) s;
					for (int i = 0; true; i += 1) {
						Statement stmt = cont.getStatementAt(i);
						if (stmt == result) {
							// stop if definition comes first
							break;
						}
						if (stmt == context) {
							// tricky: reference before definition - must
							// ignore the definition :(
							result = null;
							break;
						}
					}
				}
				if (result != null) {
					// leave _now_
					break;
				}
			}

			if (s instanceof ClassTypeDeclaration) {
				ClassTypeDeclaration td = (ClassTypeDeclaration) s;
				DeclaredType newResult = getInheritedType(name, td);

				if (newResult != null) {
					if (result == null) {
						if (DEBUG)
							Debug.log(
								"Found type "
									+ name
									+ " inherited in type scope "
									+ td.getFullName());
						result = newResult;
						break;
					} else if (result != newResult) {
						// !!!!!!! Problematic if this is a speculative 
						// question - do we really want to bail out?
						getErrorHandler().reportError(
							new AmbiguousReferenceException(
								"Type "
									+ Format.toString("%N", newResult)
									+ " is an inherited member type that is also defined as outer member type "
									+ Format.toString("%N", result),
								null,
								result,
								newResult));
						break;
					}
				}
			}

			// Go one step up in the scope	
			scope = s;
			pe = s.getASTParent();
			while (pe != null && !(pe instanceof TypeScope)) {
				context = pe;
				pe = pe.getASTParent();
			}
			s = (TypeScope) pe;
		} // End of iteration through the type scopes

		if (result != null) {
			if (DEBUG)
				Debug.log(Format.toString("Found %N", result));

			return result;
		}

		// now the outer scope is null, so we have arrived at the top
		CompilationUnit cu = (CompilationUnit) scope;

		UsingList ul = cu.getUsings(); // Add the usings from here too
		if (ul != null)
			usings.add(ul);

		// DISABLED, since there are no type imports in C#
		// 
		//		if (il != null) {
		//			// first check type usings
		//			result = getClassTypeFromTypeImports(name, il);
		//		}

		result = getClassTypeFromNamespace(name, namespace);

		if (result == null) {
			// then check for each and every namespace import
			result = getClassTypeFromPackageImports(name, usings);
		}

		if (result == null) {
			// check global types: if unqualified, attempt "System.<name>":
			// any unqualified local type would have been imported already!
			String defaultName = Naming.dot("System", name);

			if (DEBUG)
				Debug.log("Checking type in System namespace" + defaultName);

			result = ni.getClassType(defaultName);

			if (result == null) {
				// If all fails: try to load the class from somewhere else
				if (DEBUG)
					Debug.log("Checking type " + name);

				result = ni.getClassType(name);
			}

		}

		if (result != null) {
			scope.addTypeToScope(result, name); // add it to the CU scope
		}

		if (DEBUG)
			Debug.log(Format.toString("Found %N", result));

		return result;
	}

	/** Returns the type of the type reference. */
	public Type getType(TypeReference tr) {

		// First try to find this reference in the cache
		Type res = (Type) reference2element.get(tr);
		if (res != null) {
			return res;
		}

		ReferencePrefix rp = tr.getReferencePrefix();
		if (rp instanceof NamespaceReference) {
			String name = Naming.toPathName((NamespaceReference) rp, tr.getName());
			res = getNameInfo().getClassType(name);

			int[] d = tr.getDimensions();
			if (d == null) {
				d = new int[0];
			}
			if (d.length > 0 && res != null) {
				// If this was an array type, we can create it...
				res = getNameInfo().createArrayType(res, d);

			}

		} else {
			// Try to find the referenced type inside that scope
			res = getType(Naming.toPathName(tr), tr);
		}

		if (res != null) {
			// If the result has been found, we cache it
			reference2element.put(tr, res);
		}

		return res;
	}

	////////////////////////////////////////////////////////////////////////////////////
	////////////////////// SOURCE INFO IMPLEMENTATION //////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////

	public final DeclaredType getType(TypeDeclaration td) {
		return (DeclaredType) td;
	}

	public Type getType(VariableSpecification vs) {
		updateModel(); // probably not necessary
		if (vs.getParent() == null) {
			System.out.print("");
		}
		TypeReference tr = vs.getParent().getTypeReference();
		Type result = getType(tr);

		// DISABLED: In C# a variable specification may not be an 
		//		if (result != null) {
		//			int [] d = vs.getDimensions();
		//			if (d.length > 0) {
		//				result = getNameInfo().createArrayType(result,d);
		//			}
		//		}
		return result;
	}

	/** Returns the type of the reference prefix.
	 *  This method resolves primitive types to their boxing alternative, so
	 *  it is to be used before resolving field references and method references.
	 * 
	 */
	private Type getTypeOfPrefix(ReferencePrefix rp) {

		if (rp instanceof Literal) {
			// Literals as prefixes can also be primitive types, but in this case
			// we must return the matching boxed type.
			Type t =
				serviceConfiguration.getConstantEvaluator().getCompileTimeConstantType(
					(Literal) rp);
			if (t instanceof PrimitiveType)
				return ((PrimitiveType) t).getBoxingClass();
			return t;
		}

		if (rp instanceof PrimitiveType) {
			// We need to determine the type of a reference prefix 
			// As a reference prefix primitive types behave as if
			// they were a type of their boxing class.
			return ((PrimitiveType) rp).getBoxingClass();
		}

		if (rp instanceof ArrayType) {
			// Array types are boxed to System.Array, if they are prefixes
			return getNameInfo().getSystemArray();
		}

		return getType(rp);
	}

	/**
	   Returns the type of the given program element. For
	   declarations, this is the type declared by the given
	   declaration, for references, this means the referenced type,
	   and for expressions this is the result type.
	   @param pe the program element to analyze.
	   @return the type of the program element or <tt>null</tt> if the
	   type is unknown or unavailable.
	*/
	public Type getType(ProgramElement pe) {
		updateModel();
		Type result = null;

		if (pe instanceof Expression) {
			result = getType((Expression) pe);
		} else if (pe instanceof UncollatedReferenceQualifier) {
			result = getType((UncollatedReferenceQualifier) pe);
		} else if (pe instanceof TypeReference) {
			result = getType((TypeReference) pe);
		} else if (pe instanceof VariableSpecification) {
			result = getType((VariableSpecification) pe);
		} else if (pe instanceof MethodDeclaration) {
			if (!(pe instanceof ConstructorDeclaration)) {
				result = getReturnType((MethodDeclaration) pe);
			}
		} else if (pe instanceof TypeDeclaration) {
			result = getType((TypeDeclaration) pe);
		}
		return result;
	}

	private Type getType(UncollatedReferenceQualifier urq) {
		Reference r = resolveURQ(urq);
		if (r instanceof UncollatedReferenceQualifier) {
			// resolution failed, continue anyway
			return getNameInfo().getUnknownClassType();

			// TODO: Add unknown declared type instead (?)

		}
		return getType(r);
	}

	// TODO: Implement operator overload checking
	public Type getType(Expression expr) {
		Type result = null;
		if (expr instanceof Operator) { ///////////////// Operators
			Operator op = (Operator) expr;
			ExpressionList args = op.getArguments();
			if (op instanceof Assignment) {
				result = getType(args.getExpression(0));
			} else if (
				(op instanceof TypeCast)
					|| (op instanceof New)
					|| (op instanceof Typeof)) {
				result = getType(((TypeOperator) op).getTypeReference());
			} else if (op instanceof NewArray) {
				NewArray n = (NewArray) op;
				TypeReference tr = n.getTypeReference();
				result = getType(tr);
				if (result != null) {
					int[] d = tr.getDimensions();
					for (int i = 0; i < d.length; i++) {
						result = getNameInfo().createArrayType(result, d[i]);
					}
				}
			} else if (
				(op instanceof Positive)
					|| (op instanceof Negative)
					|| (op instanceof PreIncrement)
					|| (op instanceof PostIncrement)
					|| (op instanceof PreDecrement)
					|| (op instanceof PostDecrement)
					|| (op instanceof ShiftLeft)
					|| (op instanceof ShiftRight)
					|| (op instanceof ParenthesizedExpression)
					|| (op instanceof OutOperator)
					|| (op instanceof RefOperator)
					|| (op instanceof Checked)
					|| (op instanceof Unchecked)) {
				result = getType(args.getExpression(0));
			} else if (
				(op instanceof Plus)
					|| (op instanceof Minus)
					|| (op instanceof Times)
					|| (op instanceof Divide)
					|| (op instanceof Modulo)) {
				Type t1 = getType(args.getExpression(0));
				Type t2 = getType(args.getExpression(1));
				if ((op instanceof Plus)
					&& ((t1 == getNameInfo().getSystemString())
						|| (t2 == getNameInfo().getSystemString())
						|| (t1 == null)
						|| (t2 == null))) {
					// all primitive types are known - 
					// one of these must be a class type
					result = getNameInfo().getSystemString();
					// any object-plus-operation must result in a string type
				} else if (
					(t1 instanceof PrimitiveType) && (t2 instanceof PrimitiveType)) {
					result = getPromotedType((PrimitiveType) t1, (PrimitiveType) t2);
					if (result == null) {
						getErrorHandler().reportError(
							new TypingException(
								"Types could not be promoted in " + op,
								op));
						result = getNameInfo().getUnknownType();
					}
				} else {
					if ((t1 != null) && (t2 != null)) {
						getErrorHandler().reportError(
							new TypingException(
								"Illegal operand types for plus "
									+ t1
									+ " + "
									+ t2
									+ " in expression "
									+ op,
								op));
						result = getNameInfo().getUnknownType();
					}
				}
			} else if (
				(op instanceof ShiftRight)
					|| (op instanceof ShiftLeft)
					|| (op instanceof BinaryAnd)
					|| (op instanceof BinaryOr)
					|| (op instanceof BinaryNot)
					|| (op instanceof BinaryXOr)) {
				result = getType(args.getExpression(0));
			} else if (
				(op instanceof ComparativeOperator)
					|| (op instanceof LogicalAnd)
					|| (op instanceof LogicalOr)
					|| (op instanceof LogicalNot)
					|| (op instanceof Instanceof)) {
				result = getNameInfo().getBooleanType();
			} else if (op instanceof Conditional) {
				Expression e1 = args.getExpression(1);
				Expression e2 = args.getExpression(2);
				Type t1 = getType(e1);
				Type t2 = getType(e2);
				if (t1 == t2) {
					result = t1;
				} else if (t1 instanceof PrimitiveType && t2 instanceof PrimitiveType) {
					NameInfo ni = getNameInfo();
					if ((t1 == ni.getShortType() && t2 == ni.getByteType())
						|| (t2 == ni.getShortType() && t1 == ni.getByteType())) {
						result = ni.getShortType();
					} else {
						result =
							serviceConfiguration
								.getConstantEvaluator()
								.getCompileTimeConstantType(
								op);
						if (result == null) {
							if (isNarrowingTo(e1, (PrimitiveType) t2)) {
								return t2;
							}
							if (isNarrowingTo(e2, (PrimitiveType) t1)) {
								return t1;
							}
							result =
								getPromotedType((PrimitiveType) t1, (PrimitiveType) t2);
						}
					}
				} else if (t1 instanceof PrimitiveType || t2 instanceof PrimitiveType) {
					getErrorHandler().reportError(
						new TypingException("Incompatible types in conditional", op));
					result = getNameInfo().getUnknownType();
				} else { // two reference types
					if (t1 == getNameInfo().getNullType()) {
						result = t2; // null x T --> T
					} else if (t2 == getNameInfo().getNullType()) {
						result = t1; // T x null --> T
					} else {
						if (isWidening(t1, t2)) {
							result = t2;
						} else if (isWidening(t2, t1)) {
							result = t1;
						} else {
							getErrorHandler().reportError(
								new TypingException(
									"Incompatible types in conditional",
									op));
							result = getNameInfo().getUnknownType();
						}
					}
				}
			} else {
				Debug.error(
					"Type resolution not implemented for operation "
						+ op.getClass().getName());
			}
		} else if (expr instanceof Literal) { ///////////////// Literals
			if (expr instanceof NullLiteral) {
				result = getNameInfo().getNullType();
			} else if (expr instanceof BooleanLiteral) {
				result = getNameInfo().getBooleanType();
			} else if (expr instanceof LongLiteral) {
				result = getNameInfo().getLongType();
			} else if (expr instanceof IntLiteral) {
				result = getNameInfo().getIntType();
			} else if (expr instanceof FloatLiteral) {
				result = getNameInfo().getFloatType();
			} else if (expr instanceof DoubleLiteral) {
				result = getNameInfo().getDoubleType();
			} else if (expr instanceof CharLiteral) {
				result = getNameInfo().getCharType();
			} else if (expr instanceof StringLiteral) {
				result = getNameInfo().getSystemString();
			}
		} else if (expr instanceof Reference) { ///////////////// References
			if (expr instanceof UncollatedReferenceQualifier) {
				result = getType((UncollatedReferenceQualifier) expr);
			} else if (expr instanceof MetaClassReference) {
				// TODO: We don't have the same metaclass reference as in Java. What should we do?			
				result = getNameInfo().getSystemType();
			} else if (expr instanceof VariableReference) {
				// look for the variable declaration
				Variable v = getVariable((VariableReference) expr);
				if (v != null) {
					result = v.getType();
				} else {
					getErrorHandler().reportError(
						new UnresolvedReferenceException(
							Format.toString("Could not resolve " + ELEMENT_LONG, expr)
								+ " (01)",
							(VariableReference) expr));
					v = getNameInfo().getUnknownField();
				}
			} else if (expr instanceof MethodReference) {
				// look for the return type of the method
				Method m = getMethod((MethodReference) expr);
				if (m != null) {
					result = m.getReturnType();
				}
			} else if (expr instanceof ArrayLengthReference) {
				result = getNameInfo().getIntType();
			} else if (expr instanceof ArrayReference) {
				ArrayReference aref = (ArrayReference) expr;
				// Get the base type of the arrayreference.
				ReferencePrefix rp = aref.getReferencePrefix();
				Type ht = getType(rp);
				if (ht != null) {
					// DISABLED: Written in an other way
					//					ExpressionList dimExprs = aref.getDimensionExpressions();
					//					int dims = dimExprs.size();
					//					for (int i = 0; i < dims; i++) {
					//						ht = ((ArrayType) ht).getBaseType();
					//					}
					// TODO: Testing
					while (rp instanceof ArrayReference) {
						rp = ((ArrayReference) rp).getReferencePrefix();
						ht = ((ArrayType) ht).getBaseType();
					}

					if (ht != null) {
						result = ht;
					} else {
						getErrorHandler().reportError(
							new TypingException(
								"Not an array type: " + ht + " in expression " + expr,
								expr));
						result = getNameInfo().getUnknownType();
					}
				}
			} else if (expr instanceof ThisReference) {
				ReferencePrefix rp = ((ThisReference) expr).getReferencePrefix();
				if (rp == null) {
					result = getContainingClassType(expr);
				} else {
					// the prefix "points" to the required type...
					result = getType(rp);
				}
			} else if (expr instanceof SuperReference) {
				ReferencePrefix rp = ((SuperReference) expr).getReferencePrefix();
				ClassType thisType;
				if (rp == null) {
					thisType = getContainingClassType(expr);
				} else {
					thisType = (ClassType) getType(rp);
				}
				ClassTypeList supers = thisType.getSupertypes();
				if ((supers != null) && (!supers.isEmpty())) {
					result = supers.getClassType(0);
				}
			}
		} else if (expr instanceof ArrayInitializer) { //// ArrayInitializer
			ProgramElement pe = expr;
			while ((pe != null) && !(pe instanceof VariableSpecification)) {
				pe = pe.getASTParent();
			}
			result = getType(pe);
		} else {
			Debug.error(
				"Type analysis for general expressions is currently not implemented: "
					+ expr
					+ " <"
					+ expr.getClass().getName()
					+ ">");
		}
		return result;
	}

	// TODO: Check the C# specification
	public boolean isNarrowingTo(Expression expr, PrimitiveType to) {
		NameInfo ni = getNameInfo();
		int minValue, maxValue;
		if (to == ni.getByteType()) {
			minValue = Byte.MIN_VALUE;
			maxValue = Byte.MAX_VALUE;
		} else if (to == ni.getCharType()) {
			minValue = Character.MIN_VALUE;
			maxValue = Character.MAX_VALUE;
		} else if (to == ni.getShortType()) {
			minValue = Short.MIN_VALUE;
			maxValue = Short.MAX_VALUE;
		} else {
			return false;
		}
		ConstantEvaluator ce = serviceConfiguration.getConstantEvaluator();
		ConstantEvaluator.EvaluationResult res = new ConstantEvaluator.EvaluationResult();
		if (!ce.isCompileTimeConstant(expr, res)
			|| res.getTypeCode() != ConstantEvaluator.INT_TYPE) {
			return false;
		}
		int value = res.getInt();
		return (minValue <= value) && (value <= maxValue);
	}

	public Type getType(ProgramModelElement pme) {
		Debug.asserta(pme);
		// updateModel(); not necessary
		Type result = null;
		if (pme instanceof Type) {
			result = (Type) pme;
		} else {
			if (pme instanceof ProgramElement) {
				result = getType((ProgramElement) pme);
				if ((result == null) && (pme instanceof VariableSpecification)) {
					// void is acceptable for method decls
					getErrorHandler().reportError(
						new UnresolvedReferenceException(
							Format.toString("Unknown type of " + ELEMENT_LONG, pme),
							((VariableSpecification) pme)
								.getParent()
								.getTypeReference()));
					result = getNameInfo().getUnknownType();
				}
			} else {
				result = pme.getProgramModelInfo().getType(pme);
			}
		}
		return result;
	}

	public ClassType getContainingClassType(ProgramElement context) {
		Debug.asserta(context);
		//updateModel(); not necessary
		if (context instanceof TypeDeclaration) {
			context = context.getASTParent();
		}
		do {
			if (context instanceof ClassType) {
				return (ClassType) context;
			}
			context = context.getASTParent();
		} while (context != null);
		return null;
	}

	public ClassType getContainingClassType(Member m) {
		Debug.asserta(m);
		// updateModel(); not necessary
		ClassType result = null;
		ProgramElement pe = getDeclaration(m);
		if (pe == null) {
			result = m.getProgramModelInfo().getContainingClassType(m);
		} else {
			result = getContainingClassType(pe);
		}
		return result;
	}

	/*
	  Returns a field with the given name from the given class type or
	  from the bottommost supertype that defines it.
	*/
	private Field getInheritedField(String name, ClassType ct) {
		// for private use only - no model update required
		FieldList fl = ct.getAllFields();
		int nf = fl.size();
		for (int i = 0; i < nf; i++) {
			Field f = fl.getField(i);
			if (name.equals(f.getName())) {
				return f;
			}
		}
		return null;
	}

	// context can make a difference under rare circumstances
	// a context before a local declaration will not locate the declaration
	// and will look for a variable in an outer scope
	public Variable getVariable(String name, ProgramElement context) {
		Debug.asserta(name, context);
		updateModel();
		if (DEBUG)
			Debug.log("Looking for variable " + name);
		// look for the next variable scope equals to or parent of context
		ProgramElement pe = context;
		while (pe != null && !(pe instanceof VariableScope)) {
			context = pe;
			pe = pe.getASTParent();
		}
		if (DEBUG)
			Debug.log("Found scope " + Format.toString("%c @%p", pe));
		if (pe == null) {
			// a null scope can happen if we try to find a variable
			// speculatively (for URQ resolution)
			return null;
		}
		VariableScope scope = (VariableScope) pe;
		Variable result;
		do {
			result = scope.getVariableInScope(name);
			if (result != null) {
				if (DEBUG)
					Debug.log(
						"Found variable in scope " + Format.toString("%c @%p", scope));

				// must double check this result - rare cases of confusion
				// involving field references before a local variable of the
				// same name has been specified
				if (scope instanceof StatementBlock) {
					StatementContainer cont = (StatementBlock) scope;
					// we need the topmost var-scope including context,
					// or context itself if the found scope is the topmost one
					VariableDeclaration def =
						((VariableSpecification) result).getParent();
					for (int i = 0; true; i += 1) {
						Statement s = cont.getStatementAt(i);
						if (s == def) {
							// Debug.log(">>> Not ignored: " + Format.toString("%c \"%s\" @%p", result) + " for context " + Format.toString("@%p", context));

							// stop if definition comes first
							break;
						}
						if (s == context) {
							// tricky: reference before definition - must
							// ignore the definition :(

							// Debug.log(">>> Ignored: " + Format.toString("%c \"%s\" @%p", result) + " for context " + Format.toString("@%p", context));
							result = null;
							break;
						}
					}
				}
				if (result != null) {
					// leave _now_
					break;
				}
			}
			if (scope instanceof ClassType) {
				result = getInheritedField(name, (ClassType) scope);
				if (result != null) {
					break;
				}
				// might want to check for ambiguity of outer class fields!!!
			}
			pe = scope.getASTParent();
			while (pe != null && !(pe instanceof VariableScope)) {
				context = pe; // proceed the context
				pe = pe.getASTParent();
			}
			scope = (VariableScope) pe;
		}
		while (scope != null);
		// we were at the compilation unit scope, leave for good now

		return result;
	}

	public final Variable getVariable(VariableSpecification vs) {
		return (Variable) vs;
	}

	public Field getField(FieldReference fr) {
		Field res = (Field) reference2element.get(fr);
		if (res != null) {
			return res;
		}
		updateModel();
		String name = fr.getName();
		ReferencePrefix rp = fr.getReferencePrefix();
		if (rp == null) {
			res = (Field) getVariable(name, fr);
			if (res != null) {
				reference2element.put(fr, res);
			}
			return res;
		} else {
			ClassType thisType = getContainingClassType(fr);
			if (thisType == null) {
				return null;
			}
			ClassType ct = (ClassType) getTypeOfPrefix(rp);
			if (ct == null) {
				return null;
			}
			FieldList fl = ct.getAllFields();
			if (fl == null) {
				return null;
			}
			for (int i = fl.size() - 1; i >= 0; i--) {
				res = fl.getField(i);
				if (res.getName() == name) {
					reference2element.put(fr, res);
					return res;
				}
			}
			return null;
		}
	}
    
    /**
     * Method getDelegate.
     * @param dr
     * @return Variable
     */
        public Variable getVariable(DelegateCallReference dr) {
            Variable v = (Variable) reference2element.get(dr);
            if (v == null) {
                v = getVariable(dr.getName(),dr);
                if (v != null) {
                    reference2element.put(dr,v);
                }
            }
            return v;
        }
    

	public Variable getVariable(VariableReference vr) {
		if (vr instanceof FieldReference) {
			return getField((FieldReference) vr);
		} else {
			Variable res = (Variable) reference2element.get(vr);
			if (res != null) {
				return res;
			}
			res = getVariable(vr.getName(), vr);
			if (res != null) {
				reference2element.put(vr, res);
			}
			return res;
		}
	}

	// args == null is admissible
	public TypeList makeSignature(ExpressionList args) {
		if (args == null || args.isEmpty()) {
			return TypeList.EMPTY_LIST;
		}
		// updateModel(); not necessary
		int arity = args.size();
		TypeMutableList result = new TypeArrayList(arity);
		for (int i = 0; i < arity; i++) {
			Expression e = args.getExpression(i);
			Type et = getType(e);
			if (et == null) {
				getErrorHandler().reportError(
					new TypingException(
						"Unknown type for argument #"
							+ i
							+ " in call "
							+ Format.toString(ELEMENT_LONG, e.getExpressionContainer()),
						e));
				et = getNameInfo().getUnknownType();
			}
			result.add(et);
		}
		return result;
	}

	public final Method getMethod(MethodDeclaration md) {
		return (Method) md;
	}

	public final Constructor getConstructor(ConstructorDeclaration cd) {
		return (Constructor) cd;
	}

	public Method getMethod(MethodReference mr) {
		Method res = (Method) reference2element.get(mr);
		if (res != null) {
			return res;
		}
		MethodList mlist = getMethods(mr);
		if (mlist == null || mlist.isEmpty()) {
			getErrorHandler().reportError(
				new UnresolvedReferenceException(
					Format.toString("Could not resolve " + ELEMENT_LONG + " (02)", mr),
					mr));
			return getNameInfo().getUnknownMethod();
		} else if (mlist.size() > 1) {
			getErrorHandler().reportError(
				new AmbiguousReferenceException(
					Format.toString(
						ELEMENT_LONG + " is ambiguous - it could be one of ",
						mr)
						+ Format.toString("%N", mlist),
					mr,
					mlist));
			// if we have to resume, use the first for the time being
		}
		res = mlist.getMethod(0);
		reference2element.put(mr, res);
		return res;
	}

	public MethodList getMethods(MethodReference mr) {
		Debug.asserta(mr);
		updateModel();
		MethodList result = null;
		TypeList signature = makeSignature(mr.getArguments());
		ReferencePrefix rp = mr.getReferencePrefix();
		if (rp == null) {
			ClassType targetClass = getContainingClassType(mr);
			result = getMethods(targetClass, mr.getName(), signature);
			// if we didn't find an adequate method - the target class may be
			// an inner or anonymous class. So we have to look "outside"
			if (result != null && result.size() > 0) {
				return result;
			}
			for (DeclaredTypeContainer ctc = targetClass.getContainer();
				ctc != null;
				ctc = ctc.getContainer()) {
				if (ctc instanceof ClassType) {
					result = getMethods((ClassType) ctc, mr.getName(), signature);
					if ((result != null) && (result.size() > 0)) {
						return result;
					}
				}
			}
			getErrorHandler().reportError(
				new UnresolvedReferenceException(
					Format.toString("Could not resolve " + ELEMENT_LONG + " (03)", mr),
					mr));
			MethodMutableList list = new MethodArrayList(1);
			list.add(getNameInfo().getUnknownMethod());
			result = list;
		} else {
			Type rpt = getTypeOfPrefix(rp);
			if (rpt == null) {
				getErrorHandler().reportError(
					new UnresolvedReferenceException(
						Format.toString(
							"Could not resolve " + ELEMENT_LONG + " (04)",
							rp),
						rp));
				MethodMutableList list = new MethodArrayList(1);
				list.add(getNameInfo().getUnknownMethod());
				return list;
			}

			result = getMethods((ClassType) rpt, mr.getName(), signature);

		}
		return result;
	}

	public Constructor getConstructor(ConstructorReference cr) {
		Constructor res = (Constructor) reference2element.get(cr);
		if (res != null) {
			return res;
		}
		ConstructorList clist = getConstructors(cr);
		if (clist == null || clist.isEmpty()) {
			getErrorHandler().reportError(
				new UnresolvedReferenceException(
					Format.toString("Could not resolve " + ELEMENT_LONG + " (05)", cr),
					cr));
			return getNameInfo().getUnknownConstructor();
		} else if (clist.size() > 1) {
			getErrorHandler().reportError(
				new AmbiguousReferenceException(
					Format.toString(
						ELEMENT_LONG + " is ambiguous - it could be one of ",
						cr)
						+ Format.toString("%N", clist),
					cr,
					clist));
			// use the first, if we do have to continue
		}
		res = clist.getConstructor(0);
		reference2element.put(cr, res);
		return res;
	}

	public ConstructorList getConstructors(ConstructorReference cr) {
		updateModel();
		DeclaredType type = null;
		if (cr instanceof New) {
			New n = (New) cr;
			ReferencePrefix rp = n.getReferencePrefix();
			if (rp != null) {
				// In this case we need not do anything
			}
			type = (DeclaredType) getType(n.getTypeReference());
		} else if (cr instanceof ThisConstructorReference) {
			type = getContainingClassType(cr);
		} else if (cr instanceof SuperConstructorReference) {
			type = getContainingClassType(cr);
			ClassTypeList superTypes = getSupertypes((ClassType) type);
			for (int i = 0; i < superTypes.size(); i += 1) {
				type = superTypes.getClassType(i);
				if (!((ClassType) type).isInterface()) {
					break; // there must be one concrete class
					// the exception would be parsing a super() call inside
					// java.lang.Object ;)
				}
			}
		} else {
			Debug.error("Unknown Constructor Reference " + cr);
		}
		if (type == null) {
			getErrorHandler().reportError(
				new UnresolvedReferenceException(
					Format.toString("Could not resolve " + ELEMENT_LONG + " (06)", cr),
					cr));
			ConstructorMutableList list = new ConstructorArrayList(1);
			list.add(getNameInfo().getUnknownConstructor());
			return list;
		} else if (type instanceof Delegate) {
			ConstructorMutableList list = new ConstructorArrayList(1);
			list.add(new DelegateConstructor((Delegate) type));
			return list;
		} else
			return getConstructors((ClassType) type, makeSignature(cr.getArguments()));
	}

	public DeclaredTypeList getTypes(TypeDeclaration td) {
		Debug.asserta(td);
		updateModel();
		MemberDeclarationList members = td.getMembers();
		if (members == null) {
			return DeclaredTypeList.EMPTY_LIST;
		}
		int s = members.size();
		DeclaredTypeMutableList result = new DeclaredTypeArrayList();
		for (int i = 0; i < s; i += 1) {
			MemberDeclaration m = members.getMemberDeclaration(i);
			if (m instanceof TypeDeclaration) {
				result.add((DeclaredType) m);
			}
		}
		return result;
	}

	public FieldList getFields(ClassTypeDeclaration td) {
		Debug.asserta(td);
		updateModel();
		MemberDeclarationList members = td.getMembers();
		if (members == null) {
			return FieldList.EMPTY_LIST;
		}
		int s = members.size();
		FieldMutableList result = new FieldArrayList();
		for (int i = 0; i < s; i += 1) {
			MemberDeclaration m = members.getMemberDeclaration(i);
			if (m instanceof FieldDeclaration) {
				result.add(((FieldDeclaration) m).getFieldSpecifications());
			}
		}
		return result;
		// was: td.getFieldsInScope(); -- faster, but not order preserving
	}

	public MethodList getMethods(ClassTypeDeclaration td) {
		Debug.asserta(td);
		updateModel();
		MemberDeclarationList members = td.getMembers();
		if (members == null) {
			return MethodList.EMPTY_LIST;
		}
		int s = members.size();
		MethodMutableList result = new MethodArrayList();
		for (int i = 0; i < s; i += 1) {
			MemberDeclaration m = members.getMemberDeclaration(i);
			if (m instanceof MethodDeclaration) {
				if (!(m instanceof ConstructorDeclaration)) {
					result.add((MethodDeclaration) m);
				}
			}
		}
		return result;
	}

	public ConstructorList getConstructors(ClassTypeDeclaration td) {
		Debug.asserta(td);
		updateModel();
		ConstructorMutableList result = new ConstructorArrayList(2);
		MemberDeclarationList members = td.getMembers();
		int s = (members == null) ? 0 : members.size();
		for (int i = 0; i < s; i += 1) {
			MemberDeclaration m = members.getMemberDeclaration(i);
			if (m instanceof ConstructorDeclaration) {
				result.add((ConstructorDeclaration) m);
			}
		}
		if (result.isEmpty() && !td.isInterface() && td.getName() != null) {
			result.add(
				serviceConfiguration.getImplicitElementInfo().getDefaultConstructor(td));
		}
		return result;
	}

	public Namespace getNamespace(NamespaceReference pr) {
		Namespace res = (Namespace) reference2element.get(pr);
		if (res != null) {
			return res;
		}
		res = getNameInfo().createNamespace(Naming.toPathName(pr));
		if (res != null) {
			reference2element.put(pr, res);
		}
		return res;
	}

	public Namespace getNamespace(ProgramModelElement pme) {
		Debug.asserta(pme);
		updateModel();
		Namespace result = null;
		ProgramElement pe = getDeclaration(pme);
		if (pe == null) {
			result = pme.getProgramModelInfo().getNamespace(pme);
		} else {
			StringBuffer namespace = new StringBuffer();
			ProgramElement p = pe.getASTParent();
			while (p != null) {
				if (p instanceof NamespaceSpecification) {
					if (!namespace.equals("")) {
						namespace.insert(0, ".");
					}
					namespace.insert(
						0,
						((NamespaceSpecification) p)
							.getNamespaceReference()
							.getFullName());
				}
				p = p.getASTParent();
			}
			result = getNameInfo().createNamespace(namespace.toString());
		}
		return result;
	}

	public DeclaredTypeList getTypes(DeclaredTypeContainer ctc) {
		Debug.asserta(ctc);
		updateModel();
		ProgramElement decl = getDeclaration(ctc);
		if (decl == null) {
			return ctc.getProgramModelInfo().getTypes(ctc);
		} else {
			while (decl != null && !(decl instanceof TypeScope)) {
				decl = decl.getASTParent();
			}
			Debug.asserta(decl, "Internal error - scope inconsistency");
			return ((TypeScope) decl).getTypesInScope();
		}
	}

	// TODO: Check if we need this method, and reintroduce it to the interfaces.
	//	public ClassTypeList getTypes(Namespace ctc) {
	//		Debug.assert(ctc);
	//		updateModel();
	//		ProgramElement decl = getDeclaration(ctc);
	//		if (decl == null) {
	//			return ctc.getProgramModelInfo().getTypes(ctc);
	//		} else {
	//			while (decl != null && !(decl instanceof TypeScope)) {
	//				decl = decl.getASTParent();
	//			}
	//			Debug.assert(decl, "Internal error - scope inconsistency");
	//			return ((TypeScope) decl).getTypesInScope();
	//		}
	//	}

	public DeclaredTypeContainer getDeclaredTypeContainer(DeclaredType ct) {
		Debug.asserta(ct);
		TypeDeclaration td = getTypeDeclaration(ct);

		if (td == null) {
			return ct.getProgramModelInfo().getDeclaredTypeContainer(ct);
		}

		// updateModel(); not necessary
		ProgramElement cur = td;
		NonTerminalProgramElement par = cur.getASTParent();
		StringBuffer namespace = new StringBuffer();

		while (par != null) {
			cur = par;
			if (cur instanceof DeclaredTypeContainer) {
				return (DeclaredTypeContainer) cur;
			}

			if (cur instanceof NamespaceSpecification) {
				if (namespace.length() > 0) {
					namespace.insert(0, ".");
				}
				namespace.insert(
					0,
					((NamespaceSpecification) cur).getNamespaceReference().getFullName());
			}
			par = cur.getASTParent();
		}

		return getNameInfo().createNamespace(namespace.toString());

	}

	ClassTypeMutableList getClassTypeList(TypeReferenceList trl) {
		updateModel();
		int s = (trl != null) ? trl.size() : 0;
		ClassTypeMutableList result = new ClassTypeArrayList(s);
		for (int i = 0; i < s; i++) {
			result.add((ClassType) getType(trl.getTypeReference(i)));
		}
		return result;
	}

	void addToClassTypeList(ClassTypeMutableList result, TypeReferenceList trl) {
		//	updateModel();
		int s = (trl != null) ? trl.size() : 0;
		result.ensureCapacity(result.size() + s);
		for (int i = 0; i < s; i++) {
			TypeReference tr = trl.getTypeReference(i);
			if (tr != null) {
				ClassType ct = (ClassType) getType(tr);
				if (ct == null) {
					getErrorHandler().reportError(
						new UnresolvedReferenceException(
							Format.toString("Unable to resolve " + ELEMENT_LONG, tr),
							tr));
					ct = getNameInfo().getUnknownClassType();
				}
				result.add(ct);
			}
		}
	}

	public ClassTypeList getSupertypes(ClassType ct) {
		Debug.asserta(ct);
		updateModel();
		TypeDeclaration td = getTypeDeclaration(ct);
		if (td == null) {
			return ct.getProgramModelInfo().getSupertypes(ct);
		} else {
			ClassTypeCacheEntry ctce = (ClassTypeCacheEntry) classTypeCache.get(ct);
			if (ctce == null) {
				classTypeCache.put(ct, ctce = new ClassTypeCacheEntry());
			}
			if (ctce.supertypes != null) {
				return ctce.supertypes;
			}
			ClassTypeMutableList res = new ClassTypeArrayList();
			if (td instanceof InterfaceDeclaration) {
				InterfaceDeclaration id = (InterfaceDeclaration) td;
				Extends ext = id.getExtendedTypes();
				if (ext != null) {
					addToClassTypeList(res, ext.getSupertypes());
				}
			} else {
				ClassTypeDeclaration cd = (ClassTypeDeclaration) td;

				// Anonymous classes need special care
				TypeDeclarationContainer con = cd.getParent();
				// DISABLED: No support for anonymous classes in C#
				//				if (con instanceof New) {
				//					TypeReference tr = ((New) con).getTypeReference();
				//					res.add((ClassType) getType(tr));
				//				} else {
				Extends ext = cd.getExtendedTypes();
				if (ext != null) {
					addToClassTypeList(res, ext.getSupertypes());
				}
				//DISABLED: We don't use Implements in C#
				//					Implements imp = cd.getImplementedTypes();
				//					if (imp != null) {
				//						addToClassTypeList(res, imp.getSupertypes());
				//					}

				//				}
			}
			if (res.isEmpty()) {
				ClassType jlo = getNameInfo().getSystemObject();
				if (ct != jlo) {
					res.add(jlo);
				}
			}
			return ctce.supertypes = res;
		}
	}

	public FieldList getFields(ClassType ct) {
		Debug.asserta(ct);
		updateModel();
		FieldList result = null;
		TypeDeclaration td = getTypeDeclaration(ct);
		if (td == null) {
			result = ct.getProgramModelInfo().getFields(ct);
		} else {
			if (td instanceof ClassTypeDeclaration)
				result = getFields((ClassTypeDeclaration) td);
		}
		return result;
	}

	public MethodList getMethods(ClassType ct) {
		Debug.asserta(ct);
		updateModel();
		MethodList result = null;
		TypeDeclaration td = getTypeDeclaration(ct);
		if (td == null) {
			result = ct.getProgramModelInfo().getMethods(ct);
		} else {
			result = getMethods((ClassTypeDeclaration) td);
		}
		return result;
	}

	public ConstructorList getConstructors(ClassType ct) {
		Debug.asserta(ct);
		updateModel();
		ConstructorList result = null;
		TypeDeclaration td = getTypeDeclaration(ct);
		if (td == null) {
			result = ct.getProgramModelInfo().getConstructors(ct);
		} else {
			if (td instanceof ClassTypeDeclaration)
				result = getConstructors((ClassTypeDeclaration) td);
		}
		return result;
	}

	public TypeList getSignature(Method m) {
		Debug.asserta(m);
		updateModel();
		TypeList result = TypeList.EMPTY_LIST;
		MethodDeclaration md = getMethodDeclaration(m);
		if (md == null) {
			result = m.getProgramModelInfo().getSignature(m);
		} else {
			ParameterDeclarationList pdl = md.getParameters();
			int params = (pdl == null) ? 0 : pdl.size();
			if (params > 0) {
				TypeMutableList res = new TypeArrayList(params);
				result = res;
				for (int i = 0; i < params; i++) {
					Type ptype =
						getType(
							pdl
								.getParameterDeclaration(i)
								.getVariables()
								.getVariableSpecification(
								0));
					res.add(ptype);
				}
			}
		}
		return result;
	}

	public TypeList getSignature(Delegate d) {
		Debug.asserta(d);
		updateModel();
		TypeList result = TypeList.EMPTY_LIST;
		DelegateDeclaration md = getDelegateDeclaration(d);
		if (md == null) {
			result = d.getProgramModelInfo().getSignature(d);
		} else {
			ParameterDeclarationList pdl = md.getParameters();
			int params = (pdl == null) ? 0 : pdl.size();
			if (params > 0) {
				TypeMutableList res = new TypeArrayList(params);
				result = res;
				for (int i = 0; i < params; i++) {
					Type ptype =
						getType(
							pdl
								.getParameterDeclaration(i)
								.getVariables()
								.getVariableSpecification(
								0));
					res.add(ptype);
				}
			}
		}
		return result;
	}

	public ClassTypeList getExceptions(Method m) {
		Debug.asserta(m);
		updateModel();
		ClassTypeList result = ClassTypeList.EMPTY_LIST;
		MethodDeclaration md = getMethodDeclaration(m);
		if (md == null) {
			result = m.getProgramModelInfo().getExceptions(m);
		} else {
			Throws t = md.getThrown();
			if (t != null) {
				result = getClassTypeList(t.getExceptions());
			}
		}
		return result;
	}

	public Type getReturnType(Method m) {
		Debug.asserta(m);
		updateModel();
		Type result = null;
		MethodDeclaration md = getMethodDeclaration(m);
		if (md == null) {
			result = m.getProgramModelInfo().getReturnType(m);
		} else {
			TypeReference tr = md.getTypeReference();
			if (tr != null && !"void".equals(tr.getName())) {
				result = getType(tr);
			}
		}
		return result;
	}

	public Type getReturnType(Delegate d) {
		Debug.asserta(d);
		updateModel();
		Type result = null;
		DelegateDeclaration md = getDelegateDeclaration(d);
		if (md == null) {
			result = d.getProgramModelInfo().getReturnType(d);
		} else {
			TypeReference tr = md.getTypeReference();
			if (tr != null && !"void".equals(tr.getName())) {
				result = getType(tr);
			}
		}
		return result;
	}

	public Reference resolveURQ(UncollatedReferenceQualifier urq) {
		NonTerminalProgramElement parent = urq.getASTParent();

		// In C# we cannot resolve method calls either.
		// This shall solve the problem (hopefully)
		if (urq instanceof UncollatedMethodCallReference)
			return resolveUncollatedMethodCallReference(
				(UncollatedMethodCallReference) urq,
				!((parent instanceof TypeReference)
					|| (parent instanceof NamespaceReference)));

		return resolveURQ(
			urq,
			!((parent instanceof TypeReference)
				|| (parent instanceof NamespaceReference)));
	}

	/**
	 * Method resolveUncollatedMethodCallReference.
	 * @param urq
	 * @param b
	 * @return Reference
	 */
	private Reference resolveUncollatedMethodCallReference(
		UncollatedMethodCallReference urq,
		boolean allowVariables) {
		Debug.asserta(urq);
		Reference result = null;
		NonTerminalProgramElement parent = urq.getASTParent();

		// Try to resolve this first as a delegate
		if (allowVariables) {
			// Search for a variable with this name
			Variable v = getVariable(urq.getName(), urq);

			if (v != null && v.getType() instanceof Delegate)
				result = urq.toDelegateCallReference();
				
		}

		if (result == null) {
			// Look it up as a method - for performance reasons, there is no
			// direct search here.
			result = urq.toMethodReference();
		}

		// If we managed to find a reference, replace it, if not, report error
		if (result != null && result != urq) {
			parent.replaceChild(urq, result);
			Debug.asserta(parent == result.getASTParent());
		} else {
			getErrorHandler().reportError(
				new UnresolvedReferenceException(
					Format.toString("Could not resolve " + ELEMENT_LONG + " (11)", urq),
					urq));
			result = urq;
		}

		return result;

	}

	protected Reference resolveURQ(
		UncollatedReferenceQualifier urq,
		boolean allowVariables) {
		Debug.asserta(urq);

		// recursively resolve the prefix of the reference.
		ReferencePrefix rp = urq.getReferencePrefix();
		if (rp instanceof UncollatedReferenceQualifier) {
			rp =
				(ReferencePrefix) resolveURQ((UncollatedReferenceQualifier) rp,
					allowVariables);
		}
		updateModel();
		Reference result = null;
		NameInfo ni = getNameInfo();
		NonTerminalProgramElement parent = urq.getASTParent();
		String urqName = urq.getName();

		if (parent instanceof New) {
			// New must be handled specially, because we may also have a reference to a 
			// method too, if we create a new delegate.

			New _new = (New) parent;
			Type newType = getType(_new.getTypeReference());

			// Check for delegate creation
			if (newType instanceof Delegate) {

				if (DEBUG)
					Debug.log("Found a delegate-new: " + parent.toSource());

				// There are two cases: the argument can either be a variable with a delegate type

				Variable v = getVariable(urqName, urq);
				if (DEBUG)
					Debug.log("Searching variable " + urqName);
				if (v != null) {
					if (DEBUG)
						Debug.log("Found. Checking compatibility.");
					// Check if this is a compatible delegate
					if (!(v.getType() instanceof Delegate))
						getErrorHandler().reportError(
							new UnresolvedReferenceException(
								Format.toString(
									"Variable is not copatible with the given delegate "
										+ ELEMENT_LONG
										+ " (07)",
									urq),
								urq));

					result =
						v instanceof Field
							? urq.toFieldReference()
							: urq.toVariableReference();
				} else {
					// Well, it was not a variable, so it must be a class member

					result = urq.toMethodGroupReference();
					// Here we make no check, whether this is _really_ a reference
					// this has to be done by the source info.
				}
			}

		}

		if (result == null) {
			if (rp == null) {
				if (allowVariables) {
					// is it a variable?
					Variable v = getVariable(urqName, urq);
					if (v != null) {
						result =
							(v instanceof Field)
								? urq.toFieldReference()
								: urq.toVariableReference();
						reference2element.put(result, v);
					}
				} else if (parent instanceof MethodReference) {
					// this case is common enough for special treatment
					result = urq.toTypeReference();
				}

				if (result == null) {

					// is the URQ a reference to an already known namespace?
					Namespace pkg = ni.getNamespace(urqName);
					if (pkg != null) {
						result = urq.toNamespaceReference();
						reference2element.put(result, pkg);
					} else {

						// the urq might only be either a type or a namespace ref
						Type t = getType(urqName, urq);

						if (t != null) {
							result = urq.toTypeReference();
							reference2element.put(result, t);

						} else {
							// MUST be a reference to an unknown namespace
							try {
								result = urq.toNamespaceReference();
							} catch (ClassCastException cce) {
								getErrorHandler().reportError(
									new UnresolvedReferenceException(
										Format.toString(
											"Could not resolve " + ELEMENT_LONG + " (07)",
											urq),
										urq));
								result = urq.toTypeReference();

							}
						}
					}
				}
			} else if (rp instanceof ThisReference) {
				// the URQ can only be a local inner type or an attribute
				TypeScope thisScope;
				ReferencePrefix rpp = ((ThisReference) rp).getReferencePrefix();

				if (rpp == null) {
					// If there is no prefix, the containing classtype is used.
					thisScope = (TypeScope) getContainingClassType(urq);
				} else {
					//	If its a typereference, the rpp is converted, else resolved.
					TypeReference tr =
						(rpp instanceof TypeReference)
							? (TypeReference) rpp
							: (TypeReference) resolveURQ((UncollatedReferenceQualifier) rpp,
								false);
					// from the typereference is the scope resolved.
					thisScope = (TypeDeclaration) getType(tr);
				}

				Variable v = getVariable(urqName, thisScope);
				if (v != null) {
					result = urq.toFieldReference();
					reference2element.put(result, v);
				} else {
					// the URQ is either a type reference or invalid
					Type refT = thisScope.getTypeInScope(urqName);
					if (refT != null) {
						result = urq.toTypeReference();
						reference2element.put(result, refT);
					}
				}
			} else if (rp instanceof SuperReference) {
				// the URQ can only be an inner type or a field reference
				ClassType superType = (ClassType) getType(rp);
				Field f = getInheritedField(urq.getName(), superType);
				if (f != null) {
					result = urq.toFieldReference();
					reference2element.put(result, f);
				} else {
					String fullname = Naming.getFullName(superType, urq.getName());
					ClassType ct = ni.getClassType(fullname);
					if (ct != null) {
						result = urq.toTypeReference();
						reference2element.put(result, ct);
					}
				}
			} else if (rp instanceof NamespaceReference) {
				String fullRefName = Naming.toPathName(urq);
				// is the URQ a reference to an already known package?
				Namespace pkg = ni.getNamespace(fullRefName);
				if (pkg != null) {
					result = urq.toNamespaceReference();
					reference2element.put(result, pkg);
				} else {
					// is it a type?
					Type t = ni.getClassType(fullRefName);
					if (t != null) {
						result = urq.toTypeReference();
						reference2element.put(result, t);
					} else {
						result = urq.toNamespaceReference();
					}
				}
			} else if ((rp instanceof TypeReference) || (rp instanceof Expression)) {
				//  includes VariableReferences
				Type refT = getTypeOfPrefix(rp);

				if (refT instanceof Enum) {
					// Enums must be handled separately.
					// Here we can only have a enum member reference

					if (allowVariables) {
						// We will try to resolve this reference immediately
						EnumMember f = getEnumMember(urq.getName(), (Enum) refT);

						if (f != null) {
							result = urq.toEnumMemberReference();
							reference2element.put(result, f);
						}
					}

					// If we don't have this reference, we are lost...
					getErrorHandler().reportError(
						new UnresolvedReferenceException(
							Format.toString(
								"Could not resolve " + ELEMENT_LONG + " (EnumMember)",
								urq),
							urq));

				} else if (refT instanceof ClassType) {

					if (allowVariables) {
						Field f = getInheritedField(urq.getName(), (ClassType) refT);
						if (f != null) {
							result = urq.toFieldReference();
							reference2element.put(result, f);
						}
					}

					if (result == null) {
						String fullname =
							Naming.getFullName((ClassType) refT, urq.getName());
						ClassType innerType = ni.getClassType(fullname);
						if (innerType != null) {
							result = urq.toTypeReference();
							reference2element.put(result, innerType);
						}
					}

					// DISABLED: This case is no longer possible, Array type must be resolved to
					// the System.Array classtype
					//			   } else if (refT instanceof ArrayType) {
					//					if (allowVariables && urq.getName().equals("Length")) {
					//						result = urq.toArrayLengthReference();
					//					} else {
					//						getErrorHandler().reportError(
					//							new UnresolvedReferenceException(
					//								Format.toString(
					//									"Could not resolve " + ELEMENT_LONG + " (08)",
					//									urq),
					//								urq));
					//						// this IS an error in any case, but so what
					//						result = urq;
					//					}
				} else {
					getErrorHandler().reportError(
						new UnresolvedReferenceException(
							Format.toString(
								"Could not resolve " + ELEMENT_LONG + " (09)",
								rp),
							rp));
					// this would have been a class or a field
					result = urq;
				}
			} else {
				getErrorHandler().reportError(
					new UnresolvedReferenceException(
						Format.toString(
							"Could not resolve " + ELEMENT_LONG + " (10)",
							rp),
						rp));
				// this would have been a class or a field or a package
				result = urq;
			}
		}
		if (result == null) {
			getErrorHandler().reportError(
				new UnresolvedReferenceException(
					Format.toString("Could not resolve " + ELEMENT_LONG + " (11)", urq),
					urq));
			result = urq;
		} else if (result != urq) {
			parent.replaceChild(urq, result);
			Debug.asserta(parent == result.getASTParent());
		}
		return result;
	}

	/** looks for the next variable scope that is a parent of the given element
	@param  pe a program element
	@return the outer variable scope of the program element or <tt>null</tt>
	*/
	private static VariableScope findOuterVariableScope(VariableScope ts) {
		NonTerminalProgramElement pe = ts.getASTParent();
		while (pe != null && !(pe instanceof VariableScope)) {
			pe = pe.getASTParent();
		}
		return (VariableScope) pe;
	}

	// TODO: Implement new elements of the C# language
	public StatementList getSucceedingStatements(Statement s) {
		StatementMutableList list = new StatementArrayList();

		if (s instanceof LoopStatement) {
			// If in a Loop0
			LoopStatement loop = (LoopStatement) s;
			switch (getBooleanStatus(loop.getGuard())) {
				case CONSTANT_TRUE :
					if (loop.getBody() != null) {
						list.add(loop.getBody());
					}
					break;
				case CONSTANT_FALSE :
					if (loop.isCheckedBeforeIteration()) {
						// while, for
						addSequentialFollower(s, list);
					} else {
						// do
						if (loop.getBody() != null) {
							list.add(loop.getBody());
						}
						addSequentialFollower(s, list);
					}
					break;
				case NOT_CONSTANT :
					if (loop.getBody() != null) {
						list.add(loop.getBody());
					}
					addSequentialFollower(s, list);
					break;
			}
		} else if (s instanceof LabeledStatement) {
			list.add(((LabeledStatement) s).getBody());
		} else if (s instanceof StatementBlock) {
			StatementList slist = ((StatementBlock) s).getBody();
			if (slist == null || slist.isEmpty()) {
				addSequentialFollower(s, list);
			} else {
				list.add(slist.getStatement(0));
			}
		} else if (s instanceof LockedBlock) {
			// TODO: Test
			Statement slist = ((LockedBlock) s).getBody();
			list.add(slist);
		} else if (s instanceof If) {
			If ifstmt = (If) s;
			if (ifstmt.getElse() != null) {
				list.add(ifstmt.getThen().getBody());
				list.add(ifstmt.getElse().getBody());
			} else {
				list.add(ifstmt.getThen().getBody());
				addSequentialFollower(s, list);
			}
		} else if (s instanceof Switch) {
			BranchList branches = ((Switch) s).getBranchList();
			if (branches == null || branches.isEmpty()) {
				addSequentialFollower(s, list);
			} else {
				boolean hasDefault = false;
				int usefulBranches = 0;
				for (int i = 0, c = branches.size(); i < c; i += 1) {
					Branch b = branches.getBranch(i);
					StatementList stats = null;
					if (b instanceof Default) {
						stats = ((Default) b).getBody();
						if (i < c - 1 || (stats != null && !stats.isEmpty())) {
							// an empty default as last branch is not
							// significant
							hasDefault = true;
						}
					} else if (b instanceof Case) {
						stats = ((Case) b).getBody();
					}
					if (stats != null && !stats.isEmpty()) {
						list.add(stats.getStatement(0));
					}
				}
				if (!hasDefault) {
					addSequentialFollower(s, list);
				}
			}
		} else if (s instanceof Try) {
			list.add(((Try) s).getBody());
			BranchList branches = ((Try) s).getBranchList();
			if (branches == null || branches.isEmpty()) {
				addSequentialFollower(s, list);
				return list;
			}
			for (int i = 0; i < branches.size(); i += 1) {
				Branch b = branches.getBranch(i);
				if (b instanceof Catch) {
					Catch ca = (Catch) b;
					boolean newException = true;
					if (i > 0) {
						ClassType ex =
							(ClassType) getType(ca
								.getParameterDeclaration()
								.getTypeReference());
						for (int j = i - 1; j >= 0; j -= 1) {
							if (branches.getBranch(j) instanceof Catch) {
								ClassType dx =
									(ClassType) getType(((Catch) branches.getBranch(j))
										.getParameterDeclaration()
										.getTypeReference());
								if (isSubtype(ex, dx)) {
									// exception was already caught
									newException = false;
									break;
								}
							}
						}
					}
					if (newException) {
						list.add(ca.getBody());
					}
				} else if (b instanceof Finally) {
					list.add(((Finally) b).getBody());
				}
			}
			addSequentialFollower(s, list);
		} else if (s instanceof ExpressionJumpStatement) {
			// Return, Throw
			list.add(METHOD_EXIT);
			// DISABLED Commented because they are no longer LabelJumpStatements
			//	} else if (s instanceof Break) {
			//	    if (((Break)s).getIdentifier() == null) {
			//		addSequentialFollower(findInnermostBreakBlock(s), list);
			//	    } else {
			//		addSequentialFollower(StatementKit.getCorrespondingLabel((Break)s), list);
			//	    }
			//	} else if (s instanceof Continue) {
			//	    if (((Continue)s).getIdentifier() == null) {
			//		list.add(findInnermostLoop(s));
			//	    } else {
			//		list.add(StatementKit.getCorrespondingLabel((Continue)s).getBody());
			//	    }
		} else {
			/*
			  ConstructorReference:
			  EmptyStatement:
			  ExpressionStatement:
			  LoopInitializer:
			  ClassDeclaration:
			  Assert:
			*/
			addSequentialFollower(s, list);
		}
		return list;
	}

	private static void addSequentialFollower(Statement s, StatementMutableList list) {
		Debug.asserta(s);
		StatementContainer parent = s.getStatementContainer();
		while (true) {
			int c = parent.getStatementCount();
			int p = 0;
			while (parent.getStatementAt(p) != s) {
				p += 1;
			}
			if (p < c - 1) {
				list.add(parent.getStatementAt(p + 1));
				break;
			}
			if (parent instanceof MemberDeclaration) {
				list.add(METHOD_EXIT);
				break;
			}
			if (parent instanceof Statement) {
				if (parent instanceof LoopStatement) {
					LoopStatement loop = (LoopStatement) parent;

					list.add(loop);
					return;
				}
				s = (Statement) parent;
				parent = ((Statement) parent).getStatementContainer();
			} else {
				while (parent instanceof Branch) {
					s = ((Branch) parent).getParent();
					parent = s.getStatementContainer();
				}
			}
		}
	}

	private final static int CONSTANT_FALSE = 0;
	private final static int CONSTANT_TRUE = 1;
	private final static int NOT_CONSTANT = -1;

	private int getBooleanStatus(Expression expr) {
		if (expr == null) { // handle "for(...;;...)" situation
			return CONSTANT_TRUE;
		}
		ConstantEvaluator.EvaluationResult evr = new ConstantEvaluator.EvaluationResult();
		if (serviceConfiguration
			.getConstantEvaluator()
			.isCompileTimeConstant(expr, evr)) {
			return evr.getBoolean() ? CONSTANT_TRUE : CONSTANT_FALSE;
		}
		return NOT_CONSTANT;
	}

	// LoopStatement or Switch
	private static Statement findInnermostBreakBlock(Statement s) {
		NonTerminalProgramElement parent = s.getStatementContainer();
		while (parent != null && !(parent instanceof MemberDeclaration)) {
			if ((parent instanceof LoopStatement) || (parent instanceof Switch)) {
				return (Statement) parent;
			}
			parent = parent.getASTParent();
		}
		return null;
	}

	private static LoopStatement findInnermostLoop(Statement s) {
		NonTerminalProgramElement parent = s.getStatementContainer();
		while (parent != null && !(parent instanceof MemberDeclaration)) {
			if (parent instanceof LoopStatement) {
				return (LoopStatement) parent;
			}
			parent = parent.getASTParent();
		}
		return null;
	}

	/**
	   Analyzes the given program subtree.  It is required that the
	   tree has consistent parent links; this is done by the parser
	   frontends or by calling make(All)ParentRole(s)Valid().  If the
	   program element is not a CompilationUnit, it must have a valid
	   parent.
	   @param pe the program element to add.
	   @deprecated
	*/
	public void register(ProgramElement pe) {
		Debug.asserta(pe);
		if (pe instanceof CompilationUnit) {
			if (!((CompilationUnit) pe).isDefinedScope()) {
				analyzeProgramElement(pe);
			}
		} else {
			Debug.asserta(pe.getASTParent());
			analyzeProgramElement(pe);
		}
	}

	/** analyzes the given tree element within the specified scope.
	    @param pe    the root element of the tree to be analyzed
	*/
	private void analyzeProgramElement(ProgramElement pe) {
		Debug.asserta(pe);
		analyzeProgramElement0(pe);
	}

	// TODO: Check the new elements of the C# language. 
	private void analyzeProgramElement0(ProgramElement pe) {
		if (pe instanceof TerminalProgramElement) {
			return;
		}

		// traversal will continue with the children of this element
		if (pe instanceof ScopeDefiningElement) {
			((ScopeDefiningElement) pe).setDefinedScope(true);

			if (pe instanceof NamespaceSpecification) {
				StringBuffer namespace =
					new StringBuffer(((NamespaceSpecification) pe).getName());
				NonTerminalProgramElement par = pe.getASTParent();
				while (par != null) {
					if (par instanceof NamespaceSpecification) {
						namespace.insert(
							0,
							((NamespaceSpecification) par).getName() + ".");
					}
					par = par.getASTParent();
				}
				getNameInfo().createNamespace(namespace.toString());

			}

			if (pe instanceof MethodDeclaration) {
				// also for ConstructorDeclarations
				 ((MethodDeclaration) pe).setProgramModelInfo(this);

			} else if (pe instanceof TypeDeclaration) {
				TypeDeclaration td = (TypeDeclaration) pe;
				td.setProgramModelInfo(this);
				String typename = td.getName();

				if (typename != null) {
					NonTerminalProgramElement parent = pe.getASTParent();
					// usually, the type scope is just the parent
					// there are few exceptions, such as labeled or switch 
					// statements
					while (!(parent instanceof TypeScope)) {
						parent = parent.getASTParent();
					}

					TypeScope scope = (TypeScope) parent;
					DeclaredType dup = scope.getTypeInScope(typename);

					if (dup != null && dup != td) {
						getErrorHandler().reportError(
							new AmbiguousDeclarationException(
								"Duplicate declaration of "
									+ Format.toString(ELEMENT_SHORT, td)
									+ " - was "
									+ Format.toString(ELEMENT_SHORT, dup),
								td,
								dup));
						// continue anyway, if we have to
					}

					scope.addTypeToScope(td, typename);
					if (DEBUG)
						Debug.log(Format.toString("Registering %N", td));
					getNameInfo().register(td);
				}
			}
		} else if (pe instanceof VariableSpecification) {
			// also for FieldSpecification
			VariableSpecification vs = (VariableSpecification) pe;
			vs.setProgramModelInfo(this);
			NonTerminalProgramElement parent = vs.getASTParent().getASTParent();
			// usually, the variable scope is the grand parent
			// there are few exceptions, such as labeled or switch statements
			while (!(parent instanceof VariableScope)) {
				parent = parent.getASTParent();
			}
			VariableScope scope = (VariableScope) parent;

			// If the scope is a DelegateDeclaration, we don't add the Variable.
			if (!(parent instanceof DelegateDeclaration)) {

				String vname = vs.getName();
				Variable dup = scope.getVariableInScope(vname);
				if (dup != null && dup != vs) {
					getErrorHandler().reportError(
						new AmbiguousDeclarationException(
							"Duplicate declaration of "
								+ Format.toString(ELEMENT_SHORT, vs)
								+ " - was "
								+ Format.toString(ELEMENT_SHORT, dup),
							vs,
							dup));
					// continue anyway, if we have to resume
				}

				// check if the new variable hides a local variable
				if (!(scope instanceof TypeDeclaration)) {
					for (VariableScope outer = findOuterVariableScope(scope);
						!(outer instanceof TypeDeclaration);
						outer = findOuterVariableScope(outer)) {
						dup = outer.getVariableInScope(vname);
						if (dup != null) {
							getErrorHandler().reportError(
								new AmbiguousDeclarationException(
									"Hidden local declaration: "
										+ Format.toString(ELEMENT_SHORT, vs)
										+ " - hides "
										+ Format.toString(ELEMENT_SHORT, dup),
									vs,
									dup));
							// resume anyway
						}
					}
				}
				scope.addVariableToScope(vs);
				if (vs instanceof FieldSpecification) {
					getNameInfo().register((Field) vs);
				}
			}
		}
		NonTerminalProgramElement cont = (NonTerminalProgramElement) pe;
		int childCount = cont.getChildCount();
		for (int i = 0; i < childCount; i++) {
			analyzeProgramElement0(cont.getChildAt(i));
		}
	}

	void unregister(TypeDeclaration td) {
		unregister(td, td.getName());
	}

	/**
	   Remove given type from outer scope, from name info global dictionary,
	   and from subtype list of all known supertypes (if necessary).       
	 */
	void unregister(TypeDeclaration td, String shortname) {
		if (shortname != null) {
			((TypeScope) (td.getASTParent())).removeTypeFromScope(shortname);
		}
		if (td instanceof ClassTypeDeclaration) {
			getNameInfo().unregisterClassType(td.getFullName());
			ClassTypeCacheEntry ctce = (ClassTypeCacheEntry) classTypeCache.get(td);
			if (ctce != null) {
				ClassTypeList superTypes = ctce.supertypes;
				if (superTypes != null) {
					for (int i = superTypes.size() - 1; i >= 0; i -= 1) {
						removeSubtype(
							(ClassTypeDeclaration) td,
							superTypes.getClassType(i));
					}
				}
			}
		}
	}

	void unregister(VariableSpecification vs) {
		unregister(vs, vs.getName());
	}

	void unregister(VariableSpecification vs, String shortname) {
		ProgramElement pe = vs.getASTParent().getASTParent();
		while (!(pe instanceof VariableScope)) {
			pe = pe.getASTParent();
		}
		((VariableScope) pe).removeVariableFromScope(shortname);
		if (vs instanceof FieldSpecification) {
			ClassType ct = ((Field) vs).getContainingClassType();
			getNameInfo().unregisterField(Naming.getFullName(ct, shortname));
		}
	}

	/** unregisters the information, that has been computed when registering
	the given element.
	@param pe the program element to be unregistered
	*/
	void unregister(ProgramElement pe) {
		Debug.asserta(pe);
		if (pe instanceof TypeDeclaration) {
			unregister((TypeDeclaration) pe);
		} else if (pe instanceof VariableSpecification) {
			unregister((VariableSpecification) pe);
		} else if (pe instanceof VariableDeclaration) {
			VariableSpecificationList vspecs = ((VariableDeclaration) pe).getVariables();
			for (int i = vspecs.size() - 1; i >= 0; i -= 1) {
				unregister(vspecs.getVariableSpecification(i));
			}
		}
		TreeWalker tw = new TreeWalker(pe);
		while (tw.next()) {
			pe = tw.getProgramElement();
			if (pe instanceof ScopeDefiningElement) {
				flushScopes((ScopeDefiningElement) pe);
			}
		}
	}

	void flushScopes(ScopeDefiningElement sde) {
		DefaultNameInfo dni = (DefaultNameInfo) getNameInfo();
		if (sde instanceof TypeScope) {
			DeclaredTypeList ctl = ((TypeScope) sde).getTypesInScope();
			if (sde instanceof CompilationUnit) {
				// handle special case of top level CU scopes
				// caching known imported types
				// --- should be redone somewhen
				for (int j = ctl.size() - 1; j >= 0; j -= 1) {
					DeclaredType ct = (DeclaredType) ctl.getDeclaredType(j);
					if ((ct instanceof TypeDeclaration)
						&& ((TypeDeclaration) ct).getASTParent() == sde) {
						dni.unregisterClassType(ct.getFullName());
					}
				}
			} else {
				for (int j = ctl.size() - 1; j >= 0; j -= 1) {
					dni.unregisterClassType(ctl.getDeclaredType(j).getFullName());
				}
			}
		}
		if (sde instanceof TypeDeclaration) {
			FieldList fl = ((TypeDeclaration) sde).getFieldsInScope();
			for (int j = fl.size() - 1; j >= 0; j -= 1) {
				dni.unregisterField(fl.getField(j).getFullName());
			}
		}
		sde.setDefinedScope(false);
	}

	public void reset() {
		super.reset();
		reference2element.clear();
		SourceFileRepository sfr = serviceConfiguration.getSourceFileRepository();
		CompilationUnitList cul = sfr.getCompilationUnits();
		DefaultNameInfo dni = (DefaultNameInfo) getNameInfo();
		dni.unregisterNamespaces();
		for (int i = cul.size() - 1; i >= 0; i -= 1) {
			CompilationUnit cu = cul.getCompilationUnit(i);
			// remove all scopes
			unregister(cu);
			// now rebuild scopes
			analyzeProgramElement(cu);
		}
	}

	/** Returns the basetype of an enum.
	 * @see recoder.service.ProgramModelInfo#getBaseType(Enum)
	 */
	public Type getBaseType(Enum e) {
		Debug.asserta(e);
		updateModel();
		Type result = null;
		EnumDeclaration ed = getEnumDeclaration(e);
		if (ed == null) {
			result = e.getProgramModelInfo().getBaseType(e);
		} else {
			TypeReference tr = ed.getBaseTypeReference();
			if (tr != null && !"void".equals(tr.getName())) {
				result = getType(tr);
			}
		}
		return result;
	}

	/**
	 * @see recoder.service.ProgramModelInfo#getFields()
	 */
	public EnumMemberList getFields(Enum e) {
		Debug.asserta(e);
		updateModel();
		EnumMemberMutableList result = null;
		EnumDeclaration ed = getEnumDeclaration(e);
		if (ed == null) {
			return e.getProgramModelInfo().getFields(e);
		} else {
			return getFields(ed);
		}
	}

	public EnumMemberList getFields(EnumDeclaration ed) {
		Debug.asserta(ed);
		updateModel();
		MemberDeclarationList members = ed.getMembers();
		if (members == null) {
			return EnumMemberList.EMPTY_LIST;
		}
		int s = members.size();
		EnumMemberMutableList result = new EnumMemberArrayList();
		for (int i = 0; i < s; i += 1) {
			MemberDeclaration m = members.getMemberDeclaration(i);
			if (m instanceof EnumMemberDeclaration) {
				result.add(((EnumMemberDeclaration) m).getEnumMember());
			}
		}
		return result;
	}

	/**
	 * @see recoder.service.ProgramModelInfo#getContainingEnum(EnumMember)
	 */
	public Enum getContainingEnum(EnumMember e) {
		Debug.asserta(e);
		Enum result = null;
		ProgramElement pe = getDeclaration(e);
		if (pe == null) {
			result = e.getProgramModelInfo().getContainingEnum(e);
		} else {
			ProgramElement context = pe;
			do {
				if (context instanceof EnumDeclaration) {
					return (Enum) context;
				}
				context = context.getASTParent();
			} while (context != null);
		}
		return result;
	}

	/** Returns the named enum member in an enum. */
	private EnumMember getEnumMember(String name, Enum e) {
		EnumMember f = null;
		EnumMemberList flds = e.getFields();
		for (int i = flds.size() - 1; i >= 0; i--) {
			EnumMember em = flds.getEnumMember(i);
			if (em.getName().equals(name))
				f = em;
		}
		return f;
	}

}
