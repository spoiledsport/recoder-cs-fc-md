// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Shift right.
 @author <TT>AutoDoc</TT>
 */

public class ShiftRight extends Operator {

    /**
     Shift right.
     */

    public ShiftRight() {}

    /**
     Shift right.
     @param lhs an expression.
     @param rhs an expression.
     */

    public ShiftRight(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Shift right.
     @param proto a shift right.
     */

    protected ShiftRight(ShiftRight proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new ShiftRight(this);
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
        return 4;
    }

    /**
     Get notation.
     @return the int value.
     */

    public int getNotation() {
        return INFIX;
    }

    public void accept(SourceVisitor v) {
        v.visitShiftRight(this);
    }
}
