// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Shift left.
 @author <TT>AutoDoc</TT>
 */

public class ShiftLeft extends Operator {

    /**
     Shift left.
     */

    public ShiftLeft() {}

    /**
     Shift left.
     @param lhs an expression.
     @param rhs an expression.
     */

    public ShiftLeft(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Shift left.
     @param proto a shift left.
     */

    protected ShiftLeft(ShiftLeft proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new ShiftLeft(this);
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
        v.visitShiftLeft(this);
    }
}
