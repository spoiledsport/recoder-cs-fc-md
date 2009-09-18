// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration;

import recoder.*;
import recoder.list.*;
import recoder.csharp.*;
import recoder.csharp.reference.*;

/**
 Throws.
 @author <TT>AutoDoc</TT>
 */

public class Throws
 extends CSharpNonTerminalProgramElement
 implements TypeReferenceContainer {

    /**
     Parent.
     */

    protected MethodDeclaration parent;

    /**
     Exceptions.
     */

    protected TypeReferenceMutableList exceptions;

    /**
     Throws.
     */

    public Throws() {}

    /**
     Throws.
     @param exception a type reference.
     */

    public Throws(TypeReference exception) {
        exceptions = new TypeReferenceArrayList(1);
        exceptions.add(exception);
        makeParentRoleValid();
    }

    /**
     Throws.
     @param list a type reference mutable list.
     */

    public Throws(TypeReferenceMutableList list) {
        exceptions = list;
        makeParentRoleValid();
    }

    /**
     Throws.
     @param proto a throws.
     */

    protected Throws(Throws proto) {
        super(proto);
        if (proto.exceptions != null) {
            exceptions = (TypeReferenceMutableList)proto.exceptions.deepClone();
        }
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Throws(this);
    }

    /**
     Make parent role valid.
     */

    public void makeParentRoleValid() {
        super.makeParentRoleValid();
        if (exceptions != null) {
            for (int i = exceptions.size() - 1; i >= 0; i -= 1) {
                exceptions.getTypeReference(i).setParent(this);
            }
        }
    }

    public SourceElement getLastElement() {
        if (exceptions == null) {
            return this;
        }
        return exceptions.getTypeReference(exceptions.size() - 1);
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
        if (exceptions != null) result += exceptions.size();
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
        if (exceptions != null) {
            return exceptions.getProgramElement(index);
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getChildPositionCode(ProgramElement child) {
        // role 0 (IDX): type references
        if (exceptions != null) {
            int index = exceptions.indexOf(child);
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
        count = (exceptions == null) ? 0 : exceptions.size();
        for (int i = 0; i < count; i++) {
            if (exceptions.getProgramElement(i) == p) {
                if (q == null) {
                    exceptions.remove(i);
                } else {
                    TypeReference r = (TypeReference)q;
                    exceptions.set(i, r);
                    r.setParent(this);
                }
                return true;
            }
        }

        return false;
    }

    /**
     Get parent.
     @return the method declaration.
     */

    public MethodDeclaration getParent() {
        return parent;
    }

    /**
     Set parent.
     @param decl a method declaration.
     */

    public void setParent(MethodDeclaration decl) {
        parent = decl;
    }

    /**
     Get exceptions.
     @return the type reference mutable list.
     */

    public TypeReferenceMutableList getExceptions() {
        return exceptions;
    }

    /**
     Set exceptions.
     @param list a type reference mutable list.
     */

    public void setExceptions(TypeReferenceMutableList list) {
        exceptions = list;
    }

    /**
     Get the number of type references in this container.
     @return the number of type references.
     */

    public int getTypeReferenceCount() {
        return (exceptions != null) ? exceptions.size() : 0;
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
        if (exceptions != null) {
            return exceptions.getTypeReference(index);
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void accept(SourceVisitor v) {
        v.visitThrows(this);
    }
}
