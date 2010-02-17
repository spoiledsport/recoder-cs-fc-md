// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.operator;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 Shift left assignment.
 @author <TT>AutoDoc</TT>
 */

public class ShiftLeftAssignment extends Assignment {

    /**
     Shift left assignment.
     */

    public ShiftLeftAssignment() {}

    /**
     Shift left assignment.
     @param lhs an expression.
     @param rhs an expression.
     */

    public ShiftLeftAssignment(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Shift left assignment.
     @param proto a shift left assignment.
     */

    protected ShiftLeftAssignment(ShiftLeftAssignment proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new ShiftLeftAssignment(this);
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
        v.visitShiftLeftAssignment(this);
    }
}
