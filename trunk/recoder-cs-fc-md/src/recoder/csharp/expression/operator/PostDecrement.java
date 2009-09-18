// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Post decrement.
 @author <TT>AutoDoc</TT>
 */

public class PostDecrement extends Assignment {

    /**
     Post decrement.
     */

    public PostDecrement() {}

    /**
     Post decrement.
     @param child an expression.
     */

    public PostDecrement(Expression child) {
        super(child);
        makeParentRoleValid();
    }

    /**
     Post decrement.
     @param proto a post decrement.
     */

    protected PostDecrement(PostDecrement proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new PostDecrement(this);
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
        return POSTFIX;
    }

    public void accept(SourceVisitor v) {
        v.visitPostDecrement(this);
    }
}
