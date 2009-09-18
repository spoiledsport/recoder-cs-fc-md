// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Modulo assignment.
 @author <TT>AutoDoc</TT>
 */

public class ModuloAssignment extends Assignment {

    /**
     Modulo assignment.
     */

    public ModuloAssignment() {}

    /**
     Modulo assignment.
     @param lhs an expression.
     @param rhs an expression.
     */

    public ModuloAssignment(Expression lhs, Expression rhs) {
        super(lhs, rhs);
        makeParentRoleValid();
    }

    /**
     Modulo assignment.
     @param proto a modulo assignment.
     */

    protected ModuloAssignment(ModuloAssignment proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new ModuloAssignment(this);
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
        v.visitModuloAssignment(this);
    }
}
