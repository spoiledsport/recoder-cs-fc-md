// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Logical or.
 @author <TT>AutoDoc</TT>
 */

public class LogicalOr extends Operator {

    /**
     Logical or.
     */

    public LogicalOr() {}

    /**
     Logical or.
     @param lhs an expression.
     @param rhs an expression.
     */

    public LogicalOr(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Logical or.
     @param proto a logical or.
     */

    protected LogicalOr(LogicalOr proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new LogicalOr(this);
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
        return 11;
    }

    /**
     Get notation.
     @return the int value.
     */

    public int getNotation() {
        return INFIX;
    }

    public void accept(SourceVisitor v) {
        v.visitLogicalOr(this);
    }
}
