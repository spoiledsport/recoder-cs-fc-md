// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.operator;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 Binary and assignment.
 @author <TT>AutoDoc</TT>
 */

public class BinaryAndAssignment extends Assignment {

    /**
     Binary and assignment.
     */

    public BinaryAndAssignment() {}

    /**
     Binary and assignment.
     @param lhs an expression.
     @param rhs an expression.
     */

    public BinaryAndAssignment(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Binary and assignment.
     @param proto a binary and assignment.
     */

    protected BinaryAndAssignment(BinaryAndAssignment proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new BinaryAndAssignment(this);
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
        v.visitBinaryAndAssignment(this);
    }
}
