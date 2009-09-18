package recoder.io;

import recoder.csharp.CompilationUnit;
import recoder.csharp.NamespaceSpecification;
import recoder.csharp.SourceVisitor;
import recoder.csharp.declaration.ClassDeclaration;
import recoder.csharp.declaration.DelegateDeclaration;
import recoder.csharp.declaration.EnumDeclaration;
import recoder.csharp.declaration.InterfaceDeclaration;
import recoder.csharp.declaration.StructDeclaration;
import recoder.csharp.declaration.TypeDeclaration;
import recoder.list.TypeDeclarationArrayList;
import recoder.list.TypeDeclarationList;
import recoder.list.TypeDeclarationMutableList;

/**
 * @author kis
 *
 * Finds a ClassDeclaration, StructDeclaration or InterfaceDeclaration in a 
 * CompilationUnit. 
 * It matches the fully qualified name of the type.
 * 
 * This class is new in RECODER-CS. It is used to parse a compilation unit, and
 * retrieve all the information about classes, interfaces and structures in that
 * class. This is needed, because in C# classes may be deeply hidden in a compilation
 * unit.
 * 
 */
public class TypeFinderVisitor extends SourceVisitor {

	TypeDeclarationMutableList foundTypes;
	StringBuffer currentprefix;
	String searchName;
	
	public TypeFinderVisitor() {
	}
	
	public synchronized TypeDeclarationList findType(String typename, CompilationUnit cu) {
			foundTypes = new TypeDeclarationArrayList();
			searchName=typename;
			currentprefix=new StringBuffer();
			cu.accept(this);
			return foundTypes;
	}
	
	
	/**
	 * @see recoder.csharp.SourceVisitor#visitCompilationUnit(CompilationUnit)
	 */
	public void visitCompilationUnit(CompilationUnit x)  {
		int count = x.getNamespaceSpecificationCount();
		for (int i=0; i<count ; i ++) {
			x.getNamespaceSpecificationAt(i).accept(this);
		}	
		count = x.getTypeDeclarationCount();
		for (int i=0; i<count ; i ++) {
			x.getTypeDeclarationAt(i).accept(this);
		}
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitClassDeclaration(ClassDeclaration)
	 */
	public void visitClassDeclaration(ClassDeclaration x) {
		visitTypeDeclaration(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitInterfaceDeclaration(InterfaceDeclaration)
	 */
	public void visitInterfaceDeclaration(InterfaceDeclaration x) {
		visitTypeDeclaration(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitStructDeclaration(StructDeclaration)
	 */
	public void visitStructDeclaration(StructDeclaration x) {
		visitTypeDeclaration(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitDelegateDeclaration(DelegateDeclaration)
	 */
	public void visitDelegateDeclaration(DelegateDeclaration x) {
		visitTypeDeclaration(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitEnumDeclaration(EnumDeclaration)
	 */
	public void visitEnumDeclaration(EnumDeclaration x) {
		visitTypeDeclaration(x);
	}



	/**
	 * @see recoder.csharp.SourceVisitor#visitNamespaceSpecification(NamespaceSpecification)
	 */
	public void visitNamespaceSpecification(NamespaceSpecification x)  {
		String name=x.getNamespaceReference().getFullName();
		addPrefix(name);
		int count = x.getNamespaceSpecificationCount();
		for (int i=0; i<count ; i ++) {
			x.getNamespaceSpecificationAt(i).accept(this);
		}	
		count = x.getTypeDeclarationCount();
		for (int i=0; i<count ; i ++) {
			x.getTypeDeclarationAt(i).accept(this);
		}
		
		removePrefix(name);		
	}

	
	private void visitTypeDeclaration(TypeDeclaration x)  {
		String name=x.getName();
		if (checkTypeNames(name)) {
			foundTypes.add(x);
		}
		addPrefix(name);
		int count = x.getTypeDeclarationCount();
		for (int i=0; i<count ; i ++) {
			x.getTypeDeclarationAt(i).accept(this);
		}
		removePrefix(name);		
	} 
		
	boolean checkTypeNames(String foundname) {
		StringBuffer fullName=new StringBuffer(currentprefix.toString());
		if (fullName.length()>0) {
			fullName.append('.');
		}
		fullName.append(foundname);
		if (fullName.toString().equals(searchName)) {
			return true;
		}
		return false;
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
