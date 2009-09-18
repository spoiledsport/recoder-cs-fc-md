// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.reference;

import recoder.*;
import recoder.csharp.*;
import recoder.csharp.expression.*;
import recoder.csharp.declaration.*;
import recoder.list.*;

/**
 Variable reference.
 @author <TT>AutoDoc</TT>
 */

public class VariableReference
 extends CSharpNonTerminalProgramElement
 implements NameReference, Expression, ReferencePrefix {

    /**
     Access parent.
     */
    protected ReferenceSuffix accessParent;

    /**
     Parent.
     */
    protected ExpressionContainer parent;

    /**
     Name.
     */
    protected Identifier name;

    /**
     Variable reference.
     */
    public VariableReference() {}

    /**
     Variable reference.
     @param id an identifier.
     */

    public VariableReference(Identifier id) {
        setIdentifier(id);
        makeParentRoleValid();
    }

    /**
     Variable reference.
     @param proto a variable reference.
     */
    protected VariableReference(VariableReference proto) {
        super(proto);
        if (proto.name != null) {
            name = (Identifier)proto.name.deepClone();
        }
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */
    public Object deepClone() {
        return new VariableReference(this);
    }

    /**
     Make parent role valid.
     */
    public void makeParentRoleValid() {
        super.makeParentRoleValid();
        if (name != null) {
            name.setParent(this);
        }
    }

    /**
     Get AST parent.
     @return the non terminal program element.
     */
    public NonTerminalProgramElement getASTParent() {
        if (accessParent != null) {
            return accessParent;
        } else {
            return parent;
        }
    }

    /**
     Returns the number of children of this node.
     @return an int giving the number of children of this node
    */
    public int getChildCount() {
        return (name != null) ? 1 : 0;
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
        if (name != null) {
            if (index == 0) return name;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getChildPositionCode(ProgramElement child) {
        // role 0: name
        if (name == child) {
            return 0;
        }
        return -1;
    }


    /**
     Get reference suffix.
     @return the reference suffix.
     */
    public ReferenceSuffix getReferenceSuffix() {
        return accessParent;
    }

    /**
     Set reference suffix.
     @param path a reference suffix.
     */
    public void setReferenceSuffix(ReferenceSuffix path) {
        this.accessParent = path;
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
        if (name == p) {
            Identifier r = (Identifier)q;
            name = r;
            if (r != null) {
                r.setParent(this);
            }
            return true;
        }
        return false;
    }

    /**
     Get expression container.
     @return the expression container.
     */
    public ExpressionContainer getExpressionContainer() {
        return parent;
    }

    /**
     Set expression container.
     @param c an expression container.
     */
    public void setExpressionContainer(ExpressionContainer c) {
        parent = c;
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

    public SourceElement getFirstElement() {
        return name;
    }

    public void accept(SourceVisitor v) {
        v.visitVariableReference(this);
    }
}
