// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.statement;

import recoder.*;
import recoder.csharp.*;
import recoder.list.*;

/**
 Continue.
 @author <TT>AutoDoc</TT>
 */

public class Continue extends JumpStatement {

    /**
     Continue.
     */

    public Continue() {}


    /**
     Continue.
     @param proto a continue.
     */

    protected Continue(Continue proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Continue(this);
    }

    public void accept(SourceVisitor v) {
        v.visitContinue(this);
    }
}
