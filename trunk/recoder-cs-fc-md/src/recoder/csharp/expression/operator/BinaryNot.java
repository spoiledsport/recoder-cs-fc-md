// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;
import java.io.*;

/**
 Binary not.
 @author <TT>AutoDoc</TT>
 */

public class BinaryNot extends Operator {

    /**
     Binary not.
     */

    public BinaryNot() {}

    /**
     Binary not.
     @param child an expression.
     */

    public BinaryNot(Expression child) {
        super(child);
        makeParentRoleValid();
    }

    /**
     Binary not.
     @param proto a binary not.
     */

    protected BinaryNot(BinaryNot proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new BinaryNot(this);
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
        v.visitBinaryNot(this);
    }
}
