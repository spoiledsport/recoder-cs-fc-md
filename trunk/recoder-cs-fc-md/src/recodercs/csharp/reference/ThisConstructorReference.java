// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.reference;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;
import recodercs.list.*;

/**
 This constructor reference.
 @author <TT>AutoDoc</TT>
 */

public class ThisConstructorReference extends SpecialConstructorReference {

    /**
     This constructor reference.
     */

    public ThisConstructorReference() {
        makeParentRoleValid();
    }

    /**
     This constructor reference.
     @param arguments an expression mutable list.
     */

    public ThisConstructorReference(ExpressionMutableList arguments) {
        super(arguments);
        makeParentRoleValid();
    }

    /**
     This constructor reference.
     @param proto a this constructor reference.
     */

    protected ThisConstructorReference(ThisConstructorReference proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new ThisConstructorReference(this);
    }

    public int getChildPositionCode(ProgramElement child) {
        // role 0 (IDX): parameters
        if (arguments != null) {
            int index = arguments.indexOf(child);
            if (index >= 0) {
                return (index << 4) | 0;
            }
        }
        return -1;
    }

    public void accept(SourceVisitor v) {
        v.visitThisConstructorReference(this);
    }
}
