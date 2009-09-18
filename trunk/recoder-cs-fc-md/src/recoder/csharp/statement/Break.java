// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.statement;

import recoder.*;
import recoder.csharp.*;
import recoder.list.*;

/**
 Break.
 @author <TT>AutoDoc</TT>
 */

public class Break extends JumpStatement {

    /**
     Break.
     */

    public Break() {
        makeParentRoleValid();
    }


    /**
     Break.
     @param proto a break.
     */

    protected Break(Break proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Break(this);
    }

    public void accept(SourceVisitor v) {
        v.visitBreak(this);
    }
}
