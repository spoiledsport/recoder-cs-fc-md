// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Not equals.
 @author <TT>AutoDoc</TT>
 */

public class NotEquals extends ComparativeOperator {

    /**
     Not equals.
     */

    public NotEquals() {}

    /**
     Not equals.
     @param lhs an expression.
     @param rhs an expression.
     */

    public NotEquals(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Not equals.
     @param proto a not equals.
     */

    protected NotEquals(NotEquals proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new NotEquals(this);
    }

    /**
     Get precedence.
     @return the int value.
     */

    public int getPrecedence() {
        return 6;
    }

    public void accept(SourceVisitor v) {
        v.visitNotEquals(this);
    }
}
