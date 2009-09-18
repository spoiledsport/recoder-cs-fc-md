// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Logical not.
 @author <TT>AutoDoc</TT>
 */

public class LogicalNot extends Operator {

    /**
     Logical not.
     */

    public LogicalNot() {}

    /**
     Logical not.
     @param child an expression.
     */

    public LogicalNot(Expression child) {
        super(child);
        makeParentRoleValid();
    }

    /**
     Logical not.
     @param proto a logical not.
     */

    protected LogicalNot(LogicalNot proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new LogicalNot(this);
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
        v.visitLogicalNot(this);
    }
}
