// This file is part of the RECODER library and protected by the LGPL

package recoder.kit;

import recoder.ProgramFactory;
import recoder.abstraction.DeclaredType;
import recoder.abstraction.Namespace;
import recoder.csharp.declaration.TypeDeclaration;
import recoder.csharp.reference.NamespaceReference;
import recoder.list.ClassTypeList;
import recoder.list.DeclaredTypeArrayList;
import recoder.list.DeclaredTypeList;
import recoder.list.DeclaredTypeMutableList;
import recoder.list.NamespaceReferenceList;
import recoder.service.ChangeHistory;
import recoder.service.CrossReferenceSourceInfo;
import recoder.util.Debug;

/** this class implements basic functions for package handling.
    @author Uwe Assmann
    @author Andreas Ludwig
    @author Rainer Neumann
    
    THIS CLASS HAS BEEN REVISED FOR C#.
    
*/
public class NamespaceKit {

	private NamespaceKit() {}

	/**
	   Creates a new package reference derived by the name of the given 
	   package.
	   @param f the program factory to be used.
	   @param p the package which shall be referenced.
	*/
	public static NamespaceReference createNamespaceReference(
		ProgramFactory f,
		Namespace p) {
		NamespaceReference result = null;
		String name = p.getFullName();
		int i, j = -1;
		do {
			i = j + 1;
			j = name.indexOf(".", i);
			String token = (j > i) ? name.substring(i, j) : name.substring(i);
			result = f.createNamespaceReference(result, f.createIdentifier(token));
			// null is admissible as prefix
		} while (j > i);
		return result;
	}


// DISABLED: Has no sense in C# - each package must be in source
//
//	/**
//	   Query that collects all types in a namespace that are not
//	   available as sources. 
//	   
//	   <b>THIS METHOD HAS NOT YET SENSE IN RECODER-C#, since all types
//	   must be available in source code form.</b>
//	   
//	   @param pkg the package to check for non-source types.
//	   @return a list of class types of the given package that are no
//	   {@link recoder.csharp.declaration.TypeDeclaration}s.
//	 */
//	public static DeclaredTypeList getNonSourcePackageTypes(Namespace pkg) {
//		DeclaredTypeMutableList result = new DeclaredTypeArrayList();
//		DeclaredTypeList classes = pkg.getDeclaredTypes();
//		for (int i = classes.size() - 1; i >= 0; i -= 1) {
//			DeclaredType ct = classes.getDeclaredType(i);
//			if (!(ct instanceof TypeDeclaration)) {
//				result.add(ct);
//			}
//		}
//		return result;
//	}

// DISABLED: WAS DEPRECATED
//
//	/**
//	   Transformation that renames all known references to a namespace.
//	   @param ch the change history (may be <CODE>null</CODE>).
//	   @param xr the cross referencer service.
//	   @param pkg the package to be renamed;
//	   may not be <CODE>null</CODE>.
//	   @param newName the new name for the package;
//	   may not be <CODE>null</CODE> and must denote a valid identifier name.
//	   @return <CODE>true</CODE>, if a rename has been necessary,
//	   <CODE>false</CODE> otherwise.
//	   @deprecated replaced by recoder.kit.transformation.RenamePackage
//	 */
//	public static boolean rename(
//		ChangeHistory ch,
//		CrossReferenceSourceInfo xr,
//		Namespace pkg,
//		String newName) {
//		Debug.assert(xr, pkg, newName);
//		Debug.assert(pkg.getName());
//		if (!newName.equals(pkg.getName())) {
//			NamespaceReferenceList refs = xr.getReferences(pkg);
//			for (int i = refs.size() - 1; i >= 0; i -= 1) {
//				MiscKit.rename(ch, refs.getPackageReference(i), newName);
//			}
//			return true;
//		}
//		return false;
//	}
}
