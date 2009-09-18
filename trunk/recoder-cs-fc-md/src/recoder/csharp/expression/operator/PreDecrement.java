// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Pre decrement.
 @author <TT>AutoDoc</TT>
 */

public class PreDecrement extends Assignment {

    /**
     Pre decrement.
     */

    public PreDecrement() {}

    /**
     Pre decrement.
     @param child an expression.
     */

    public PreDecrement(Expression child) {
        super(child);
        makeParentRoleValid();
    }

    /**
     Pre decrement.
     @param proto a pre decrement.
     */

    protected PreDecrement(PreDecrement proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new PreDecrement(this);
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
        v.visitPreDecrement(this);
    }
}
