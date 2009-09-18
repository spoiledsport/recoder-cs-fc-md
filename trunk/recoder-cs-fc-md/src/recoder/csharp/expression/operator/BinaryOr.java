// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Binary or.
 @author <TT>AutoDoc</TT>
 */

public class BinaryOr extends Operator {

    /**
     Binary or.
     */

    public BinaryOr() {}

    /**
     Binary or.
     @param lhs an expression.
     @param rhs an expression.
     */

    public BinaryOr(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Binary or.
     @param proto a binary or.
     */

    protected BinaryOr(BinaryOr proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new BinaryOr(this);
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
        return 9;
    }

    /**
     Get notation.
     @return the int value.
     */

    public int getNotation() {
        return INFIX;
    }

    public void accept(SourceVisitor v) {
        v.visitBinaryOr(this);
    }
}
