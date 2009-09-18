// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.statement;

import recoder.*;
import recoder.csharp.*;
import recoder.list.*;

/**
 Return.
 @author <TT>AutoDoc</TT>
 */

public class Return extends ExpressionJumpStatement {

    /**
     Return.
     */

    public Return() {}

    /**
     Return.
     @param expr an expression.
     */

    public Return(Expression expr) {
        super(expr);
        makeParentRoleValid();
    }

    /**
     Return.
     @param proto a return.
     */

    protected Return(Return proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Return(this);
    }

    public void accept(SourceVisitor v) {
        v.visitReturn(this);
    }
}
