// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.reference.*;
import recodercs.list.*;

/**
 Inheritance specification.
 @author <TT>AutoDoc</TT>
 */

public abstract class InheritanceSpecification
 extends CSharpNonTerminalProgramElement
 implements TypeReferenceContainer {

    /**
     Parent.
     */

    protected ClassTypeDeclaration parent;

    /**
     Supertypes.
     */

    protected TypeReferenceMutableList supertypes;

    /**
     Inheritance specification.
     */

    public InheritanceSpecification() {}

    /**
     Inheritance specification.
     @param supertype a type reference.
     */

    public InheritanceSpecification(TypeReference supertype) {
        supertypes = new TypeReferenceArrayList(1);
        supertypes.add(supertype);
    }

    /**
     Inheritance specification.
     @param supertypes a type reference mutable list.
     */

    public InheritanceSpecification(TypeReferenceMutableList supertypes) {
        this.supertypes = supertypes;
    }

    /**
     Inheritance specification.
     @param proto an inheritance specification.
     */

    protected InheritanceSpecification(InheritanceSpecification proto) {
        super(proto);
        if (proto.supertypes != null) {
            supertypes = (TypeReferenceMutableList)proto.supertypes.deepClone();
        }
    }

    /**
     Get AST parent.
     @return the non terminal program element.
     */

    public NonTerminalProgramElement getASTParent() {
        return parent;
    }

    /**
     Make parent role valid.
     */

    public void makeParentRoleValid() {
        super.makeParentRoleValid();
        if (supertypes != null) {
            for (int i = supertypes.size() - 1; i >= 0; i -= 1) {
                supertypes.getTypeReference(i).setParent(this);
            }
        }
    }

    public SourceElement getLastElement() {
        if (supertypes == null) {
            return this;
        }
        return supertypes.getTypeReference(supertypes.size() - 1);
    }


    /**
     Returns the number of children of this node.
     @return an int giving the number of children of this node
    */

    public int getChildCount() {
        int result = 0;
        if (supertypes != null) result += supertypes.size();
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
        if (supertypes != null) {
            return supertypes.getProgramElement(index);
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getChildPositionCode(ProgramElement child) {
        // role 0 (IDX): types
        if (supertypes != null) {
            int index = supertypes.indexOf(child);
            if (index >= 0) {
                return (index << 4) | 0;
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
        count = (supertypes == null) ? 0 : supertypes.size();
        for (int i = 0; i < count; i++) {
            if (supertypes.getProgramElement(i) == p) {
                if (q == null) {
                    supertypes.remove(i);
                } else {
                    TypeReference r = (TypeReference)q;
                    supertypes.set(i, r);
                    r.setParent(this);
                }
                return true;
            }
        }
        return false;
    }

    /**
     Get parent.
     @return the type declaration.
     */

    public ClassTypeDeclaration getParent() {
        return parent;
    }

    /**
     Set parent.
     @param decl a type declaration.
     */

    public void setParent(ClassTypeDeclaration decl) {
        parent = decl;
    }

    /**
     Get supertypes.
     @return the type reference mutable list.
     */

    public TypeReferenceMutableList getSupertypes() {
        return supertypes;
    }

    /**
     Set supertypes.
     @param list a type reference mutable list.
     */

    public void setSupertypes(TypeReferenceMutableList list) {
        supertypes = list;
    }


    /**
     Get the number of type references in this container.
     @return the number of type references.
     */

    public int getTypeReferenceCount() {
        return (supertypes != null) ? supertypes.size() : 0;
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
        if (supertypes != null) {
            return supertypes.getTypeReference(index);
        }
        throw new ArrayIndexOutOfBoundsException();
    }
}
