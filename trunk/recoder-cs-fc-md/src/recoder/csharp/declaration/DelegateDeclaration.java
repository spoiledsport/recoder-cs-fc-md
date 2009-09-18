// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration;

import recoder.*;
import recoder.abstraction.*;
import recoder.service.*;
import recoder.list.*;
import recoder.csharp.*;
import recoder.csharp.attributes.AttributeSection;
import recoder.csharp.attributes.AttributeSectionMutableList;
import recoder.csharp.reference.*;
import recoder.convenience.*;
import recoder.abstraction.Namespace;
import recoder.abstraction.Type;
import recoder.util.Debug;

/**
 Method declaration.
 @author <TT>AutoDoc</TT>
 */

public class DelegateDeclaration
	extends TypeDeclaration
	implements TypeReferenceContainer, NamedProgramElement, ParameterContainer, Delegate {

	/**
	 Return type.
	 */

	protected TypeReference returnType;

	/**
	 Parameters.
	 */

	protected ParameterDeclarationMutableList parameters;

//	/**
//	   Service.
//	 */
//
//	protected ProgramModelInfo service;

	/**
	 Method declaration.
	 */

	public DelegateDeclaration() {}

	/**
	 Method declaration.
	 @param modifiers a modifier mutable list.
	 @param returnType a type reference.
	 @param name an identifier.
	 @param parameters a parameter declaration mutable list.
	 @param exceptions a throws.
	 */

	public DelegateDeclaration(
		ModifierMutableList modifiers,
		TypeReference returnType,
		Identifier name,
		ParameterDeclarationMutableList parameters) {
		super(modifiers, name);
		setTypeReference(returnType);
		setParameters(parameters);
		makeParentRoleValid();
	}

	/**
	 Method declaration.
	 @param proto a method declaration.
	 */

	protected DelegateDeclaration(DelegateDeclaration proto) {
		super(proto);
		if (proto.returnType != null) {
			returnType = (TypeReference) proto.returnType.deepClone();
		}
		if (proto.parameters != null) {
			parameters = (ParameterDeclarationMutableList) proto.parameters.deepClone();
		}
		if (proto.attrSections != null) {
			attrSections = (AttributeSectionMutableList) proto.attrSections.deepClone();
		}
		makeParentRoleValid();
	}

	/**
	 Deep clone.
	 @return the object.
	 */

	public Object deepClone() {
		return new DelegateDeclaration(this);
	}

	/**
	 Make parent role valid.
	 */

	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		if (returnType != null) {
			returnType.setParent(this);
		}
		if (parameters != null) {
			for (int i = parameters.size() - 1; i >= 0; i -= 1) {
				parameters.getParameterDeclaration(i).setParameterContainer(this);
			}
		}
	}

	public int getChildPositionCode(ProgramElement child) {
		// role 0 (IDX): modifier
		// role 1: return type (no occurance in constructors)
		// role 2: name
		// role 3 (IDX): parameter
		// role 4: throws
		// role 5: body
		if (attrSections != null) {
			int index = attrSections.indexOf(child);
			if (index >= 0) {
				return (index << 4) | 15;
			}
		}
		if (modifiers != null) {
			int index = modifiers.indexOf(child);
			if (index >= 0) {
				return (index << 4) | 0;
			}
		}
		if (returnType == child) {
			return 1;
		}
		if (name == child) {
			return 2;
		}
		if (parameters != null) {
			int index = parameters.indexOf(child);
			if (index >= 0) {
				return (index << 4) | 3;
			}
		}
		return -1;
	}

	public SourceElement getFirstElement() {
		return super.getFirstElement();
//		return getChildAt(0).getFirstElement();
	}

	public SourceElement getLastElement() {
//		return getChildAt(getChildCount() - 1).getLastElement();
		return this;
	}

	/**
	 Get AST parent.
	 @return the non terminal program element.
	 */

	public NonTerminalProgramElement getASTParent() {
		return parent;
	}

	/**
	 Returns the number of children of this node.
	 @return an int giving the number of children of this node
	*/

	public int getChildCount() {
		int result = 0;
		if (attrSections != null)
			result += attrSections.size();
		if (modifiers != null)
			result += modifiers.size();
		if (returnType != null)
			result++;
		if (name != null)
			result++;
		if (parameters != null)
			result += parameters.size();
		return result;
	}

	/**
	 Returns the child at the specified index in this node's "virtual"
	 child array
	 @param index an index into this node's "virtual" child array
	 @return the program element at the given position
	 @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
	            of bounds
	*/

	public ProgramElement getChildAt(int index) {
		int len;
		if (attrSections != null) {
			len = attrSections.size();
			if (len > index) {
				return attrSections.getAttributeSection(index);
			}
			index -= len;
		}
		if (modifiers != null) {
			len = modifiers.size();
			if (len > index) {
				return modifiers.getProgramElement(index);
			}
			index -= len;
		}
		if (returnType != null) {
			if (index == 0)
				return returnType;
			index--;
		}
		if (name != null) {
			if (index == 0)
				return name;
			index--;
		}
		if (parameters != null) {
			len = parameters.size();
			if (len > index) {
				return parameters.getProgramElement(index);
			}
			index -= len;
		}

		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * Replace a single child in the current node.
	 * The child to replace is matched by identity and hence must be known
	 * exactly. The replacement element can be null - in that case, the child
	 * is effectively removed.
	 * The parent role of the new child is validated, while the
	 * parent link of the replaced child is left untouched.
	 * @param p the old child.
	 * @param p the new child.
	 * @return true if a replacement has occured, false otherwise.
	 * @exception ClassCastException if the new child cannot take over
	 *            the role of the old one.
	 */

	public boolean replaceChild(ProgramElement p, ProgramElement q) {
		if (p == null) {
			throw new NullPointerException();
		}
		int count;
		count = (attrSections == null) ? 0 : attrSections.size();
		for (int i = 0; i < count; i++) {
			if (attrSections.getAttributeSection(i) == p) {
				if (q == null) {
					attrSections.remove(i);
				} else {
					AttributeSection r = (AttributeSection) q;
					attrSections.set(i, r);
					r.setParent(this);
				}
				return true;
			}
		}
		count = (modifiers == null) ? 0 : modifiers.size();
		for (int i = 0; i < count; i++) {
			if (modifiers.getProgramElement(i) == p) {
				if (q == null) {
					modifiers.remove(i);
				} else {
					Modifier r = (Modifier) q;
					modifiers.set(i, r);
					r.setParent(this);
				}
				return true;
			}
		}
		if (returnType == p) {
			TypeReference r = (TypeReference) q;
			returnType = r;
			if (r != null) {
				r.setParent(this);
			}
			return true;
		}
		if (name == p) {
			Identifier r = (Identifier) q;
			name = r;
			if (r != null) {
				r.setParent(this);
			}
			return true;
		}
		count = (parameters == null) ? 0 : parameters.size();
		for (int i = 0; i < count; i++) {
			if (parameters.getProgramElement(i) == p) {
				if (q == null) {
					parameters.remove(i);
				} else {
					ParameterDeclaration r = (ParameterDeclaration) q;
					parameters.set(i, r);
					r.setParameterContainer(this);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 Get the number of type references in this container.
	 @return the number of type references.
	 */

	public int getTypeReferenceCount() {
		return (returnType != null) ? 1 : 0;
	}

	/*
	  Return the type reference at the specified index in this node's
	  "virtual" type reference array.
	  @param index an index for a type reference.
	  @return the type reference with the given index.
	  @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
	  of bounds.
	*/

	public TypeReference getTypeReferenceAt(int index) {
		if (returnType != null && index == 0) {
			return returnType;
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 Get the number of parameters in this container.
	 @return the number of parameters.
	 */

	public int getParameterDeclarationCount() {
		return (parameters != null) ? parameters.size() : 0;
	}

	/*
	  Return the parameter declaration at the specified index in this node's
	  "virtual" parameter declaration array.
	  @param index an index for a parameter declaration.
	  @return the parameter declaration with the given index.
	  @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
	  of bounds.
	*/

	public ParameterDeclaration getParameterDeclarationAt(int index) {
		if (parameters != null) {
			return parameters.getParameterDeclaration(index);
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 Get return type.
	 @return the type reference.
	 */

	public TypeReference getTypeReference() {
		return returnType;
	}

	/**
	 Sets the return type.
	 Null is okay for subclass ConstructorDeclaration.
	 */

	public void setTypeReference(TypeReference type) {
		returnType = type;
	}

	/**
	 Get parameters.
	 @return the parameter declaration mutable list.
	 */

	public ParameterDeclarationMutableList getParameters() {
		return parameters;
	}

	/**
	 Set parameters.
	 @param list a parameter declaration mutable list.
	 */

	public void setParameters(ParameterDeclarationMutableList list) {
		parameters = list;
	}

	/**
	 * Test whether the declaration is public. Methods of interfaces
	 * are always public.
	 */

	public boolean isPublic() {
		return (getASTParent() instanceof InterfaceDeclaration) || super.isPublic();
	}

	/**
	 * Test whether the declaration is abstract. Methods of interfaces
	 * are always abstract.
	 */

	public boolean isAbstract() {
		return (getASTParent() instanceof InterfaceDeclaration);
	}


	public ProgramModelInfo getProgramModelInfo() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		return service;
	}


	public void setProgramModelInfo(ProgramModelInfo service) {
		this.service = service;
	}


	protected static void updateModel() {
		factory.getServiceConfiguration().getChangeHistory().updateModel();
	}
	
	public void accept(SourceVisitor v) {
		v.visitDelegateDeclaration(this);
	}

	/**
	 * @see recoder.csharp.StatementContainer#getStatementAt(int)
	 */
	public Statement getStatementAt(int index) {
		return null;
	}

	/**
	 * @see recoder.csharp.StatementContainer#getStatementCount()
	 */
	public int getStatementCount() {
		return 0;
	}

	/**
	 * @see recoder.csharp.declaration.CSharpDeclaration#isSealed()
	 */
	public boolean isSealed() {
		return false;
	}

	/**
	 * @see recoder.csharp.declaration.CSharpDeclaration#isStatic()
	 */
	public boolean isStatic() {
		return false;
	}
	
	public TypeList getSignature() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		return service.getSignature(this);
	}

	public Type getReturnType() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		return service.getReturnType(this);
	}
	
	public boolean isCompatibleMethod(Method m) {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		return service.isCompatibleMethod(m,this);
	}
	

}
