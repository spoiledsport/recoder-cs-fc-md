// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.statement;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.list.*;

/**
 Throw.
 @author <TT>AutoDoc</TT>
 */

public class Throw extends ExpressionJumpStatement {

    /**
     Throw.
     */

    public Throw() {}

    /**
     Throw.
     @param expr an expression.
     */

    public Throw(Expression expr) {
        super(expr);
        if (expr == null) {
            throw new IllegalArgumentException("Throw requires one argument");
        }
        makeParentRoleValid();
    }

    /**
     Throw.
     @param proto a throw.
     */

    protected Throw(Throw proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Throw(this);
    }

    public void accept(SourceVisitor v) {
        v.visitThrow(this);
    }
}
