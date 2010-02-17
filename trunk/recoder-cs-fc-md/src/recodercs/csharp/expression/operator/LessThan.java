// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.operator;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 Less than.
 @author <TT>AutoDoc</TT>
 */

public class LessThan extends ComparativeOperator {

    /**
     Less than.
     */

    public LessThan() {}

    /**
     Less than.
     @param lhs an expression.
     @param rhs an expression.
     */

    public LessThan(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Less than.
     @param proto a less than.
     */

    protected LessThan(LessThan proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new LessThan(this);
    }

    /**
     Get precedence.
     @return the int value.
     */

    public int getPrecedence() {
        return 5;
    }

    public void accept(SourceVisitor v) {
        v.visitLessThan(this);
    }
}
