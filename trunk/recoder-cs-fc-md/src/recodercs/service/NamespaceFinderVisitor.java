package recodercs.service;

import recodercs.service.NameInfo;
import recodercs.abstraction.Namespace;
import recodercs.convenience.Naming;
import recodercs.csharp.CompilationUnit;
import recodercs.csharp.NamespaceSpecification;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.declaration.ClassDeclaration;
import recodercs.csharp.declaration.DelegateDeclaration;
import recodercs.csharp.declaration.EnumDeclaration;
import recodercs.csharp.declaration.InterfaceDeclaration;
import recodercs.csharp.declaration.StructDeclaration;
import recodercs.csharp.declaration.TypeDeclaration;
import recodercs.list.NamespaceArrayList;
import recodercs.list.NamespaceList;
import recodercs.list.NamespaceMutableList;
import recodercs.list.NamespaceSpecificationArrayList;
import recodercs.list.NamespaceSpecificationList;
import recodercs.list.NamespaceSpecificationMutableList;
import recodercs.list.TypeDeclarationArrayList;
import recodercs.list.TypeDeclarationList;
import recodercs.list.TypeDeclarationMutableList;

/**
 * @author kis
 *
 * Finds all the declared namespaces in a compilation unit 
 * CompilationUnit. 
 * It matches the fully qualified name of the type.
 */
class NamespaceFinderVisitor extends SourceVisitor {

	NamespaceMutableList foundNamespaces;
	StringBuffer currentprefix;
	NameInfo nameInfo;
	
	public NamespaceFinderVisitor(NameInfo ni) {
		nameInfo = ni;
	}
	
	public synchronized NamespaceList find(CompilationUnit cu) {
			foundNamespaces = new NamespaceArrayList();
			currentprefix=new StringBuffer("");
			cu.accept(this);
			return foundNamespaces;
			
	}
	
	
	/**
	 * @see recodercs.csharp.SourceVisitor#visitCompilationUnit(CompilationUnit)
	 */
	public void visitCompilationUnit(CompilationUnit x)  {
		int count = x.getNamespaceSpecificationCount();
		for (int i=0; i<count ; i ++) {
			x.getNamespaceSpecificationAt(i).accept(this);
		}	
	}


	/**
	 * @see recodercs.csharp.SourceVisitor#visitNamespaceSpecification(NamespaceSpecification)
	 */
	public void visitNamespaceSpecification(NamespaceSpecification x)  {
		String name=x.getNamespaceReference().getFullName();
		Namespace n=nameInfo.createNamespace((currentprefix.toString().equals("")) ? name : Naming.dot(currentprefix.toString(),name));
		if (n != null) {
			foundNamespaces.add(n);
		}
		addPrefix(name);
		
		int count = x.getNamespaceSpecificationCount();
		for (int i=0; i<count ; i ++) {
			x.getNamespaceSpecificationAt(i).accept(this);
		}	
		removePrefix(name);		
	}

	

	void addPrefix(String x) {
		if (currentprefix.length()>0) {
			currentprefix.append('.');
		}
		currentprefix.append(x);
	}
	
	void removePrefix(String x) {
		if (currentprefix.length()>0) {
			int index = currentprefix.toString().lastIndexOf("."+x);
			if (index>=0) {
				currentprefix.replace(index,currentprefix.length(),"");
			} else {
				index = currentprefix.toString().lastIndexOf(x);
				if (index>=0) {
					currentprefix.replace(index,currentprefix.length(),"");
				}
			}
		}
	}


}
