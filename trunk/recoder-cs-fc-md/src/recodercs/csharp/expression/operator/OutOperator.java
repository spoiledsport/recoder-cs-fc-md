// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.operator;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 OutOperator.
 @author <TT>AutoDoc</TT>
 */

public class OutOperator extends Operator {

    /**
     OutOperator.
     */

    public OutOperator() {}

    /**
     OutOperator.
     @param child an expression.
     */

    public OutOperator(Expression child) {
        super(child);
        makeParentRoleValid();
    }

    /**
     OutOperator.
     @param proto a OutOperator.
     */

    protected OutOperator(OutOperator proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new OutOperator(this);
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

    /**
       Checks if this operator is left or right associative. Ordinary
       unary operators are right associative.
       @return <CODE>true</CODE>, if the operator is left associative,
       <CODE>false</CODE> otherwise.
     */

    public boolean isLeftAssociative() {
        return false;
    }

    public void accept(SourceVisitor v) {
       v.visitOutOperator(this);
    }
}
