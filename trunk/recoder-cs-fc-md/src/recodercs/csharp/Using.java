// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp;

import recodercs.*;
import recodercs.convenience.Naming;
import recodercs.csharp.reference.*;
import recodercs.list.*;

/** TODO: Namespace name reference should be NamespaceReference instead
 * of TypeReferenceInfix.
 Using.
 @author <TT>AutoDoc</TT>
 */

public class Using extends CSharpNonTerminalProgramElement
 implements TypeReferenceContainer, NamespaceReferenceContainer {

    /**
       Multi import flag.
     */
    protected boolean isMultiImport;

    /**
     Parent.
     */
    protected NamespaceSpecificationContainer parent;

    /**
     Type reference infix.
     */
    protected TypeReferenceInfix reference;

    /**
     Using.
     */
    public Using() {}

    /**
     Using.
     @param t a type reference.
     @param multi indicates the wildcard.
     */

    public Using(TypeReference t, boolean multi) {
        setReference(t);
        setMultiImport(multi);
    }

    /**
     Using.
     @param t a package reference.
     */
    public Using(NamespaceReference t) {
        setReference(t);
        setMultiImport(true);
    }

    /**
     Using.
     @param proto an import.
     */
    protected Using(Using proto) {
        super(proto);
        if (proto.reference != null) {
            reference = (TypeReferenceInfix)proto.reference.deepClone();
        }
        isMultiImport = proto.isMultiImport;
        makeParentRoleValid();
    }

    /**
     Make parent role valid.
     */
    public void makeParentRoleValid() {
        super.makeParentRoleValid();
        if (reference instanceof TypeReference) {
            ((TypeReference)reference).setParent(this);
        } else if (reference instanceof NamespaceReference) {
            ((NamespaceReference)reference).setParent(this);
        } else if (reference instanceof UncollatedReferenceQualifier) {
            ((UncollatedReferenceQualifier)reference).setParent(this);
        }
    }

    /**
     Deep clone.
     @return the object.
     */
    public Object deepClone() {
        return new Using(this);
    }

    public SourceElement getLastElement() {
        return reference.getLastElement();
    }

    /**
       Checks if this import is a multi type import, also known as
       type-on-demand import.
       @return the kind of this import.
     */
    public boolean isMultiImport() {
        return isMultiImport;
    }

    /**
       Sets this import to be a multi type import.
       @param multi denotes the wildcard for this import.
       @exception IllegalArgumentException if the reference is a package
          and multi is <CODE>false</CODE>.
     */
    public void setMultiImport(boolean multi) {
        if (!multi && reference instanceof NamespaceReference) {
            throw new IllegalArgumentException("Namespace imports are always multi");
        }
        isMultiImport = multi;
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
        if (reference != null) result++;
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
        if (reference != null) {
            if (index == 0) return reference;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getChildPositionCode(ProgramElement child) {
        // role 0: reference
        if (child == reference) {
            return 0;
        }
        return -1;
    }

    /**
     Get parent.
     @return the compilation unit.
     */
    public NamespaceSpecificationContainer getParent() {
        return parent;
    }

    /**
     Set parent.
     @param u a compilation unit.
     */
    public void setParent(NamespaceSpecificationContainer u) {
        parent = u;
    }

    /**
     Get the number of type references in this container.
     @return the number of type references.
     */
    public int getTypeReferenceCount() {
        return (reference instanceof TypeReference) ? 1 : 0;
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
        if (reference instanceof TypeReference && index == 0) {
            return (TypeReference)reference;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
       Returns the type reference of this import, if there is one.
       @return the reference of this import statement.
    */
    public TypeReference getTypeReference() {
        return (reference instanceof TypeReference) ? (TypeReference)reference : null;
    }

    /**
       Returns the package reference of this import, if there is one.
       @return the reference of this import statement.
    */
    public NamespaceReference getNamespaceReference() {
        return (reference instanceof NamespaceReference) ? (NamespaceReference)reference : null;
    }

    /**
       Returns the reference of this import, either a
       type or a package reference.
       @return the reference of this import statement.
    */
    public TypeReferenceInfix getReference() {
        return reference;
    }

    /**
     Set reference.
     @param t a type reference infix.
     */
    public void setReference(TypeReferenceInfix t) {
        reference = t;
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
        if (reference == p) {
            TypeReferenceInfix r = (TypeReferenceInfix)q;
            reference = r;
            if (r instanceof TypeReference) {
                ((TypeReference)r).setParent(this);
            } else if (r instanceof NamespaceReference) {
                ((NamespaceReference)r).setParent(this);
                isMultiImport = true;
            }
            return true;
        }
        return false;
    }

    public void accept(SourceVisitor v) {
        v.visitUsing(this);
    }
}
