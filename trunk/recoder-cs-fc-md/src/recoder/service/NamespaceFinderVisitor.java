package recoder.service;

import recoder.abstraction.Namespace;
import recoder.convenience.Naming;
import recoder.csharp.CompilationUnit;
import recoder.csharp.NamespaceSpecification;
import recoder.csharp.SourceVisitor;
import recoder.csharp.declaration.ClassDeclaration;
import recoder.csharp.declaration.DelegateDeclaration;
import recoder.csharp.declaration.EnumDeclaration;
import recoder.csharp.declaration.InterfaceDeclaration;
import recoder.csharp.declaration.StructDeclaration;
import recoder.csharp.declaration.TypeDeclaration;
import recoder.list.NamespaceArrayList;
import recoder.list.NamespaceList;
import recoder.list.NamespaceMutableList;
import recoder.list.NamespaceSpecificationArrayList;
import recoder.list.NamespaceSpecificationList;
import recoder.list.NamespaceSpecificationMutableList;
import recoder.list.TypeDeclarationArrayList;
import recoder.list.TypeDeclarationList;
import recoder.list.TypeDeclarationMutableList;

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
	 * @see recoder.csharp.SourceVisitor#visitCompilationUnit(CompilationUnit)
	 */
	public void visitCompilationUnit(CompilationUnit x)  {
		int count = x.getNamespaceSpecificationCount();
		for (int i=0; i<count ; i ++) {
			x.getNamespaceSpecificationAt(i).accept(this);
		}	
	}


	/**
	 * @see recoder.csharp.SourceVisitor#visitNamespaceSpecification(NamespaceSpecification)
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
