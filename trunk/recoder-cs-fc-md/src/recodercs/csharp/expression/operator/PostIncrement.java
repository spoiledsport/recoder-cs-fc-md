// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.operator;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 Post increment.
 @author <TT>AutoDoc</TT>
 */

public class PostIncrement extends Assignment {

    /**
     Post increment.
     */

    public PostIncrement() {}

    /**
     Post increment.
     @param child an expression.
     */

    public PostIncrement(Expression child) {
        super(child);
        makeParentRoleValid();
    }

    /**
     Post increment.
     @param proto a post increment.
     */

    protected PostIncrement(PostIncrement proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new PostIncrement(this);
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
        v.visitPostIncrement(this);
    }
}
