// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.operator;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 Copy assignment.
 @author <TT>AutoDoc</TT>
 */

public class CopyAssignment extends Assignment {

    /**
     Copy assignment.
     */

    public CopyAssignment() {}

    /**
     Copy assignment.
     @param lhs an expression.
     @param rhs an expression.
     */

    public CopyAssignment(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Copy assignment.
     @param proto a copy assignment.
     */

    protected CopyAssignment(CopyAssignment proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new CopyAssignment(this);
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
        v.visitCopyAssignment(this);
    }
}
