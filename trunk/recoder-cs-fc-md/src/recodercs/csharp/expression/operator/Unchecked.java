// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.operator;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 Checked.
 @author <TT>AutoDoc</TT>
 */

public class Unchecked extends Operator {

    /**
     Checked.
     */

    public Unchecked() {}

    /**
     Checked.
     @param child an expression.
     */

    public Unchecked(Expression child) {
        super(child);
        makeParentRoleValid();
    }

    /**
     Checked.
     @param proto a negative.
     */

    protected Unchecked(Unchecked proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Unchecked(this);
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
        v.visitUnchecked(this);
    }
}
