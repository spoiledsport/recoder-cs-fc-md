// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Typeof.
 @author <TT>AutoDoc</TT>
 */

public class Typeof extends TypeOperator  {

    /**
     Typeof.
     */

    public Typeof() {}

    /**
     Typeof.
     @param proto a negative.
     */

    protected Typeof(Typeof proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Typeof(this);
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

        v.visitTypeof(this);
    }
}
