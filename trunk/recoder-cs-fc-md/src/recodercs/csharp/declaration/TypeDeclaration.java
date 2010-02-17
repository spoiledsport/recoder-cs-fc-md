// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration;

import recodercs.abstraction.ClassType;
import recodercs.service.ProgramModelInfo;
import recodercs.convenience.Naming;
import recodercs.util.Debug;
import recodercs.*;
import recodercs.abstraction.*;
import recodercs.convenience.*;
import recodercs.csharp.*;
import recodercs.csharp.attributes.*;
import recodercs.csharp.declaration.modifier.*;
import recodercs.list.*;
import recodercs.service.*;
import recodercs.util.*;

import java.util.Enumeration;

/**
 Type declaration.
 @author <TT>AutoDoc</TT>
 */
public abstract class TypeDeclaration
	extends CSharpDeclaration
	implements
		NamedProgramElement,
		MemberDeclaration,
		DeclaredType,
		VariableScope,
		TypeDeclarationContainer,
		TypeScope {


	/**
	 Name.
	 */
	protected Identifier name;

	/**
	 Parent.
	 */
	protected TypeDeclarationContainer parent;

	/**
	 Members.
	 */
	protected MemberDeclarationMutableList members;

	/**
	   Service.
	 */
	protected ProgramModelInfo service;

	/**
	   Undefined scope tag.
	 */
	protected final static MutableMap UNDEFINED_SCOPE = new NaturalHashTable();

	/**
	   Scope table for types.
	*/
	protected MutableMap name2type = UNDEFINED_SCOPE;

	/**
	   Scope table for fields.
	*/
	protected MutableMap name2field = UNDEFINED_SCOPE;

	/**
	 Type declaration.
	 */
	public TypeDeclaration() {
	}

	/**
	 Type declaration.
	 @param name an identifier.
	 */
	public TypeDeclaration(Identifier name) {
		setIdentifier(name);
	}

	/**
	 Type declaration.
	 @param mods a modifier mutable list.
	 @param name an identifier.
	 */
	public TypeDeclaration(ModifierMutableList mods, Identifier name) {
		super(mods);
		setIdentifier(name);
	}

	/**
	 Type declaration.
	 @param proto a type declaration.
	 */
	protected TypeDeclaration(TypeDeclaration proto) {
		super(proto);
		if (proto.name != null) {
			name = (Identifier) proto.name.deepClone();
		}
		if (proto.members != null) {
			members = (MemberDeclarationMutableList) proto.members.deepClone();
		}
	}

	/**
	 Make parent role valid.
	 */
	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		if (name != null) {
			name.setParent(this);
		}
		if (members != null) {
			for (int i = members.size() - 1; i >= 0; i -= 1) {
				members.getMemberDeclaration(i).setMemberParent(this);
			}
		}
	}

// Commented out, because the CSharpDeclaration implements it.
//	public SourceElement getFirstElement() {
//		if (modifiers != null && !modifiers.isEmpty()) {
//			return modifiers.getModifier(0);
//		} else {
//			return super.getFirstElement();
//		}
//	}

	public SourceElement getLastElement() {
		return this;
	}

	/**
	 Get name.
	 @return the string.
	 */
	public final String getName() {
		return (name == null) ? null : name.getText();
	}

	/**
	 Get identifier.
	 @return the identifier.
	 */
	public Identifier getIdentifier() {
		return name;
	}

	/**
	 Set identifier.
	 @param id an identifier.
	 */
	public void setIdentifier(Identifier id) {
		name = id;
	}

	/**
	 Get parent.
	 @return the type declaration container.
	 */
	public TypeDeclarationContainer getParent() {
		return parent;
	}

	/**
	 Get member parent.
	 @return the type declaration.
	 */
	public TypeDeclaration getMemberParent() {
		if (parent instanceof TypeDeclaration) {
			return (TypeDeclaration) parent;
		} else {
			return null;
		}
	}

	/**
	 Set parent.
	 @param p a type declaration container.
	 */
	public void setParent(TypeDeclarationContainer p) {
		parent = p;
	}

	/**
	 Set member parent.
	 @param p a type declaration.
	 */
	public void setMemberParent(TypeDeclaration p) {
		parent = p;
	}

	/**
	 Get AST parent.
	 @return the non terminal program element.
	 */
	public NonTerminalProgramElement getASTParent() {
		return parent;
	}

	/**
	 Get members.
	 @return the member declaration mutable list.
	 */
	public MemberDeclarationMutableList getMembers() {
		return members;
	}

	/**
	 Set members.
	 @param list a member declaration mutable list.
	 */
	public void setMembers(MemberDeclarationMutableList list) {
		members = list;
	}


	/**
	 * Test whether the declaration is public.
	 */
	public boolean isPublic() {
		return (getASTParent() instanceof InterfaceDeclaration)
			|| super.isPublic();
	}

	/**
	 * Test whether the declaration is static.
	 */
	public boolean isStatic() {
		return (getASTParent() instanceof InterfaceDeclaration)
			|| super.isStatic();
	}
	/**
	 * @see recodercs.abstraction.Member#isInternal()
	 */
	public boolean isInternal() {
		return super.isInternal();
	}

	/**
	 * @see recodercs.abstraction.Member#isNew()
	 */
	public boolean isNew() {
		return super.isNew();
	}

	/**
	 * @see recodercs.abstraction.Member#isPrivate()
	 */
	public boolean isPrivate() {
		return super.isPrivate();
	}

	/**
	 * @see recodercs.abstraction.Member#isProtected()
	 */
	public boolean isProtected() {
		return super.isProtected();
	}

	/**
	 * @see recodercs.abstraction.Member#isSealed()
	 */
	public boolean isSealed() {
		return super.isSealed();
	}

	/**
	 * @see recodercs.abstraction.DeclaredType#isAbstract()
	 */
	public boolean isAbstract() {
		return super.isAbstract();
	}


	public ProgramModelInfo getProgramModelInfo() {
		return service;
	}

	public void setProgramModelInfo(ProgramModelInfo service) {
		this.service = service;
	}

	protected static void updateModel() {
		factory.getServiceConfiguration().getChangeHistory().updateModel();
	}

	public String getFullName() {
		return Naming.getFullName(this);
		/*
		    DeclaredTypeContainer container = getContainer();
		String containerName = null;
		    if (container instanceof Method) {
		    containerName = String.valueOf(System.identityHashCode(container));
		        container = container.getContainer();
		    if (container != null) {
			containerName = Naming.dot(container.getFullName(), containerName);
		    }
		    } else {
		    if (container != null) {
			containerName = container.getFullName();
		    }	    
		}
		String name = getName();
		if (name == null) {
		    name = String.valueOf(System.identityHashCode(this));
		}
		if (containerName != null) {
		    name = Naming.dotOnDemand(containerName, name);
		}
		return name;
		*/
	}

	public DeclaredTypeContainer getContainer() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		if (service == null) {
			Debug.error("Service not defined in TypeDeclaration " + getName());
		}
		return service.getDeclaredTypeContainer(this);
	}

	public ClassType getContainingClassType() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		if (service == null) {
			Debug.error("Service not defined in TypeDeclaration " + getName());
		}
		DeclaredTypeContainer ctc = service.getDeclaredTypeContainer(this);
		return (ctc instanceof ClassType) ? (ClassType) ctc : null;
	}

	public Namespace getNamespace() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		if (service == null) {
			Debug.error("Service not defined in TypeDeclaration " + getName());
		}
		return service.getNamespace(this);
	}


	public boolean isDefinedScope() {
		return name2type != UNDEFINED_SCOPE;
	}

	public void setDefinedScope(boolean defined) {
		if (!defined) {
			name2type = UNDEFINED_SCOPE;
			name2field = UNDEFINED_SCOPE;
		} else {
			name2type = null;
			name2field = null;
		}
	}

	public DeclaredTypeList getTypesInScope() {
		if (name2type == null || name2type.isEmpty()) {
			return DeclaredTypeList.EMPTY_LIST;
		}
		DeclaredTypeMutableList res = new DeclaredTypeArrayList();
		Enumeration menum = name2type.elements();
		while (menum.hasMoreElements()) {
			res.add((DeclaredType) menum.nextElement());
		}
		return res;
	}

	public DeclaredType getTypeInScope(String name) {
		Debug.asserta(name);
		if (name2type == null) {
			return null;
		}
		return (DeclaredType) name2type.get(name);
	}

	public void addTypeToScope(DeclaredType type, String name) {
		Debug.asserta(type, name);
		if (name2type == null || name2type == UNDEFINED_SCOPE) {
			name2type = new NaturalHashTable();
		}
		name2type.put(name, type);
	}

	public void removeTypeFromScope(String name) {
		Debug.asserta(name);
		if (name2type == null || name2type == UNDEFINED_SCOPE) {
			return;
		}
		name2type.remove(name);
	}

	public FieldSpecificationList getFieldsInScope() {
		if (name2field == null || name2field.isEmpty()) {
			return FieldSpecificationList.EMPTY_LIST;
		}
		FieldSpecificationMutableList res = new FieldSpecificationArrayList();
		Enumeration menum = name2field.elements();
		while (menum.hasMoreElements()) {
			res.add((FieldSpecification) menum.nextElement());
		}
		return res;
	}

	public VariableSpecificationList getVariablesInScope() {
		return getFieldsInScope();
	}

	public VariableSpecification getVariableInScope(String name) {
		Debug.asserta(name);
		if (name2field == null) {
			return null;
		}
		return (VariableSpecification) name2field.get(name);
	}

	public void addVariableToScope(VariableSpecification var) {
		Debug.asserta(var instanceof FieldSpecification);
		if (name2field == null || name2field == UNDEFINED_SCOPE) {
			name2field = new NaturalHashTable();
		}
		name2field.put(var.getName(), var);
	}

	public void removeVariableFromScope(String name) {
		Debug.asserta(name);
		if (name2field == null || name2field == UNDEFINED_SCOPE) {
			return;
		}
		name2field.remove(name);
	}

	/**
	 Get the number of type declarations in this container.
	 @return the number of type declarations.
	 */
	public int getTypeDeclarationCount() {
		int count = 0;
		if (members != null) {
			for (int i = members.size() - 1; i >= 0; i -= 1) {
				if (members.getMemberDeclaration(i)
					instanceof TypeDeclaration) {
					count += 1;
				}
			}
		}
		return count;
	}


	/*
	  Return the type declaration at the specified index in this node's
	  "virtual" type declaration array.
	  @param index an index for a type declaration.
	  @return the type declaration with the given index.
	  @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
	  of bounds.
	*/
	public TypeDeclaration getTypeDeclarationAt(int index) {
		if (members != null) {
			int s = members.size();
			for (int i = 0; i < s && index >= 0; i += 1) {
				MemberDeclaration md = members.getMemberDeclaration(i);
				if (md instanceof TypeDeclaration) {
					if (index == 0) {
						return (TypeDeclaration) md;
					}
					index -= 1;
				}
			}
		}
		throw new ArrayIndexOutOfBoundsException();
	}


}
