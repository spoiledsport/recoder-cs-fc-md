// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.operator;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 Divide assignment.
 @author <TT>AutoDoc</TT>
 */

public class DivideAssignment extends Assignment {

    /**
     Divide assignment.
     */

    public DivideAssignment() {}

    /**
     Divide assignment.
     @param lhs an expression.
     @param rhs an expression.
     */

    public DivideAssignment(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Divide assignment.
     @param proto a divide assignment.
     */

    protected DivideAssignment(DivideAssignment proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new DivideAssignment(this);
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
        v.visitDivideAssignment(this);
    }
}
