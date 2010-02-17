// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.statement;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.list.*;

/**
 Do.
 @author <TT>AutoDoc</TT>
 */

public class Do extends LoopStatement {

    /**
     Do.
     */

    public Do() {}

    /**
     Do.
     @param guard an expression.
     */

    public Do(Expression guard) {
        super();
        setGuard(guard);
        makeParentRoleValid();
    }

    /**
     Do.
     @param guard an expression.
     @param body a statement.
     */

    public Do(Expression guard, Statement body) {
        super(body);
        setGuard(guard);
        makeParentRoleValid();
    }

    /**
     Do.
     @param proto a do.
     */

    protected Do(Do proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Do(this);
    }

    public SourceElement getLastElement() {
        return this;
    }


    /**
     Is exit condition.
     @return the boolean value.
     */

    public boolean isExitCondition() {
        return true;
    }

    /**
     Is checked before iteration.
     @return the boolean value.
     */

    public boolean isCheckedBeforeIteration() {
        return false;
    }

    public void accept(SourceVisitor v) {
        v.visitDo(this);
    }
}
