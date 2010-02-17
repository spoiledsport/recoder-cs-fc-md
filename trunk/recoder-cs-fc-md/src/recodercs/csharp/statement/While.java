// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.statement;

import recodercs.csharp.*;
import recodercs.list.*;

/**
 While.
 @author <TT>AutoDoc</TT>
 */

public class While extends LoopStatement {

    /**
     While.
     */

    public While() {}

    /**
     While.
     @param guard an expression.
     */

    public While(Expression guard) {
        setGuard(guard);
        makeParentRoleValid();
    }

    /**
     While.
     @param guard an expression.
     @param body a statement.
     */

    public While(Expression guard, Statement body) {
        super(body);
        setGuard(guard);
        makeParentRoleValid();
    }

    /**
     While.
     @param proto a while.
     */

    protected While(While proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new While(this);
    }


    public SourceElement getLastElement() {
        return (body != null) ? body.getLastElement() : this;
    }

    /**
     Is exit condition.
     @return the boolean value.
     */

    public boolean isExitCondition() {
        return false;
    }

    /**
     Is checked before iteration.
     @return the boolean value.
     */

    public boolean isCheckedBeforeIteration() {
        return true;
    }

    public void accept(SourceVisitor v) {
        v.visitWhile(this);
    }
}
