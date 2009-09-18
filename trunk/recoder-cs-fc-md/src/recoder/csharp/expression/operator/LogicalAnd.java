// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Logical and.
 @author <TT>AutoDoc</TT>
 */

public class LogicalAnd extends Operator {

    /**
     Logical and.
     */

    public LogicalAnd() {}

    /**
     Logical and.
     @param lhs an expression.
     @param rhs an expression.
     */

    public LogicalAnd(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Logical and.
     @param proto a logical and.
     */

    protected LogicalAnd(LogicalAnd proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new LogicalAnd(this);
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
        return 10;
    }

    /**
     Get notation.
     @return the int value.
     */

    public int getNotation() {
        return INFIX;
    }

    public void accept(SourceVisitor v) {
        v.visitLogicalAnd(this);
    }
}
