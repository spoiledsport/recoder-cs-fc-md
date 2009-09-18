// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration;

import recoder.*;
import recoder.csharp.*;
import recoder.csharp.attributes.AttributeSection;
import recoder.csharp.attributes.AttributeSectionMutableList;
import recoder.csharp.declaration.modifier.Readonly;
import recoder.csharp.declaration.modifier.Volatile;
import recoder.csharp.reference.*;
import recoder.list.*;

/**
 Field declaration.
 @author <TT>AutoDoc</TT>
 */

public class FieldDeclaration
	extends VariableDeclaration
	implements MemberDeclaration {

	/**
	 Parent.
	 */

	protected TypeDeclaration parent;

	/**
	 Field specs.
	 */

	protected FieldSpecificationMutableList fieldSpecs;

	/**
	 Field declaration.
	 */

	public FieldDeclaration() {
	}

	/**
	 Field declaration.
	 @param typeRef a type reference.
	 @param name an identifier.
	 */

	public FieldDeclaration(TypeReference typeRef, Identifier name) {
		setTypeReference(typeRef);
		FieldSpecificationMutableList list = new FieldSpecificationArrayList(1);
		list.add(getFactory().createFieldSpecification(name));
		setFieldSpecifications(list);
		makeParentRoleValid();
	}

	/**
	 Field declaration.
	 @param mods a modifier mutable list.
	 @param typeRef a type reference.
	 @param name an identifier.
	 @param init an expression.
	 */

	public FieldDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		Identifier name,
		Expression init) {
		setModifiers(mods);
		setTypeReference(typeRef);
		FieldSpecificationMutableList list = new FieldSpecificationArrayList(1);
		list.add(getFactory().createFieldSpecification(name, init));
		setFieldSpecifications(list);
		makeParentRoleValid();
	}

	/**
	 Field declaration.
	 @param mods a modifier mutable list.
	 @param typeRef a type reference.
	 @param vars a variable specification mutable list.
	 */

	public FieldDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		FieldSpecificationMutableList vars) {
		setModifiers(mods);
		setTypeReference(typeRef);
		setFieldSpecifications(vars);
		makeParentRoleValid();
	}

	/**
	 Field declaration.
	 @param proto a field declaration.
	 */

	protected FieldDeclaration(FieldDeclaration proto) {
		super(proto);
		if (proto.fieldSpecs != null) {
			fieldSpecs =
				(FieldSpecificationMutableList) proto.fieldSpecs.deepClone();
		}
		if (proto.attrSections != null) {
			attrSections =
				(AttributeSectionMutableList) proto.attrSections.deepClone();
		}
		makeParentRoleValid();
	}

	/**
	 Deep clone.
	 @return the object.
	 */

	public Object deepClone() {
		return new FieldDeclaration(this);
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
	 @param p a type declaration.
	 */

	public void setMemberParent(TypeDeclaration p) {
		parent = p;
	}

	/**
	 Make parent role valid.
	 */

	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		if (fieldSpecs != null) {
			for (int i = fieldSpecs.size() - 1; i >= 0; i -= 1) {
				fieldSpecs.getFieldSpecification(i).setParent(this);
			}
		}
	}

	public FieldSpecificationMutableList getFieldSpecifications() {
		return fieldSpecs;
	}

	public void setFieldSpecifications(FieldSpecificationMutableList l) {
		fieldSpecs = l;
		makeParentRoleValid();
	}

	public VariableSpecificationList getVariables() {
		return fieldSpecs;
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
		if (typeReference != null)
			result++;
		if (fieldSpecs != null)
			result += fieldSpecs.size();
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
		if (typeReference != null) {
			if (index == 0)
				return typeReference;
			index--;
		}
		if (fieldSpecs != null) {
			len = fieldSpecs.size();
			if (len > index) {
				return fieldSpecs.getProgramElement(index);
			}
			index -= len;
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	public int getChildPositionCode(ProgramElement child) {
		// role 0 (IDX): modifier
		// role 1: type reference
		// role 2 (IDX): var specs
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
		if (typeReference == child) {
			return 1;
		}
		if (fieldSpecs != null) {
			int index = fieldSpecs.indexOf(child);
			if (index >= 0) {
				return (index << 4) | 2;
			}
		}
		return -1;
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
		if (typeReference == p) {
			TypeReference r = (TypeReference) q;
			typeReference = r;
			if (r != null) {
				r.setParent(this);
			}
			return true;
		}

		count = (fieldSpecs == null) ? 0 : fieldSpecs.size();
		for (int i = 0; i < count; i++) {
			if (fieldSpecs.getProgramElement(i) == p) {
				if (q == null) {
					fieldSpecs.remove(i);
				} else {
					FieldSpecification r = (FieldSpecification) q;
					fieldSpecs.set(i, r);
					r.setParent(this);
				}
				return true;
			}
		}
		return false;
	}

///////////////// VISIBILITY CHECK //////////////////////////////////////

	/**
	 * Test whether the declaration is final. Fields of interfaces are
	 * always final.
	 */

	public boolean isSealed() {
		return false;
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
	 * Test whether the declaration is public. Fields of interfaces
	 * are always public.
	 */

	public boolean isPublic() {
		return (getASTParent() instanceof InterfaceDeclaration)
			|| super.isPublic();
	}

	/**
	 * Test whether the declaration is static. Fields of interfaces
	 * are always static.
	 */

	public boolean isStatic() {
		return (getASTParent() instanceof InterfaceDeclaration)
			|| super.isStatic();
	}

	public void accept(SourceVisitor v) {
		v.visitFieldDeclaration(this);
	}
	/**
	 * @see recoder.csharp.declaration.MemberDeclaration#isInternal()
	 */
	public boolean isInternal() {
		return super.isInternal();
	}

	/**
	 * @see recoder.csharp.declaration.MemberDeclaration#isNew()
	 */
	public boolean isNew() {
		return super.isNew();
	}

	protected boolean isReadOnly() {
		return (parent instanceof InterfaceDeclaration || containsModifier(Readonly.class));			
	}
	
	protected boolean isVolatile() {
		return containsModifier(Volatile.class);
	}
	
}
