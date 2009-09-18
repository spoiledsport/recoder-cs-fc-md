// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Greater than.
 @author <TT>AutoDoc</TT>
 */

public class GreaterThan extends ComparativeOperator {

    /**
     Greater than.
     */

    public GreaterThan() {}

    /**
     Greater than.
     @param lhs an expression.
     @param rhs an expression.
     */

    public GreaterThan(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Greater than.
     @param proto a greater than.
     */

    protected GreaterThan(GreaterThan proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new GreaterThan(this);
    }

    /**
     Get precedence.
     @return the int value.
     */

    public int getPrecedence() {
        return 5;
    }

    public void accept(SourceVisitor v) {
        v.visitGreaterThan(this);
    }
}
