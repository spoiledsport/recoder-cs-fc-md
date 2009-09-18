// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Addition or string concatenation assignment "+=".
 */

public class PlusAssignment extends Assignment {

    /**
     Plus assignment.
     */

    public PlusAssignment() {}

    /**
     Plus assignment.
     @param lhs an expression.
     @param rhs an expression.
     */

    public PlusAssignment(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Plus assignment.
     @param proto a plus assignment.
     */

    protected PlusAssignment(PlusAssignment proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new PlusAssignment(this);
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
        return 13;
    }

    /**
     Get notation.
     @return the int value.
     */

    public int getNotation() {
        return INFIX;
    }

    public void accept(SourceVisitor v) {
        v.visitPlusAssignment(this);
    }
}
