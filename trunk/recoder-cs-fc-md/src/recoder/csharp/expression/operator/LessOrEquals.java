// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Less or equals.
 @author <TT>AutoDoc</TT>
 */

public class LessOrEquals extends ComparativeOperator {

    /**
     Less or equals.
     */

    public LessOrEquals() {}

    /**
     Less or equals.
     @param lhs an expression.
     @param rhs an expression.
     */

    public LessOrEquals(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Less or equals.
     @param proto a less or equals.
     */

    protected LessOrEquals(LessOrEquals proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new LessOrEquals(this);
    }

    /**
     Get precedence.
     @return the int value.
     */

    public int getPrecedence() {
        return 5;
    }

    public void accept(SourceVisitor v) {
        v.visitLessOrEquals(this);
    }
}
