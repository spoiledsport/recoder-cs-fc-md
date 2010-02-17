// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.operator;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 Binary X or assignment.
 @author <TT>AutoDoc</TT>
 */

public class BinaryXOrAssignment extends Assignment {

    /**
     Binary X or assignment.
     */

    public BinaryXOrAssignment() {}

    /**
     Binary X or assignment.
     @param lhs an expression.
     @param rhs an expression.
     */

    public BinaryXOrAssignment(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Binary X or assignment.
     @param proto a binary X or assignment.
     */

    protected BinaryXOrAssignment(BinaryXOrAssignment proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new BinaryXOrAssignment(this);
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
        v.visitBinaryXOrAssignment(this);
    }
}
