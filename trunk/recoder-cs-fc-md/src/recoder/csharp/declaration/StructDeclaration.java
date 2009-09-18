// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration;

import recoder.*;
import recoder.csharp.*;
import recoder.csharp.attributes.AttributeSection;
import recoder.csharp.attributes.AttributeSectionMutableList;
import recoder.list.*;

/**
	Struct Declaration
 */
public class StructDeclaration extends ClassTypeDeclaration implements Statement {

	/**
	 Extending.
	 */
	protected Extends extending;

	/**
	 Implementing.
	 */
	protected Implements implementing;

	/**
	 Class declaration.
	 */
	public StructDeclaration() {
	}

	/** Construct a non-anonymous class. */
	public StructDeclaration(
		ModifierMutableList modifiers,
		Identifier name,
		Extends extended,
		Implements implemented,
		MemberDeclarationMutableList members) {
		super(modifiers, name);
		setExtendedTypes(extended);
		setImplementedTypes(implemented);
		setMembers(members);
		makeParentRoleValid();
	}

	/**
	 Class declaration.
	 @param members a member declaration mutable list.
	 */
	public StructDeclaration(MemberDeclarationMutableList members) {
		setMembers(members);
		makeParentRoleValid();
	}

	/**
	 Class declaration.
	 @param proto a class declaration.
	 */
	protected StructDeclaration(StructDeclaration proto) {
		super(proto);
		if (proto.extending != null) {
			extending = (Extends) proto.extending.deepClone();
		}
		if (proto.implementing != null) {
			implementing = (Implements) proto.implementing.deepClone();
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
		return new StructDeclaration(this);
	}

	/**
	 Make parent role valid.
	 */
	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		if (extending != null) {
			extending.setParent(this);
		}
		if (implementing != null) {
			implementing.setParent(this);
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
		if (extending != null)
			result++;
		if (implementing != null)
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
		if (extending != null) {
			if (index == 0)
				return extending;
			index--;
		}
		if (implementing != null) {
			if (index == 0)
				return implementing;
			index--;
		}
		if (members != null) {
			return members.getProgramElement(index);
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	public int getChildPositionCode(ProgramElement child) {
		// role 0 (IDX): modifier
		// role 1: identifier
		// role 2: extends
		// role 3: implements (no occurance in interfaces)
		// role 4 (IDX): members
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

		if (name == child)
			return 1;
		if (extending == child)
			return 2;
		if (implementing == child)
			return 3;
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
		if (extending == p) {
			Extends r = (Extends) q;
			extending = r;
			if (r != null) {
				r.setParent(this);
			}
			return true;
		}
		if (implementing == p) {
			Implements r = (Implements) q;
			implementing = r;
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
	   Get statement container.
	   @return null, if the type is not declared locally.
	 */
	public StatementContainer getStatementContainer() {
		return (parent instanceof StatementContainer)
			? (StatementContainer) parent
			: null;
	}

	/**
	 Set statement container. Must be a {@link recoder.csharp.StatementBlock}.
	 @param p a statement container.
	 */
	public void setStatementContainer(StatementContainer p) {
		parent = (StatementBlock) p;
	}

	/**
	 Get extended types.
	 @return the extends.
	 */
	public Extends getExtendedTypes() {
		return extending;
	}

	/**
	 Set extended types.
	 @param spec an extends.
	 */
	public void setExtendedTypes(Extends spec) {
		extending = spec;
	}

	/**
	 Get implemented types.
	 @return the implements.
	 */
	public Implements getImplementedTypes() {
		return implementing;
	}

	/**
	 Set implemented types.
	 @param spec an implements.
	 */
	public void setImplementedTypes(Implements spec) {
		implementing = spec;
	}

	public boolean isInterface() {
		return false;
	}

	/**
	 * @see recoder.abstraction.ClassType#isStruct()
	 */
	public boolean isStruct() {
		return true;
	}
	

	public void accept(SourceVisitor v) {
		v.visitStructDeclaration(this);
	}

}
