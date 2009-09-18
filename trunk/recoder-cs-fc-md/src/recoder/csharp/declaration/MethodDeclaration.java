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

public class MethodDeclaration
	extends CSharpDeclaration
	implements
		MemberDeclaration,
		TypeReferenceContainer,
		NamedProgramElement,
		ParameterContainer,
		Method,
		VariableScope,
		MemberNameContainer {

	/**
	 Parent.
	 */

	protected TypeDeclaration parent;

	/**
	 Return type.
	 */

	protected TypeReference returnType;

	/**
	 Name.
	 */

	protected MemberName name;

	/**
	 Parameters.
	 */

	protected ParameterDeclarationMutableList parameters;

	/**
	 Exceptions.
	 */

	protected Throws exceptions;

	/**
	 Body.
	 */

	protected StatementBlock body;

	/**
	   Service.
	 */

	protected ProgramModelInfo service;

	/**
	 Method declaration.
	 */

	public MethodDeclaration() {}

	/**
	 Method declaration.
	 @param modifiers a modifier mutable list.
	 @param returnType a type reference.
	 @param name an identifier.
	 @param parameters a parameter declaration mutable list.
	 @param exceptions a throws.
	 */

	public MethodDeclaration(
		ModifierMutableList modifiers,
		TypeReference returnType,
		Identifier name,
		ParameterDeclarationMutableList parameters,
		Throws exceptions) {
		super(modifiers);
		setTypeReference(returnType);
		setIdentifier(name);
		setParameters(parameters);
		setThrown(exceptions);
		makeParentRoleValid();
	}

	/**
	 Method declaration.
	 @param modifiers a modifier mutable list.
	 @param returnType a type reference.
	 @param name an identifier.
	 @param parameters a parameter declaration mutable list.
	 @param exceptions a throws.
	 @param body a statement block.
	 */

	public MethodDeclaration(
		ModifierMutableList modifiers,
		TypeReference returnType,
		Identifier name,
		ParameterDeclarationMutableList parameters,
		Throws exceptions,
		StatementBlock body) {
		super(modifiers);
		setTypeReference(returnType);
		setIdentifier(name);
		setParameters(parameters);
		setThrown(exceptions);
		setBody(body);
		makeParentRoleValid();
	}

	/**
	 Method declaration.
	 @param proto a method declaration.
	 */

	protected MethodDeclaration(MethodDeclaration proto) {
		super(proto);
		if (proto.returnType != null) {
			returnType = (TypeReference) proto.returnType.deepClone();
		}
		if (proto.name != null) {
			name = (MemberName) proto.name.deepClone();
		}
		if (proto.parameters != null) {
			parameters = (ParameterDeclarationMutableList) proto.parameters.deepClone();
		}
		if (proto.exceptions != null) {
			exceptions = (Throws) proto.exceptions.deepClone();
		}
		if (proto.body != null) {
			body = (StatementBlock) proto.body.deepClone();
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
		return new MethodDeclaration(this);
	}

	/**
	 Make parent role valid.
	 */

	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		if (returnType != null) {
			returnType.setParent(this);
		}
		if (name != null) {
			name.setMemberNameContainer(this);
		}
		if (exceptions != null) {
			exceptions.setParent(this);
		}
		if (parameters != null) {
			for (int i = parameters.size() - 1; i >= 0; i -= 1) {
				parameters.getParameterDeclaration(i).setParameterContainer(this);
			}
		}
		if (body != null) {
			body.setStatementContainer(this);
		}
	}

	public int getChildPositionCode(ProgramElement child) {
		// role 0 (IDX): modifier
		// role 1: return type (no occurance in constructors)
		// role 2: name
		// role 3 (IDX): parameter
		// role 4: throws
		// role 5: body
		// role 15: attributes        
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
		if (exceptions == child) {
			return 4;
		}
		if (body == child) {
			return 5;
		}
		return -1;
	}

	public SourceElement getFirstElement() {
		return getChildAt(0).getFirstElement();
	}

	public SourceElement getLastElement() {
		return getChildAt(getChildCount() - 1).getLastElement();
	}

	/**
	 Get AST parent.
	 @return the non terminal program element.
	 */

	public NonTerminalProgramElement getASTParent() {
		return parent;
	}

	/**
	 Get member parent.
	 @return the type declaration.
	 */

	public TypeDeclaration getMemberParent() {
		return parent;
	}

	/**
	 Set member parent.
	 @param decl a type declaration.
	 */

	public void setMemberParent(TypeDeclaration decl) {
		parent = decl;
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
		//        if (exceptions != null) result++;
		if (body != null)
			result++;
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
		//        if (exceptions != null) {
		//            if (index == 0) return exceptions;
		//            index--;
		//        }
		if (body != null) {
			if (index == 0)
				return body;
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 Get the number of statements in this container.
	 @return the number of statements.
	 */

	public int getStatementCount() {
		return (body != null) ? 1 : 0;
	}

	/*
	  Return the statement at the specified index in this node's
	  "virtual" statement array.
	  @param index an index for a statement.
	  @return the statement with the given index.
	  @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
	  of bounds.
	*/

	public Statement getStatementAt(int index) {
		if (body != null && index == 0) {
			return body;
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
			MemberName r = (MemberName) q;
			name = r;
			if (r != null) {
				r.setMemberNameContainer(this);
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
		if (exceptions == p) {
			Throws r = (Throws) q;
			exceptions = r;
			if (r != null) {
				r.setParent(this);
			}
			return true;
		}
		if (body == p) {
			StatementBlock r = (StatementBlock) q;
			body = r;
			if (r != null) {
				r.setStatementContainer(this);
			}
			return true;
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
	 Get name.
	 @return the string.
	 */

	public String getName() {
		return (name == null) ? null : name.getText();
	}

	/**
	 Get identifier.
	 @return the identifier.
	 */

	public Identifier getIdentifier() {
		return (name == null) ? null : name.getIdentifier();
	}

	/**
	 Set identifier.
	 @param id an identifier.
	 */

	public void setIdentifier(Identifier id) {
		if (name != null) {
			name.setIdentifier(id);
		} else {
			name = getFactory().createMemberName(id);
		}
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
	 Get thrown.
	 @return the throws.
	 */

	public Throws getThrown() {
		return exceptions;
	}

	/**
	 Set thrown.
	 @param exceptions a throws.
	 */

	public void setThrown(Throws exceptions) {
		this.exceptions = exceptions;
	}

	/**
	 Get body.
	 @return the statement block.
	 */

	public StatementBlock getBody() {
		return body;
	}

	/**
	 Set body.
	 @param body a statement block.
	 */

	public void setBody(StatementBlock body) {
		this.body = body;
	}

	/**
	 * Test whether the declaration is final.
	 */

	public boolean isSealed() {
		return super.isSealed();
	}

	/**
	 * Test whether the declaration is private.
	 */

	public boolean isPrivate() {
		return super.isPrivate();
	}

	/**
	 * Test whether the declaration is protected.
	 */

	public boolean isProtected() {
		return super.isProtected();
	}

	/**
	 * Test whether the declaration is public. Methods of interfaces
	 * are always public.
	 */

	public boolean isPublic() {
		return (getASTParent() instanceof InterfaceDeclaration) || super.isPublic();
	}

	/**
	 * Test whether the declaration is static.
	 */

	public boolean isStatic() {
		return super.isStatic();
	}

	/**
	 * Test whether the declaration is abstract. Methods of interfaces
	 * are always abstract.
	 */

	public boolean isAbstract() {
		return (getASTParent() instanceof InterfaceDeclaration) || super.isAbstract();
	}

	/**
	 * Test whether the declaration is native. Constructors
	 * are never native.
	 */

	public boolean isExtern() {
		return super.isExtern();
	}

	/**
	 * @see recoder.abstraction.Member#isInternal()
	 */
	public boolean isInternal() {
		return super.isInternal();
	}

	/**
	 * @see recoder.abstraction.Member#isNew()
	 */
	public boolean isNew() {
		return super.isNew();
	}

	/**
	 * @see recoder.abstraction.Method#isOverride()
	 */
	public boolean isOverride() {
		return super.isOverride();
	}

	/**
	 * @see recoder.abstraction.Method#isVirtual()
	 */
	public boolean isVirtual() {
		return super.isVirtual();
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

	private static void updateModel() {
		factory.getServiceConfiguration().getChangeHistory().updateModel();
	}

	public ClassType getContainingClassType() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		return service.getContainingClassType(this);
	}

	public Type getReturnType() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		return service.getReturnType(this);
	}

	public TypeList getSignature() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		return service.getSignature(this);
	}

	public ClassTypeList getExceptions() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		return service.getExceptions(this);
	}

	public Namespace getNamespace() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		return service.getNamespace(this);
	}

	public String getFullName() {
	    ClassType ct = getContainingClassType();
	    if (ct == null) {
	        throw new RuntimeException("No class found for " + getName());
        }
	    return Naming.dot(ct.getFullName(), getName());
	}

	public boolean isDefinedScope() {
		return true;
	}

	public void setDefinedScope(boolean defined) {}

	public VariableSpecificationList getVariablesInScope() {
		if (parameters == null || parameters.isEmpty()) {
			return VariableSpecificationList.EMPTY_LIST;
		}
		int s = parameters.size();
		VariableSpecificationMutableList res = new VariableSpecificationArrayList(s);
		for (int i = 0; i < s; i += 1) {
			res.add(parameters.getParameterDeclaration(i).getVariableSpecification());
		}
		return res;
	}

	public VariableSpecification getVariableInScope(String name) {
		Debug.asserta(name);
		if (parameters == null) {
			return null;
		}
		for (int i = 0, s = parameters.size(); i < s; i += 1) {
			VariableSpecification res =
				parameters.getParameterDeclaration(i).getVariableSpecification();
			if (name.equals(res.getName())) {
				return res;
			}
		}
		return null;
	}

	public void addVariableToScope(VariableSpecification var) {
		Debug.asserta(var);
	}

	public void removeVariableFromScope(String name) {
		Debug.asserta(name);
	}

	public void accept(SourceVisitor v) {
		v.visitMethodDeclaration(this);
	}
	/**
	 * @see recoder.csharp.MemberNameContainer#getMemberName()
	 */
	public MemberName getMemberName() {
		return name;
	}

	/**
	 * @see recoder.csharp.MemberNameContainer#setMemberName(MemberName)
	 */
	public void setMemberName(MemberName mname) {
		name = mname;
	}


}
