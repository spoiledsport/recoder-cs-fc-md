// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Binary or assignment.
 @author <TT>AutoDoc</TT>
 */

public class BinaryOrAssignment extends Assignment {

    /**
     Binary or assignment.
     */

    public BinaryOrAssignment() {}

    /**
     Binary or assignment.
     @param lhs an expression.
     @param rhs an expression.
     */

    public BinaryOrAssignment(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Binary or assignment.
     @param proto a binary or assignment.
     */

    protected BinaryOrAssignment(BinaryOrAssignment proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new BinaryOrAssignment(this);
    }

    /**
     Get arity.
     @return the int value.
     */

    public int getArity() {
        return 2;
    }

    /**
     Get precedence.
     @return the int value.
     */

    public int getPrecedence() {
        return 13;
    }

    /**
     Get notation.
     @return the int value.
     */

    public int getNotation() {
        return INFIX;
    }

    public void accept(SourceVisitor v) {
        v.visitBinaryOrAssignment(this);
    }
}
