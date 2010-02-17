// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.operator;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 Times assignment.
 @author <TT>AutoDoc</TT>
 */

public class TimesAssignment extends Assignment {

    /**
     Times assignment.
     */

    public TimesAssignment() {}

    /**
     Times assignment.
     @param lhs an expression.
     @param rhs an expression.
     */

    public TimesAssignment(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Times assignment.
     @param proto a times assignment.
     */

    protected TimesAssignment(TimesAssignment proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new TimesAssignment(this);
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
        v.visitTimesAssignment(this);
    }
}
