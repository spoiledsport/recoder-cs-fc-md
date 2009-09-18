// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Greater or equals.
 @author <TT>AutoDoc</TT>
 */

public class GreaterOrEquals extends ComparativeOperator {

    /**
     Greater or equals.
     */

    public GreaterOrEquals() {}

    /**
     Greater or equals.
     @param lhs an expression.
     @param rhs an expression.
     */

    public GreaterOrEquals(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Greater or equals.
     @param proto a greater or equals.
     */

    protected GreaterOrEquals(GreaterOrEquals proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new GreaterOrEquals(this);
    }

    /**
     Get precedence.
     @return the int value.
     */

    public int getPrecedence() {
        return 5;
    }

    public void accept(SourceVisitor v) {
        v.visitGreaterOrEquals(this);
    }
}
