// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Minus assignment.
 @author <TT>AutoDoc</TT>
 */

public class MinusAssignment extends Assignment {

    /**
     Minus assignment.
     */

    public MinusAssignment() {}

    /**
     Minus assignment.
     @param lhs an expression.
     @param rhs an expression.
     */

    public MinusAssignment(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Minus assignment.
     @param proto a minus assignment.
     */

    protected MinusAssignment(MinusAssignment proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new MinusAssignment(this);
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
        v.visitMinusAssignment(this);
    }
}
