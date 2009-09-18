// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration;

import recoder.*;
import recoder.abstraction.Enum;
import recoder.abstraction.Type;
import recoder.csharp.*;
import recoder.csharp.attributes.AttributeSection;
import recoder.csharp.attributes.AttributeSectionMutableList;
import recoder.csharp.reference.TypeReference;
import recoder.csharp.reference.TypeReferenceContainer;
import recoder.list.*;
import recoder.util.Debug;

/**
 Enum declaration.
 @author <TT>AutoDoc</TT>
 */

public class EnumDeclaration
	extends TypeDeclaration
	implements TypeReferenceContainer, Enum {

	/**
	 Extending.
	 */

	protected TypeReference base;

	/**
	 Enum declaration.
	 */

	public EnumDeclaration() {
	}

	/** Construct a new outer or member interface class. */

	public EnumDeclaration(
		ModifierMutableList modifiers,
		Identifier name,
		MemberDeclarationMutableList members) {
		super(modifiers, name);
		setMembers(members);
		makeParentRoleValid();
	}

	/**
	 Enum declaration.
	 @param proto an interface declaration.
	 */

	protected EnumDeclaration(EnumDeclaration proto) {
		super(proto);
		if (proto.attrSections != null) {
			attrSections =
				(AttributeSectionMutableList) proto.attrSections.deepClone();
		}
		if (proto.base != null) {
			base = (TypeReference) proto.base.deepClone();
		}
		makeParentRoleValid();
	}

	/**
	 Deep clone.
	 @return the object.
	 */

	public Object deepClone() {
		return new EnumDeclaration(this);
	}

	/**
	 Make parent role valid.
	 */

	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		if (base != null) {
			base.setParent(this);
		}
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
		if (name != null)
			result++;
		if (base != null)
			result++;
		if (members != null)
			result += members.size();
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
		if (name != null) {
			if (index == 0)
				return name;
			index--;
		}
		if (base != null) {
			if (index == 0)
				return base;
			index--;
		}
		if (members != null) {
			len = members.size();
			if (len > index) {
				return members.getProgramElement(index);
			}
			index -= len;
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	public int getChildPositionCode(ProgramElement child) {
		// role 0 (IDX): modifier
		// role 1: identifier
		// role 2: extends (basetype)
		// role 3: implements (no occurance in interfaces)
		// role 4 (IDX): members
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
		if (name == child)
			return 1;
		if (base == child)
			return 2;
		if (members != null) {
			int index = members.indexOf(child);
			if (index >= 0) {
				return (index << 4) | 4;
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
		if (name == p) {
			Identifier r = (Identifier) q;
			name = r;
			if (r != null) {
				r.setParent(this);
			}
			return true;
		}
		if (base == p) {
			TypeReference r = (TypeReference) q;
			base = r;
			if (r != null) {
				r.setParent(this);
			}
			return true;
		}
		count = (members == null) ? 0 : members.size();
		for (int i = 0; i < count; i++) {
			if (members.getProgramElement(i) == p) {
				if (q == null) {
					members.remove(i);
				} else {
					MemberDeclaration r = (MemberDeclaration) q;
					members.set(i, r);
					r.setMemberParent(this);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 Get base type
	 @return the base.
	 */

	public TypeReference getBaseTypeReference() {
		return base;
	}

	/**
	 Set base type.
	 @param spec the base.
	 */

	public void setBaseTypeReference(TypeReference spec) {
		base = spec;
	}

	/**
	 Enums are never abstract.
	 */

	public boolean isAbstract() {
		return false;
	}

	/**
	 * Enums are never native.
	 */

	public boolean isExtern() {
		return false;
	}

	/**
	 * Enums are never protected.
	 */

	public boolean isProtected() {
		return false;
	}

	/**
	 * Enums are never private.
	 */

	public boolean isPrivate() {
		return false;
	}

	public boolean isInterface() {
		return false;
	}

	public void accept(SourceVisitor v) {
		v.visitEnumDeclaration(this);
	}

	/**
	 Get the number of type references in this container.
	 @return the number of type references.
	 */
	public int getTypeReferenceCount() {
		return (base == null) ? 0 : 1;
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
		if (index == 0 && base != null)
			return base;
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recoder.abstraction.Enum#getType()
	 */
	public Type getBaseType() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		if (service == null) {
			Debug.error("Service not defined in TypeDeclaration " + getName());
		}
		return service.getBaseType(this);		
	}

	/**
	 * @see recoder.abstraction.Enum#getFields()
	 */
	public EnumMemberList getFields() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		if (service == null) {
			Debug.error("Service not defined in TypeDeclaration " + getName());
		}
		return service.getFields(this);		
	}

}
