// This file is part of the RECODER library and protected by the LGPL.

package recodercs.kit;

import recodercs.convenience.Naming;
import recodercs.service.ChangeHistory;
import recodercs.service.CrossReferenceSourceInfo;
import recodercs.util.Debug;
import recodercs.util.Order;
import recodercs.*;
import recodercs.abstraction.*;
import recodercs.convenience.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;
import recodercs.csharp.expression.*;
import recodercs.csharp.expression.operator.*;
import recodercs.csharp.reference.*;
import recodercs.csharp.statement.*;
import recodercs.list.*;
import recodercs.service.*;
import recodercs.util.*;

import java.util.Enumeration;

/** This class provides utilities for manipulating compilation units.
 *  Now, it provides some tools for the using declarations, but these
 *  are not really well applicable for C# programs, since there the
 *  import structure is different (e.g. there can be imports in the
 *  namespace declarations too).
 * 
 *  THIS CLASS IS INCOMPLETE AND SHALL IS NOT MEANT FOR A REAL USE.
 * 
 * Until it is rewritten, use with care.
 * 
 */
public class UnitKit {

	private UnitKit() {}

	private final static boolean DEBUG = true;

	/**
	   Query that returns the compilation unit which the given program
	   element is located in. Returns <CODE>null</CODE>, if the
	   element is <CODE>null</CODE> or not a part of a compilation
	   unit. If the element is already a compilation unit, the element
	   is returned.
	   @param p a program element.
	   @return the compilation unit of the given element, or <CODE>null</CODE>.  */
	public static CompilationUnit getCompilationUnit(ProgramElement p) {
		while (p != null) {
			if (p instanceof CompilationUnit) {
				return (CompilationUnit) p;
			}
			p = p.getASTParent();
		}
		return null;
	}

	// creates an empty compilation unit in the package of "brother"
	//CompilationUnit createCompilationUnit(CompilationUnit brother)

	// DISABLED, since C# has no support for direct imports this way.
	// 
	//	private static ClassType getNecessaryImportedType(
	//		CrossReferenceSourceInfo xi,
	//		Using imp) 
	//	{
	//		if (imp.isMultiImport()) {
	//			return null;
	//		}
	//		
	//		TypeReference tr = imp.getTypeReference();
	//		ClassType ct = (ClassType) xi.getType(tr);
	//		if (ct == null) {
	//			throw new RuntimeException(
	//				"No type found for " + Format.toString(Formats.ELEMENT_LONG, tr));
	//		}
	//		// there must be at least one reference to this import
	//		if (TypeKit.getReferences(xi, ct, imp.getASTParent(), false).size() > 1) {
	//			return ct;
	//		} else {
	//			return null;
	//		}
	//	}

	/** Returns true, if the using is necessary, false otherwise.
	 */
	private static boolean isNecessaryUsing(
		CrossReferenceSourceInfo xrsi,
		Using imp,
		Set coveredTypes) {
		//  FIXME: rework method

		// TODO: Write this method, after we have completed the cross reference
		// source 

		//	if (!imp.isMultiImport()) {
		//	    return false;
		//	}
		//	TypeReferenceInfix ref = imp.getReference();
		//	CompilationUnit cu = imp.getParent();
		//	ClassTypeList types;
		//	if (ref instanceof NamespaceReference) {
		//	    types = xrsi.getPackage((NamespaceReference)ref).getTypes();
		//	} else {
		//	    types = ((ClassType)xrsi.getType((TypeReference)ref)).getTypes();
		//	}
		//	boolean result = false;
		//	for (int j = types.size() - 1; j >= 0 && !result; j -= 1) {
		//	    ClassType ct = types.getClassType(j);		    
		//	    if (!coveredTypes.contains(ct)) {
		//		TypeReferenceMutableList refs = 
		//		    TypeKit.getReferences(xrsi, ct, cu, false);
		//		for (int k = refs.size() - 1; k >= 0; k -= 1) {
		//		    if (refs.getTypeReference(k).getASTParent().getASTParent() == cu) {
		//			refs.remove(k);
		//		    }
		//		}
		//		result = !refs.isEmpty();
		//	    }
		//	}
		//	return result;
		return true;
	}

	/**
	   Query that finds all unnecessary import specifications in
	   a compilation unit. Single type usings are considered unnecessary if 
	   no reference to the imported type occurs in the unit.  There might be
	   fully qualified references only, such that the import would be unused
	   in fact, but this does not seem worthwhile to check.
	   A wildcarded import is considered unused if the only types with the
	   given prefixes are imported by corresponding single type usings.
	   @param xrsi the cross reference source info to use.
	   @param cu the compilation unit to find unnecessary usings in.
	   @return a list of unnecessary usings in the unit.
	 */
	public static UsingList getUnnecessaryUsings(
		CrossReferenceSourceInfo xrsi,
		CompilationUnit cu) {

		Debug.asserta(xrsi, cu);
		UsingList il = cu.getUsings();

		if (il == null || il.isEmpty()) {
			return UsingList.EMPTY_LIST;
		}

		UsingMutableList removalList = new UsingArrayList();
		MutableSet coveredTypes = new NaturalHashSet();

		for (int i = 0, s = il.size(); i < s; i += 1) {
			Using imp = il.getUsing(i);
			if (imp.isMultiImport() && !isNecessaryUsing(xrsi, imp, coveredTypes)) {
				removalList.add(imp);
			}
		}

		return removalList;
	}

	// DISABLED: This operation shall really become a transformation.
	// 
	//	/**
	//	   @deprecated should become a first class transformation.
	//	 */
	//	public static void removeUnusedImports(
	//		ChangeHistory ch,
	//		CrossReferenceSourceInfo xrsi,
	//		CompilationUnit cu) {
	//		Debug.assert(ch);
	//		UsingList removalList = getUnnecessaryUsings(xrsi, cu);
	//		for (int i = removalList.size() - 1; i >= 0; i -= 1) {
	//			MiscKit.remove(ch, removalList.getUsing(i));
	//		}
	//	}

	// DISABLED: 
	//	/**
	//	   @deprecated should become a fully grown transformation.
	//	 */
	//	// checks all type references in the types of the given unit
	//	// that refer to external class types and ensures that these
	//	// are imported directly. All multi type usings are deleted,
	//	// as are all single type usings that are not necessary.
	//	public static void normalizeImports(
	//		ChangeHistory ch,
	//		CrossReferenceSourceInfo xrsi,
	//		CompilationUnit cu,
	//		boolean removeMultiTypeImports,
	//		boolean removeSingleTypeImports,
	//		boolean addJavaLangImports,
	//		boolean addDefaultPackageImports) {
	//		Debug.assert(xrsi, cu);
	//		// first step: collect all external class types referred to in the unit
	//		MutableSet importTypes = new NaturalHashSet();
	//		Namespace unitPackage = cu.getPrimaryTypeDeclaration().getNamespace();
	//		TreeWalker tw = new TreeWalker(cu);
	//		// skip usings and package spec subtrees
	//		for (int i = cu.getTypeDeclarationCount() - 1; i >= 0; i -= 1) {
	//			tw.reset(cu.getTypeDeclarationAt(i));
	//			while (tw.next(TypeReference.class)) {
	//				TypeReference tr = (TypeReference) tw.getProgramElement();
	//				Type type = xrsi.getType(tr);
	//				while (type instanceof ArrayType) {
	//					type = ((ArrayType) type).getBaseType();
	//				}
	//				if ((type instanceof ClassType)
	//					&& !((type instanceof TypeDeclaration)
	//						&& MiscKit.contains(cu, (TypeDeclaration) type))
	//					&& (addDefaultPackageImports
	//						|| ((ClassType) type).getNamespace() != unitPackage)
	//					&& (addJavaLangImports
	//						|| !type.getFullName().startsWith("java.lang."))) {
	//					importTypes.add(type);
	//				}
	//			}
	//		}
	//
	//		UsingList il = cu.getUsings();
	//		int ilsize = (il == null) ? 0 : il.size();
	//
	//		// now collect all class types that are already imported
	//		ClassType[] classTypes = new ClassType[ilsize];
	//		MutableSet importedTypes = new NaturalHashSet();
	//		for (int i = ilsize - 1; i >= 0; i -= 1) {
	//			Using imp = il.getUsing(i);
	//			if (!imp.isMultiImport()) {
	//				TypeReference tr = imp.getTypeReference();
	//				ClassType ct = (ClassType) xrsi.getType(tr);
	//				classTypes[i] = ct;
	//				importedTypes.add(ct);
	//			}
	//		}
	//
	//		MutableSet commonTypes = new NaturalHashSet(importTypes.size());
	//		commonTypes.add(importTypes);
	//		commonTypes.intersect(importedTypes);
	//
	//		// compute the types that must be imported additionally
	//		importTypes.subtract(commonTypes);
	//
	//		// now find the types that do no longer have to be imported
	//		importedTypes.subtract(commonTypes);
	//
	//		// now, remove the no longer used usings including all multi usings
	//		for (int i = ilsize - 1; i >= 0; i -= 1) {
	//			Using imp = il.getUsing(i);
	//			if ((imp.isMultiImport() && removeMultiTypeImports)
	//				|| (!imp.isMultiImport()
	//					&& removeSingleTypeImports
	//					&& importedTypes.contains(classTypes[i]))) {
	//				MiscKit.remove(ch, imp);
	//			}
	//		}
	//
	//		// finally, create required single type usings
	//		Enumeration enum = importTypes.elements();
	//		while (enum.hasMoreElements()) {
	//			ClassType ct = (ClassType) enum.nextElement();
	//			appendImport(ch, cu, ct);
	//		}
	//	}

	// DISABLED, since C# does not support importing a class type directly.
	//	/**
	//	   Transformation that appends an import specification for the given class
	//	   type. This method does not check whether the import is needed or 
	//	   redundant.
	//	   @param ch the change history to notify (may be <CODE>null</CODE>).
	//	   @param cu the unit to create the import for.
	//	   @param ct the class type to create the import for.
	//	   @return the new import.
	//	 */
	//	public static Using appendImport(
	//		ChangeHistory ch,
	//		CompilationUnit cu,
	//		ClassType ct) {
	//		return appendImport(ch, cu, ct.getFullName());
	//	}

	// DISABLED, since C# has no direct imports.
	//
	//	/**
	//	   Transformation that appends an import specification for the given class
	//	   type. This method does not check whether the import is needed or 
	//	   redundant.
	//	   @param ch the change history to notify (may be <CODE>null</CODE>).
	//	   @param cu the unit to create the import for.
	//	   @param typeName the class type name to create the import for.
	//	   @return the new import.
	//	   @deprecated should become a fully grown transformation.
	//	 */
	//	public static Using appendImport(
	//		ChangeHistory ch,
	//		CompilationUnit cu,
	//		String typeName) 
	//	{
	//		Debug.assert(cu, typeName);
	//		ProgramFactory factory = cu.getFactory();
	//		TypeReference ref = TypeKit.createTypeReference(factory, typeName);
	//		Using newImport = factory.createUsing(ref, false);
	//		newImport.makeAllParentRolesValid();
	//		MiscKit.append(ch, cu, newImport);
	//		
	//		return newImport;
	//	}

// THESE FUNCTIONS HAVE ALSO BEEN DISABLED FOR NOW.
// They may be supported later, but in this form they are not usable due to the
// intelligent idea in C# to disable single type imports.

// TODO: Write methods similar to these

//	/**
//	   Transformation that ensures that the given class type is known at
//	   the given location by importing it on demand. If the type is already
//	   known, <CODE>null</CODE> is returned, otherwise the new import
//	   specification that usings the type directly.
//	   @param ch the change history to report to (may be <CODE>null</CODE>).
//	   @param si the source info service.
//	   @param typeName the fully qualified name of the type to be known at the
//	   unit level.
//	   @param context the context in which the type should be known.
//	   @return a new import specification as added to the compilation unit,
//	   or <CODE>null</CODE> if no new import was needed.
//	   @deprecated needs further testing - use at your own risks
//	 */
//	public static Using ensureImport(
//		ChangeHistory ch,
//		SourceInfo si,
//		String typeName,
//		ProgramElement context) {
//			
//		Debug.assert(si, typeName, context);
//		Debug.assert(typeName.length() > 0);
//		
//		if (si.getType(typeName, context) != null) {
//			return null;
//		}
//		
//		return appendImport(ch, MiscKit.getParentCompilationUnit(context), typeName);
//	}
//
//	/**
//	   Transformation that ensures that all type references in the given
//	   subtree are resolvable by importing the corresponding types on demand.
//	   @param ch the change history to report to (may be <CODE>null</CODE>).
//	   @param si the source info service.
//	   @param root the root element in a subtree containing type references
//	   to check.
//	   @deprecated needs further testing - use at your own risks
//	 */
//	public static void ensureImports(
//		ChangeHistory ch,
//		SourceInfo si,
//		ProgramElement root) {
//		Debug.assert(si, root);
//		CompilationUnit cu = MiscKit.getParentCompilationUnit(root);
//		TreeWalker tw = new TreeWalker(root);
//		while (tw.next()) {
//			ProgramElement pe = tw.getProgramElement();
//			if (pe instanceof TypeReference) {
//				String name = Naming.toPathName((TypeReference) pe);
//				while (name.endsWith("]")) {
//					name = name.substring(0, name.length() - 2);
//				}
//				Type type = si.getType(name, pe);
//				if (type == null) {
//					ensureImport(ch, si, name, cu);
//				}
//			}
//		}
//	}

	// sort usings lexically and insert one blank line between different
	// top level names
	// !!!!!!!!!!!!!!!!!!!! untested !!!!!!!!!!!!!!!
	public static void sortUsings(ChangeHistory ch, CompilationUnit cu) {
		Debug.asserta(cu);
		UsingMutableList il = cu.getUsings();
		if (il == null) {
			return;
		}
		final String[] names = new String[il.size()];
		for (int i = 0; i < il.size(); i += 1) {
			Using imp = il.getUsing(i);
			names[i] = Naming.toPathName(imp.getReference());
		}
		for (int i = 1; i < names.length; i += 1) {
			String x = names[i];
			int j = i - 1;
			while (j >= 0 && Order.LEXICAL.greater(names[j], x)) {
				names[j + 1] = names[j];
				j -= 1;
			}
			names[j + 1] = x;
			if (j + 1 != i) {
				Using oldImp = il.getUsing(i);
				il.remove(i);
				il.insert(j + 1, oldImp);
				if (ch != null) {
					ch.detached(oldImp, cu, i);
					ch.attached(oldImp);
				}
			}
		}
		String prefix = null;
		for (int i = 0; i < names.length; i += 1) {
			String name = names[i];
			int dot = name.indexOf('.');
			String newPrefix = (dot >= 0) ? name.substring(0, dot) : name;
			if (i > 0 && !prefix.equals(newPrefix)) {
				Using imp = il.getUsing(i);
				SourceElement.Position pos = imp.getFirstElement().getRelativePosition();
				if (pos.getLine() == 0) {
					pos.setLine(1);
					imp.getFirstElement().setRelativePosition(pos);
				}
			}
			prefix = newPrefix;
		}
	}
}
