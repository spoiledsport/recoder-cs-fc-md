// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 RefOperator.
 @author <TT>AutoDoc</TT>
 */

public class RefOperator extends Operator {

    /**
     RefOperator.
     */

    public RefOperator() {}

    /**
     RefOperator.
     @param child an expression.
     */

    public RefOperator(Expression child) {
        super(child);
        makeParentRoleValid();
    }

    /**
     RefOperator.
     @param proto a RefOperator.
     */

    protected RefOperator(RefOperator proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new RefOperator(this);
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
       v.visitRefOperator(this);
    }
}
