package recoder.csharp.declaration;

import recoder.abstraction.*;
import recoder.csharp.*;
import recoder.list.*;
import recoder.util.*;

/**
 * ClassTypeDeclaration.java
 * @author orosz
 *
 * 
 */
public abstract class ClassTypeDeclaration extends TypeDeclaration 
implements ClassType,
			TypeDeclarationContainer

{

	/**
	 * Constructor for ClassTypeDeclaration.
	 */
	public ClassTypeDeclaration() {
		super();
	}

	/**
	 * Constructor for ClassTypeDeclaration.
	 * @param name
	 */
	public ClassTypeDeclaration(Identifier name) {
		super(name);
	}

	/**
	 * Constructor for ClassTypeDeclaration.
	 * @param mods
	 * @param name
	 */
	public ClassTypeDeclaration(ModifierMutableList mods, Identifier name) {
		super(mods, name);
	}

	/**
	 * Constructor for ClassTypeDeclaration.
	 * @param proto
	 */
	public ClassTypeDeclaration(TypeDeclaration proto) {
		super(proto);
	}


	/**
	 * @see recoder.csharp.declaration.TypeDeclaration#isInterface()
	 */
	public abstract boolean isInterface();

	/**
	 * @see recoder.abstraction.ClassType#isStruct()
	 */
	public abstract boolean isStruct();
	


	/**
	 * @see recoder.abstraction.Member#isInternal()
	 */
	public boolean isInternal() {
		return false;
	}

	/**
	 * @see recoder.abstraction.Member#isNew()
	 */
	public boolean isNew() {
		return false;
	}


	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		return 0;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
		return null;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
	 */
	public int getChildPositionCode(ProgramElement child) {
		return 0;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
	 */
	public boolean replaceChild(ProgramElement p, ProgramElement q) {
		return false;
	}

	/**
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {}

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return null;
	}

	public ClassTypeList getSupertypes() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		if (service == null) {
			Debug.error("Service not defined in TypeDeclaration " + getName());
		}
		return service.getSupertypes(this);
	}

	public ClassTypeList getAllSupertypes() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		if (service == null) {
			Debug.error("Service not defined in TypeDeclaration " + getName());
		}
		return service.getAllSupertypes(this);
	}

	public FieldList getFields() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		if (service == null) {
			Debug.error("Service not defined in TypeDeclaration " + getName());
		}
		return service.getFields(this);
	}

	public FieldList getAllFields() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		if (service == null) {
			Debug.error("Service not defined in TypeDeclaration " + getName());
		}
		return service.getAllFields(this);
	}

	public MethodList getMethods() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		if (service == null) {
			Debug.error("Service not defined in TypeDeclaration " + getName());
		}
		return service.getMethods(this);
	}

	public MethodList getAllMethods() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		if (service == null) {
			Debug.error("Service not defined in TypeDeclaration " + getName());
		}
		return service.getAllMethods(this);
	}

	public ConstructorList getConstructors() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		if (service == null) {
			Debug.error("Service not defined in TypeDeclaration " + getName());
		}
		return service.getConstructors(this);
	}

	public DeclaredTypeList getAllTypes() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		if (service == null) {
			Debug.error("Service not defined in TypeDeclaration " + getName());
		}
		return service.getAllTypes(this);
	}

	public DeclaredTypeList getDeclaredTypes() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		if (service == null) {
			Debug.error("Service not defined in TypeDeclaration " + getName());
		}
		return service.getTypes(this);
	}
	
	public abstract Extends getExtendedTypes();

}
