// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration;

import recoder.*;
import recoder.list.*;
import recoder.csharp.*;
import recoder.csharp.attributes.AttributeSection;
import recoder.csharp.attributes.AttributeSectionMutableList;
import recoder.csharp.reference.*;

/**
 Formal parameters require a VariableSpecificationList of size() <= 1
 (size() == 0 for abstract methods) without initializer (for Java).
 */

public class ParameterDeclaration extends VariableDeclaration {

	/**
	 Parent.
	 */

	protected ParameterContainer parent;

	/**
	 Var spec.
	 */

	protected VariableSpecification varSpec;

	/**
	 Parameter declaration.
	 */

	public ParameterDeclaration() {
	}

	/**
	 Parameter declaration.
	 @param typeRef a type reference.
	 @param name an identifier.
	 */

	public ParameterDeclaration(TypeReference typeRef, Identifier name) {
		setTypeReference(typeRef);
		setVariableSpecification(
			getFactory().createVariableSpecification(name));
		makeParentRoleValid();
	}

	/**
	 Parameter declaration.
	 @param mods a modifier mutable list.
	 @param typeRef a type reference.
	 @param name an identifier.
	 */

	public ParameterDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		Identifier name) {
		setModifiers(mods);
		setTypeReference(typeRef);
		setVariableSpecification(
			getFactory().createVariableSpecification(name));
		makeParentRoleValid();
	}

	/**
	 Parameter declaration.
	 @param proto a parameter declaration.
	 */

	protected ParameterDeclaration(ParameterDeclaration proto) {
		super(proto);
		if (proto.attrSections != null) {
			attrSections =
				(AttributeSectionMutableList) proto.attrSections.deepClone();
		}
		if (proto.varSpec != null) {
			this.varSpec = (VariableSpecification) proto.varSpec.deepClone();
		}
		makeParentRoleValid();
	}

	/**
	 Deep clone.
	 @return the object.
	 */

	public Object deepClone() {
		return new ParameterDeclaration(this);
	}

	/**
	 Make parent role valid.
	 */

	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		if (varSpec != null) {
			varSpec.setParent(this);
		}
	}

	public VariableSpecification getVariableSpecification() {
		return varSpec;
	}

	public void setVariableSpecification(VariableSpecification vs) {
		varSpec = vs;
	}

	public VariableSpecificationList getVariables() {
		if (varSpec != null) return new VariableSpecificationArrayList(varSpec);
		return null;
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
		if (typeReference != null)
			result++;
		if (varSpec != null)
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
		if (typeReference != null) {
			if (index == 0)
				return typeReference;
			index--;
		}
		if (varSpec != null) {
			if (index == 0)
				return varSpec;
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
		if (varSpec == child) {
			return 2;
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
		if (varSpec == p) {
			VariableSpecification r = (VariableSpecification) q;
			varSpec = r;
			if (r != null) {
				r.setParent(this);
			}
			return true;
		}
		return false;
	}

	/**
	 Get parameter container.
	 @return the parameter container.
	 */

	public ParameterContainer getParameterContainer() {
		return parent;
	}

	/**
	 Set parameter container.
	 @param c a parameter container.
	 */

	public void setParameterContainer(ParameterContainer c) {
		parent = c;
	}

	/**
	 * Parameters are never private.
	 */

	public boolean isPrivate() {
		return false;
	}

	/**
	 * Parameters are never protected..
	 */

	public boolean isProtected() {
		return false;
	}

	/**
	 * Parameters are never "public".
	 */

	public boolean isPublic() {
		return false;
	}

	/**
	 * Parameters are never static.
	 */

	public boolean isStatic() {
		return false;
	}

	public void accept(SourceVisitor v) {
		v.visitParameterDeclaration(this);
	}

}
