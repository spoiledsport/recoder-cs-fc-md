// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration;

import recoder.*;
import recoder.csharp.*;
import recoder.csharp.attributes.AttributeSection;
import recoder.csharp.attributes.AttributeSectionMutableList;
import recoder.list.*;

/**
 Interface declaration.
 @author <TT>AutoDoc</TT>
 */

public class InterfaceDeclaration extends ClassTypeDeclaration {

    /**
     Extending.
     */

    protected Extends extending;

    /**
     Interface declaration.
     */

    public InterfaceDeclaration() {}

    /** Construct a new outer or member interface class. */

    public InterfaceDeclaration(ModifierMutableList modifiers, Identifier name, Extends extended, MemberDeclarationMutableList members) {
        super(modifiers, name);
        setExtendedTypes(extended);
        setMembers(members);
        makeParentRoleValid();
    }

    /**
     Interface declaration.
     @param proto an interface declaration.
     */

    protected InterfaceDeclaration(InterfaceDeclaration proto) {
        super(proto);
        if (proto.attrSections != null) {
			attrSections = (AttributeSectionMutableList) proto.attrSections.deepClone();
		}

        if (proto.extending != null) {
            extending = (Extends)proto.extending.deepClone();
        }
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new InterfaceDeclaration(this);
    }

    /**
     Make parent role valid.
     */

    public void makeParentRoleValid() {
        super.makeParentRoleValid();
        if (extending != null) {
            extending.setParent(this);
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

        if (modifiers != null) result += modifiers.size();
        if (name != null)      result++;
        if (extending != null) result++;
        if (members != null)   result += members.size();
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
            if (index == 0) return name;
            index--;
        }
        if (extending != null) {
            if (index == 0) return extending;
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
        // role 2: extends
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
        if (name == child) return 1;
        if (extending == child) return 2;
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
                    Modifier r = (Modifier)q;
                    modifiers.set(i, r);
                    r.setParent(this);
                }
                return true;
            }
        }
        
        if (name == p) {
            Identifier r = (Identifier)q;
            name = r;
            if (r != null) {
                r.setParent(this);
            }
            return true;
        }
        if (extending == p) {
            Extends r = (Extends)q;
            extending = r;
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
                    MemberDeclaration r = (MemberDeclaration)q;
                    members.set(i, r);
                    r.setMemberParent(this);
                }
                return true;
            }
        }
        return false;
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
     Interfaces are always abstract.
     */

    public boolean isAbstract() {
        return true;
    }

    /**
     * Interfaces are never native.
     */

    public boolean isExtern() {
        return false;
    }

    /**
     * Interfaces are never protected.
     */

    public boolean isProtected() {
        return false;
    }

    /**
     * Interfaces are never private.
     */

    public boolean isPrivate() {
        return false;
    }

    public boolean isInterface() {
        return true;
    }

	/**
	 * @see recoder.abstraction.ClassType#isStruct()
	 */
	public boolean isStruct() {
		return false;
	}
    

    public void accept(SourceVisitor v) {
        v.visitInterfaceDeclaration(this);
    }

}
