// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.operator;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 Binary and.
 @author <TT>AutoDoc</TT>
 */

public class BinaryAnd extends Operator {

    /**
     Binary and.
     */

    public BinaryAnd() {}

    /**
     Binary and.
     @param lhs an expression.
     @param rhs an expression.
     */

    public BinaryAnd(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Binary and.
     @param proto a binary and.
     */

    protected BinaryAnd(BinaryAnd proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new BinaryAnd(this);
    }

    /**
     Get arity.
     @return the int value.
     */

    public final int getArity() {
        return 2;
    }

    /**
     Get precedence.
     @return the int value.
     */

    public final int getPrecedence() {
        return 7;
    }

    /**
     Get notation.
     @return the int value.
     */

    public final int getNotation() {
        return INFIX;
    }

    public void accept(SourceVisitor v) {
        v.visitBinaryAnd(this);
    }
}
