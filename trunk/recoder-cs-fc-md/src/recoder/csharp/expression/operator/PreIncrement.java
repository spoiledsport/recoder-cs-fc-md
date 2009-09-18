// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Pre increment.
 @author <TT>AutoDoc</TT>
 */

public class PreIncrement extends Assignment {

    /**
     Pre increment.
     */

    public PreIncrement() {}

    /**
     Pre increment.
     @param child an expression.
     */

    public PreIncrement(Expression child) {
        super(child);
        makeParentRoleValid();
    }

    /**
     Pre increment.
     @param proto a pre increment.
     */

    protected PreIncrement(PreIncrement proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new PreIncrement(this);
    }

    /**
     Get arity.
     @return the int value.
     */

    public int getArity() {
        return 1;
    }

    /**
     Get precedence.
     @return the int value.
     */

    public int getPrecedence() {
        return 1;
    }

    /**
     Get notation.
     @return the int value.
     */

    public int getNotation() {
        return PREFIX;
    }

    public void accept(SourceVisitor v) {
        v.visitPreIncrement(this);
    }
}
