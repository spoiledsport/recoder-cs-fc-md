// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.operator;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 Binary X or.
 @author <TT>AutoDoc</TT>
 */

public class BinaryXOr extends Operator {

    /**
     Binary X or.
     */

    public BinaryXOr() {}

    /**
     Binary X or.
     @param lhs an expression.
     @param rhs an expression.
     */

    public BinaryXOr(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Binary X or.
     @param proto a binary X or.
     */

    protected BinaryXOr(BinaryXOr proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new BinaryXOr(this);
    }

    /**
     Get arity.
     @return the int value.
     */

    public int getArity() {
        return 2;
    }

    /**
     Get precedence.
     @return the int value.
     */

    public int getPrecedence() {
        return 8;
    }

    /**
     Get notation.
     @return the int value.
     */

    public int getNotation() {
        return INFIX;
    }

    public void accept(SourceVisitor v) {
        v.visitBinaryXOr(this);
    }
}
