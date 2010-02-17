// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.operator;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.expression.*;
import recodercs.list.*;

import java.io.*;

/**
   The ternary C-style conditional operator.
 */
public class Conditional extends Operator {

    /**
     Conditional.
     */

    public Conditional() {}

    /**
     Conditional.
     @param guard an expression.
     @param thenExpr an expression.
     @param elseExpr an expression.
     */

    public Conditional(Expression guard, Expression thenExpr, Expression elseExpr) {
        children = new ExpressionArrayList(getArity());
        children.add(guard);
        children.add(thenExpr);
        children.add(elseExpr);
        super.makeParentRoleValid();
    }

    /**
     Conditional.
     @param proto a conditional.
     */

    protected Conditional(Conditional proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Conditional(this);
    }

    /**
     Get arity.
     @return the int value.
     */

    public int getArity() {
        return 3;
    }

    /**
     Get precedence.
     @return the int value.
     */

    public int getPrecedence() {
        return 12;
    }

    /**
     Get notation.
     @return the int value.
     */

    public int getNotation() {
        return INFIX;
    }

    /**
       Checks if this operator is left or right associative. Conditionals
       are right associative.
       @return <CODE>true</CODE>, if the operator is left associative,
       <CODE>false</CODE> otherwise.
     */

    public boolean isLeftAssociative() {
        return false;
    }

    public void accept(SourceVisitor v) {
        v.visitConditional(this);
    }
}
