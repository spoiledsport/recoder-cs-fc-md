// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.operator;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 Equals.
 @author <TT>AutoDoc</TT>
 */

public class Equals extends ComparativeOperator {

    /**
     Equals.
     */

    public Equals() {}

    /**
     Equals.
     @param lhs an expression.
     @param rhs an expression.
     */

    public Equals(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Equals.
     @param proto an equals.
     */

    protected Equals(Equals proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Equals(this);
    }

    /**
     Get precedence.
     @return the int value.
     */

    public int getPrecedence() {
        return 6;
    }

    public void accept(SourceVisitor v) {
        v.visitEquals(this);
    }
}
