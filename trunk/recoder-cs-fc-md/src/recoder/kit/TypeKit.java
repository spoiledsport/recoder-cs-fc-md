// This file is part of the RECODER library and protected by the LGPL

package recoder.kit;

import recoder.*;
import recoder.convenience.*;
import recoder.abstraction.*;
import recoder.abstraction.Namespace;
import recoder.csharp.*;
import recoder.csharp.declaration.*;
import recoder.csharp.statement.*;
import recoder.csharp.declaration.modifier.*;
import recoder.csharp.expression.*;
import recoder.csharp.expression.operator.*;
import recoder.csharp.reference.*;
import recoder.list.*;
import recoder.io.*;
import recoder.service.*;
import recoder.util.*;

/** this class implements basic functions for type handling.
    @author Uwe Assmann
    @author Andreas Ludwig
    @author Rainer Neumann
    @author Dirk Heuzeroth
*/
public class TypeKit {

	private TypeKit() {}

	/** Factory method that creates a new type reference fitting to the
		constructor declaration.
	    The reference is not prefixed as its use context is unknown.
		This method is useful for creating factory classes.
	@param    decl a constructor declaration.
	@return   the newly created type reference, parent links are valid.
	*/
	public static TypeReference createTypeReference(ConstructorDeclaration decl) {
		ProgramFactory f = decl.getFactory();
		TypeReference result = f.createTypeReference(f.createIdentifier(decl.getName()));
		result.makeAllParentRolesValid();
		return result;
	}

	/** Factory method that creates a type reference with prefices
	(UncollatedReferenceQualifiers) from a qualified name.
	@param f the program factory to use.
	@param qualifiedName a qualified (= potentially dotted) name.
	@return a type reference to the given type; parent links of
	the reference are made valid.
	*/
	public static TypeReference createTypeReference(
		ProgramFactory f,
		String qualifiedName) {
		return MiscKit
			.createUncollatedReferenceQualifier(f, qualifiedName)
			.toTypeReference();
	}

	/** Factory method that creates a new type reference derived by the
	name of the given type.
	@param f the program factory to be used.
	@param t the type which shall be referenced.
	@return a type reference to the given type; parent links of
	the reference are made valid.
	*/
	public static TypeReference createTypeReference(ProgramFactory f, Type t) {
		TypeReference result = null;

		if (t instanceof PrimitiveType) {
			result = f.createTypeReference(f.createIdentifier(t.getName()));
		} else if (t instanceof DeclaredType) {
			result = f.createTypeReference(f.createIdentifier(t.getName()));

			DeclaredTypeContainer ctc = ((ClassType) t).getContainer();
			if (ctc instanceof Namespace) {
				result.setReferencePrefix(
					NamespaceKit.createNamespaceReference(f, (Namespace) ctc));
			} else if (ctc instanceof ClassType) {
				result.setReferencePrefix(createTypeReference(f, (ClassType) ctc));
			}
		} else if (t instanceof ArrayType) {
			result = createTypeReference(f, ((ArrayType) t).getBaseType());

			// Dimensions need to be shifted
			int[] dims = result.getDimensions();
			int offset = dims == null ? 0 : dims.length;
			int[] newdims = new int[offset + 1];
			System.arraycopy(dims, 0, newdims, 0, offset);
			newdims[offset] = ((ArrayType) t).getDimension();
			result.setDimensions(newdims);

		}
		result.makeAllParentRolesValid();
		return result;
	}

	/** Factory method that creates a new type reference derived by
	the name of the given type and tries to minimize its
	qualification using the given context for the type
	reference. The context should describe the position the type
	reference will be inserted into, usually a statement
	container.
	@param si the source info to be used.
	@param t the type which shall be referenced.
	@param context a program element from which the type shall be addressed (may not be <CODE>null</CODE>).
	@return a minimal type reference to the given type; parent links of
	the reference are made valid.
	*/
	public static TypeReference createTypeReference(
		SourceInfo si,
		Type t,
		ProgramElement context) {
		TypeReference result = null;
		ProgramFactory f = context.getFactory();
		if (t instanceof PrimitiveType) {
			result = f.createTypeReference(f.createIdentifier(t.getName()));
		} else if (t instanceof ClassType) {
			result = f.createTypeReference(f.createIdentifier(t.getName()));
			DeclaredTypeContainer ctc = ((ClassType) t).getContainer();
			if (ctc != null && si.getType(t.getName(), context) != t) {
				if (ctc instanceof Namespace) {
					result.setReferencePrefix(
						NamespaceKit.createNamespaceReference(f, (Namespace) ctc));
				} else if (ctc instanceof ClassType) {
					result.setReferencePrefix(createTypeReference(f, (ClassType) ctc));
				}
			}
		} else if (t instanceof ArrayType) {
			result = createTypeReference(si, ((ArrayType) t).getBaseType(), context);

			// Dimensions need to be shifted
			int[] dims = result.getDimensions();
			int offset = dims == null ? 0 : dims.length;
			int[] newdims = new int[offset + 1];
			System.arraycopy(dims, 0, newdims, 0, offset);
			newdims[offset] = ((ArrayType) t).getDimension();
			result.setDimensions(newdims);
		}
		result.makeAllParentRolesValid();
		return result;
	}

	/** creates an abstract super class (interface) for the given class.
	@param    concrete public class to abstractify
	*/
	public static InterfaceDeclaration createAbstractSuperClass(
		NameInfo ni,
		ClassDeclaration cdecl,
		String abstractsupername)
		throws NameClashException {
		// assert that c is a public class and not an interface
		/* Problems may still occur with nested classes, especially
		   anonymous classes.
		*/
		String message =
			"Sorry, only public classes which are not interfaces can be transformed.";
		Debug.asserta(cdecl.isPublic() && !cdecl.isInterface(), message);

		if (ni.getType(abstractsupername) != null) {
			// problem: abstractsupername already is a type
			throw new NameClashException(
				"Error: Name " + abstractsupername + "is already declared.");
		}
		/* Iterate through members directly defined in the class cdecl.
		   - Put the signature of every non-static public method that does
		     not override an inherited method into a newly
		     created interface that then has to be implemented by
		     the class cdecl.
		   - Put every static final field with initalizers into the newly
		     created interface.
		   - Put every public interface and public class declarations of
		     cdecl into the newly created interface.
		*/
		ProgramFactory pf = cdecl.getFactory();

		MemberDeclarationMutableList imembers = new MemberDeclarationArrayList(1);
		MemberDeclarationList cmems = cdecl.getMembers();
		if (cmems != null) {
			for (int i = 0, s = cmems.size(); i < s; i++) {
				MemberDeclaration cmemd = cmems.getMemberDeclaration(i);

				if (!cmemd.isPublic()) {
					continue;
				}
				if (cmemd instanceof FieldDeclaration) {
					if (!((FieldDeclaration) cmemd).isSealed() || !cmemd.isStatic()) {
						continue;
					}
					FieldDeclaration d = (FieldDeclaration) cmemd.deepClone();

					VariableSpecificationMutableList vars =
						// Cast könnte kritisch sein!
	 (VariableSpecificationMutableList) d.getVariables();
					for (int j = 0, z = vars.size(); j < z; j++) {
						if (vars.getVariableSpecification(j).getInitializer() == null) {
							vars.remove(j);
							j -= 1;
							z -= 1;
						}
					}
					if (vars.size() > 0) {
						imembers.add(d);
					}
				} else if (cmemd instanceof MethodDeclaration) {
					MethodDeclaration md = (MethodDeclaration) cmemd;

					if (!md.isStatic()
						&& md.isPublic()
						&& !(md
							instanceof ConstructorDeclaration) //!!!!!!!!!!!!!!!!!! Die folgende Methode gibt es noch nicht !!!!!!!!!!!!!
					// && !cdecl.overridesInherited(md)
					) {
						imembers.add(MethodKit.createAbstractMethodDeclaration(md, true));
					} else
						continue;
				} else if (cmemd instanceof TypeDeclaration) {
					imembers.add((TypeDeclaration) cmemd.deepClone());
				} else
					continue;
			}
			if (!imembers.isEmpty()) {
				Identifier iid = pf.createIdentifier(abstractsupername);

				// Copy class modifiers into newly created interface
				Modifier vis = cdecl.getVisibilityModifier();
				ModifierMutableList imods = null;
				if (vis != null) {
					imods = new ModifierArrayList(1);
					imods.add((Modifier) vis.deepClone());
				}
					InterfaceDeclaration idecl =
						pf.createInterfaceDeclaration(imods, // modifiers
		iid, // name of the new interface
		null, // the interface does not extend others
	imembers); // the extracted field and method declarations

				// !!!!!!!!!!!!! Folgenden Teil zur Modifikation von cdecl besser in eine eigene kit-Methode verschieben, damit ohne Seiteneffekte gearbeitet werden kann. Ausserdem wird das erweitern von implements- und extends-Listen sicher haeufiger benoetigt. !!!!!!!!!!!!!!!!!!!!!!!!

				// extend "extends list" of cdecl by idecl
				TypeReferenceMutableList itypes = new TypeReferenceArrayList(1);
				TypeReference iref = pf.createTypeReference(iid);

				Implements impl = cdecl.getImplementedTypes();
				if (impl == null) {
					impl = new Implements(iref);
				} else {
					itypes = impl.getSupertypes();
					itypes.add(iref);
					impl.setSupertypes(itypes);
				}
				cdecl.setImplementedTypes(impl);

				return idecl;
			} else {
				// System.out.println("Sorry, no members of "+cdecl.getName() +" can be abstractified!\n");
				return null;
			}
		} else {
			// System.out.println("Sorry, the class "+cdecl.getName()+" contains no members!\n");
			return null;
		}
	}

	/**
	   Create an interface from a class declaration. This is done by omission
	   of all elements that cannot be transformed.
	   The following members are kept: 
	   <UL>
	   <LI>Public FieldDeclarations that are final and static
	       and are initialized (in a list of specifications, 
	       only the variables that have initializers are kept), 
	   <LI>public MethodDeclarations that are no ConstructorDeclarations, 
	       and <EM>not</EM> static,
	   <LI>public InterfaceDeclarations and ClassDeclarations (weird, but
	       admissible).
	   </UL>
	   The interface does not extend any of the interfaces implemented by
	   the class declaration by default. The name of the returned declaration
	   corresponds to "Abstract" + decl.getName(), which can be changed
	   afterwards.
	 */
	public static InterfaceDeclaration createInterfaceDeclaration(ClassDeclaration decl) {
		ProgramFactory factory = decl.getFactory();
		InterfaceDeclaration res = factory.createInterfaceDeclaration();
		res.setIdentifier(factory.createIdentifier("Abstract" + decl.getName()));
		Modifier vis = decl.getVisibilityModifier();
		if (vis != null) {
			ModifierMutableList imods = new ModifierArrayList(1);
			imods.add((Modifier) vis.deepClone());
			res.setModifiers(imods);
		}
		MemberDeclarationMutableList imembers = new MemberDeclarationArrayList();
		res.setMembers(imembers);
		MemberDeclarationList cmems = decl.getMembers();
		if (cmems == null) {
			return res;
		}
		for (int i = 0, s = cmems.size(); i < s; i += 1) {
			MemberDeclaration cmemd = cmems.getMemberDeclaration(i);
			if (!cmemd.isPublic()) {
				continue;
			}
			if (cmemd instanceof FieldDeclaration) {
				if (!((FieldDeclaration) cmemd).isSealed() || !cmemd.isStatic()) {
					continue;
				}
				FieldDeclaration d = (FieldDeclaration) cmemd.deepClone();
				VariableSpecificationMutableList vars =
					//!!!!!!! Cast könnte kritisch sein!
	 (VariableSpecificationMutableList) d.getVariables();
				for (int j = 0, z = vars.size(); j < z; j += 1) {
					if (vars.getVariableSpecification(j).getInitializer() == null) {
						vars.remove(j);
						j -= 1;
						z -= 1;
					}
				}
				if (vars.size() > 0) {
					imembers.add(d);
				}
			} else if (cmemd instanceof MethodDeclaration) {
				if (cmemd instanceof ConstructorDeclaration || cmemd.isStatic()) {
					continue;
				}
				imembers.add(
					MethodKit.createAbstractMethodDeclaration(
						(MethodDeclaration) cmemd,
						true));
			} else if (cmemd instanceof TypeDeclaration) {
				imembers.add((TypeDeclaration) cmemd.deepClone());
			}
		}
		return res;
	}

	/**
	   Create a simple adapter class for a class declaration. If the class is
	   <p> class c { 
	   <p> m(int i, int i2) { ..}
	   <p> m2(int i, int i2) { ..}
	   <p> }
	   <p> the created class is
	   <p> class cAdapter { 
	   <p> m(int i, int i2) {  delegatingObject.m(i,i2); }
	   <p> m2(int i, int i2) { delegatingObject.m2(i,i2); }
	   <p> }
	   @deprecated needs rework
	*/
	public static ClassDeclaration createAdapterClass(
		String adapterName,
		ClassDeclaration classDecl) {
		ProgramFactory factory = classDecl.getFactory();
		ReferencePrefix delegationObject =
			new FieldReference(
				factory.createIdentifier("delegationObject" + classDecl.getName()));
		ClassDeclaration adapterClass =
			factory.createClassDeclaration(
				new ModifierArrayList(),
				factory.createIdentifier(adapterName),
				factory.createExtends(),
				factory.createImplements(),
				new MemberDeclarationArrayList());

		// Create an adapter interface with delegating methods
		for (int i2 = 0; i2 < classDecl.getMembers().size(); i2++) {
			MemberDeclaration member = classDecl.getMembers().getMemberDeclaration(i2);
			if (member instanceof MethodDeclaration) {
				MethodDeclaration method = (MethodDeclaration) member;
				if (method.isPublic()) {
					Debug.info(2, "adapting public method " + method.getName());
					MethodDeclaration clone =
						MethodKit.createAdapterMethod(delegationObject, method);
					if (clone != null)
						adapterClass.getMembers().add(clone);
				}
			}
		}
		return adapterClass;
	}

// DISABLED: Was deprecated...

//	/**
//	   Transformation that renames a type declaration and all known references
//	   to that type. The new name should not hide another type in the
//	   declaration context.
//	   @param ch the change history (may be <CODE>null</CODE>).
//	   @param xr the cross referencer service.
//	   @param ni the name info service.
//	   @param type the type declaration to be renamed;
//	   may not be <CODE>null</CODE>.
//	   @param newName the new name for the element;
//	   may not be <CODE>null</CODE> and must denote a valid identifier name.
//	   @return <CODE>true</CODE>, if a rename has been necessary,
//	   <CODE>false</CODE> otherwise.
//	   @deprecated replaced by recoder.kit.transformation.RenameType
//	 */
//	public static boolean rename(
//		ChangeHistory ch,
//		CrossReferenceSourceInfo xr,
//		NameInfo ni,
//		ClassTypeDeclaration type,
//		String newName) {
//		Debug.assert(xr, ni, type, newName);
//		Debug.assert(type.getName());
//		if (!newName.equals(type.getName())) {
//			TypeReferenceMutableList refs = new TypeReferenceArrayList();
//			refs.add(xr.getReferences(type));
//			ConstructorList cons = type.getConstructors();
//			Type atype = ni.getArrayType(type);
//			while (atype != null) {
//				refs.add(xr.getReferences(atype));
//				atype = ni.getArrayType(atype);
//			}
//			MiscKit.rename(ch, type, newName);
//			if (cons != null) {
//				for (int i = cons.size() - 1; i >= 0; i -= 1) {
//					Constructor con = cons.getConstructor(i);
//					if (con instanceof ConstructorDeclaration) {
//						MiscKit.rename(ch, (ConstructorDeclaration) con, newName);
//					}
//					// no need to rename all references to the constructors
//					// as these are either nameless (this/super) or
//					// contain a type reference
//				}
//			}
//			if (refs != null) {
//				for (int i = refs.size() - 1; i >= 0; i -= 1) {
//					MiscKit.rename(ch, refs.getTypeReference(i), newName);
//				}
//			}
//			return true;
//		}
//		return false;
//	}

	/**
	   @deprecated still untested.
	 */
	public static TypeReferenceList getInfluencedReferences(
		CrossReferenceSourceInfo xr,
		String newTypeName,
		NonTerminalProgramElement context) {
		Debug.asserta(xr, newTypeName, context);
		// check from the point of view of a scope defining element
		context = MiscKit.getScopeDefiningElement(context);
		Type t = xr.getType(newTypeName, context);
		if (t == null) {
			// the type is new, hence there are no references
			return TypeReferenceList.EMPTY_LIST;
		}
		TypeReferenceList list = xr.getReferences(t);
		if (list.isEmpty()) {
			return list;
		}
		// a new type is only visible in its scope
		// all references from outside do not change
		TypeReferenceMutableList result = new TypeReferenceArrayList();
		for (int i = list.size() - 1; i >= 0; i -= 1) {
			TypeReference tr = list.getTypeReference(i);
			if (MiscKit.contains(context, tr)) {
				result.add(tr);
			}
		}
		return result;
	}

	/**
	   Query that retrieves all references to a given type that are contained
	   within the given tree. The specified flag defines the strategy to use:
	   either the cross reference information is filtered, or the cross
	   reference information is collected from the tree. The filtering
	   mode is faster if the tree contains more nodes than there are global 
	   references to the given type.
	   @param xr the cross referencer to use.
	   @param t a type.
	   @param root the root of an arbitrary syntax tree.
	   @param scanTree flag indicating the search strategy; if 
	   <CODE>true</CODE>, local cross reference information is build,
	   otherwise the global cross reference information is filtered.
	   @return the list of references to the given type in the given tree,
	   can be empty but not <CODE>null</CODE>.
	 */
	public static TypeReferenceMutableList getReferences(
		CrossReferenceSourceInfo xr,
		Type t,
		NonTerminalProgramElement root,
		boolean scanTree) {
		Debug.asserta(xr, t, root);
		TypeReferenceMutableList result = new TypeReferenceArrayList();
		if (scanTree) {
			TreeWalker tw = new TreeWalker(root);
			while (tw.next(TypeReference.class)) {
				TypeReference tr = (TypeReference) tw.getProgramElement();
				if (xr.getType(tr) == t) {
					result.add(tr);
				}
			}
		} else {
			TypeReferenceList refs = xr.getReferences(t);
			for (int i = 0, s = refs.size(); i < s; i += 1) {
				TypeReference tr = refs.getTypeReference(i);
				if (MiscKit.contains(root, tr)) {
					result.add(tr);
				}
			}
		}
		return result;
	}

	/**
	   Query that collects all members of a class type, a method, or a 
	   package. For a package, this includes all class types of that package,
	   for a class type, this includes all defined constructors, fields, 
	   methods, and inner types, and for a method, this includes all
	   inner types.
	   @param ct the class type to collect members from.
	   @return a mutable list of all members of the given class type.
	 */
	public static MemberMutableList getMembers(DeclaredTypeContainer ctc) {
		MemberMutableList result = new MemberArrayList();
		MemberList mlist;
		if (ctc instanceof ClassType) {
			ClassType ct = (ClassType) ctc;
			mlist = ct.getConstructors();
			if (mlist != null) {
				result.add(mlist);
			}
			mlist = ct.getFields();
			if (mlist != null) {
				result.add(mlist);
			}
			mlist = ct.getMethods();
			if (mlist != null) {
				result.add(mlist);
			}
		}
		mlist = ctc.getDeclaredTypes();
		if (mlist != null) {
			result.add(mlist);
		}
		return result;
	}

	/** Query returing the super class of the given class type.
	If the class type is an interface or has no explicit extended type,
	java.lang.Object will be reported (also for java.lang.Object itself).
	@param ni the name info service to use.
	@param ct the class type to get the super class from.
	@return the super class.
	*/
	public static ClassType getSuperClass(NameInfo ni, ClassType ct) {
		if (!ct.isInterface()) {
			ClassTypeList ctl = ct.getSupertypes();
			for (int i = 0; i < ctl.size(); i += 1) {
				ct = ctl.getClassType(i);
				if (!ct.isInterface()) {
					return ct;
				}
			}
		}
		return ni.getSystemObject();
	}

	/**
	   Query comparing the visibility of two members.
	   @param x the first member.
	   @param y the second member.
	   @return <CODE>true</CODE> if the first member is less visible than
	   the second (in the order "private" - "" (package) - "protected" - "public"
	   where applicable), <CODE>false</CODE> if it is as least as visible.
	*/
	public static boolean isLessVisible(Member x, Member y) {
		if (x.isPublic()) {
			return false;
		}
		if (y.isPublic()) {
			return true;
		}
		if (x.isProtected()) {
			return false;
		}
		if (y.isProtected()) {
			return true;
		}
		return x.isPrivate() && !y.isPrivate();
	}

	/**
	   Checks if for each class type in the first type list there is a super
	   type in the second. This is useful to check if a exception lists
	   is less or equally strict that the other.
	   @param tsi the type system info to use.
	   @param x a class list, may not be <CODE>null</CODE>.
	   @param y a class list, may not be <CODE>null</CODE>.
	   @return <CODE>true</CODE> if the first list of class types is
	   covered by the second one, <CODE>false</CODE> otherwise.
	*/
	public static boolean isCovered(
		ProgramModelInfo pmi,
		ClassTypeList x,
		ClassTypeList y) {
		Debug.asserta(x, y);
		boolean found = true;
		for (int i = x.size() - 1;(i >= 0) && found; i -= 1) {
			ClassType ct = x.getClassType(i);
			found = false;
			for (int j = y.size() - 1; j >= 0; j -= 1) {
				if (pmi.isSubtype(ct, y.getClassType(j))) {
					found = true;
					break;
				}
			}
		}
		return found;
	}

	/**
	   Query that checks if the given member is admissible within an
	   interface.  Admissible are public final static
	   completely-initialized fields, public non-static
	   non-constructor body-less methods, and public interface and
	   class declarations.  This method does not criticize the use of
	   redundant modifiers such as abstract or static.
	   @param member a potential interface member.
	   @return <CODE>true</CODE> if the given member could become a
	   member of an interface, <CODE>false</CODE> otherwise.
	*/
	public static boolean isValidInterfaceMember(MemberDeclaration member) {
		if (!member.isPublic()) {
			return false;
		}
		if (member instanceof FieldDeclaration) {
			if (!member.isStatic() || !((FieldDeclaration) member).isSealed()) {
				return false;
			}
			VariableSpecificationList vars = ((FieldDeclaration) member).getVariables();
			for (int j = 0, z = vars.size(); j < z; j += 1) {
				if (vars.getVariableSpecification(j).getInitializer() == null) {
					return false;
				}
			}
			return true;
		}
		if (member instanceof MethodDeclaration) {
			return !(member instanceof ConstructorDeclaration)
				&& !member.isStatic()
				&& ((MethodDeclaration) member).getBody() == null;
		}
		if (member instanceof TypeDeclaration) {
			return true;
		}
		return false;
	}

	/**
	   Gets all types in the given list that are subtypes of other types in 
	   the list.
	   @param pmi the program model info service to use.
	   @param list a list of class types.
	   @return a list of class types that have supertypes in the list.
	   @see #removeCoveredSubtypes
	   @since 0.71
	*/
	public static ClassTypeList getCoveredSubtypes(
		ProgramModelInfo pmi,
		ClassTypeList list) {
		ClassTypeMutableList copy = new ClassTypeArrayList();
		copy.add(list);
		return removeCoveredSubtypes(pmi, copy);
	}

	/**
	   Removes types in the given list that are subtypes of other types in 
	   the list and returns the removed types in a new list. 
	   @param pmi the program model info service to use.
	   @param list a mutable list of class types.
	   @return a set of class types that have supertypes in the list
	   and that have been removed.
	   @since 0.71
	   @see #getCoveredSubtypes
	*/
	public static ClassTypeMutableList removeCoveredSubtypes(
		ProgramModelInfo pmi,
		ClassTypeMutableList list) {
		ClassTypeMutableList removed = new ClassTypeArrayList();
		for (int i = list.size() - 1; i >= 0; i -= 1) {
			ClassType ct = list.getClassType(i);
			for (int j = list.size() - 1; j >= 0; j -= 1) {
				if (j != i) {
					ClassType ct2 = list.getClassType(j);
					if (pmi.isSubtype(ct, ct2)) {
						removed.add(ct);
						list.remove(i);
						break;
					}
				}
			}
		}
		return removed;
	}

	/**
	   Query that returns all redundant super interfaces of the specified
	   type declaration. This does not cover redundant extensions of
	   <CODE>java.lang.Object</CODE>, but does cover double extensions
	   or implementations of interfaces as well as transitive interface
	   inheritance.
	   @param si the source info service to use.
	   @param td the type declaration to check for inheritance redundancy.
	
	   @since 0.71
	 */
	public static TypeReferenceList getRedundantSuperInterfaces(
		SourceInfo si,
		TypeDeclaration td) {
		// get all super interface references
		ClassType superclass = null;
		TypeReferenceList superinterfaces = TypeReferenceList.EMPTY_LIST;
		if (td instanceof InterfaceDeclaration) {
			InterfaceDeclaration id = (InterfaceDeclaration) td;
			if (id.getExtendedTypes() != null) {
				superinterfaces = id.getExtendedTypes().getSupertypes();
			}
		} else {
			ClassDeclaration cd = (ClassDeclaration) td;
			if (cd.getImplementedTypes() != null) {
				superinterfaces = cd.getImplementedTypes().getSupertypes();
			}
			if (cd.getExtendedTypes() != null) {
				superclass =
					(ClassType) si.getType(
						cd.getExtendedTypes().getSupertypes().getTypeReference(0));
			}
		}

		TypeReferenceMutableList redundantReferences = new TypeReferenceArrayList();
		ClassTypeMutableList types = new ClassTypeArrayList();
		for (int i = 0; i < superinterfaces.size(); i += 1) {
			TypeReference tr = superinterfaces.getTypeReference(i);
			types.add((ClassType) si.getType(tr));
		}
		for (int i = superinterfaces.size() - 1; i >= 0; i -= 1) {
			TypeReference tr = superinterfaces.getTypeReference(i);
			ClassType ct = types.getClassType(i);
			if (superclass != null) {
				if (si.isSubtype(superclass, ct)) {
					redundantReferences.add(tr);
					continue;
				}
			}
			for (int j = superinterfaces.size() - 1; j >= 0; j -= 1) {
				if (i != j) {
					ClassType st = types.getClassType(j);
					if (si.isSubtype(st, ct)) {
						redundantReferences.add(tr);
						break;
					}
				}
			}
		}
		return redundantReferences;
	}

	/**
	   Query that returns all redundant exceptions of the specified
	   throws clause. This does not cover redundant extensions of
	   <CODE>java.lang.RuntimeException</CODE> or subclasses, but does cover 
	   double declarations as well as transitive exceptions.
	   @param si the source info service to use.
	   @param t the throws clause to check for redundancy.
	
	   @since 0.71
	 */
	public static TypeReferenceList getRedundantExceptions(SourceInfo si, Throws t) {
		TypeReferenceList exceptions = t.getExceptions();
		TypeReferenceMutableList redundantReferences = new TypeReferenceArrayList();
		ClassTypeMutableList types = new ClassTypeArrayList(exceptions.size());
		for (int i = 0; i < exceptions.size(); i += 1) {
			types.add((ClassType) si.getType(exceptions.getTypeReference(i)));
		}
		for (int i = exceptions.size() - 1; i >= 0; i -= 1) {
			ClassType ct = types.getClassType(i);
			for (int j = exceptions.size() - 1; j >= 0; j -= 1) {
				if (i != j) {
					ClassType st = types.getClassType(j);
					if (si.isSubtype(ct, st)) {
						redundantReferences.add(exceptions.getTypeReference(i));
						break;
					}
				}
			}
		}
		return redundantReferences;
	}

}
