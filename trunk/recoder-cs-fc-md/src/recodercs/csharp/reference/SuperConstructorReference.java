// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.reference;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;
import recodercs.list.*;

/**
 Super constructor reference.
 @author <TT>AutoDoc</TT>
 */

public class SuperConstructorReference
    extends SpecialConstructorReference
    implements ReferenceSuffix {

    /**
     Access path.
     */

    protected ReferencePrefix accessPath;

    /**
     Super constructor reference.
     */

    public SuperConstructorReference() {
        makeParentRoleValid();
    }

    /**
     Super constructor reference.
     @param arguments an expression mutable list.
     */

    public SuperConstructorReference(ExpressionMutableList arguments) {
        super(arguments);
        makeParentRoleValid();
    }

    /**
     Super constructor reference.
     @param path a reference prefix.
     @param arguments an expression mutable list.
     */

    public SuperConstructorReference(ReferencePrefix path,
                                     ExpressionMutableList arguments) {
        super(arguments);
        setReferencePrefix(path);
        makeParentRoleValid();
    }

    /**
     Super constructor reference.
     @param proto a super constructor reference.
     */

    protected SuperConstructorReference(SuperConstructorReference proto) {
        super(proto);
        if (proto.accessPath != null) {
            accessPath = (ReferencePrefix)proto.accessPath.deepClone();
        }
        makeParentRoleValid();
    }

    /**
     Make parent role valid.
     */

    public void makeParentRoleValid() {
        super.makeParentRoleValid();
        if (accessPath != null) {
            accessPath.setReferenceSuffix(this);
        }
    }

    public int getChildPositionCode(ProgramElement child) {
        // role 0: prefix
        // role 1 (IDX): parameters
        if (accessPath == child) {
            return 0;
        }
        if (arguments != null) {
            int index = arguments.indexOf(child);
            if (index >= 0) {
                return (index << 4) | 1;
            }
        }
        return -1;
    }

    /**
     Get reference prefix.
     @return the reference prefix.
     */

    public ReferencePrefix getReferencePrefix() {
        return accessPath;
    }

    /**
     Set reference prefix.
     @param qualifier a reference prefix.
     */

    public void setReferencePrefix(ReferencePrefix qualifier) {
        accessPath = qualifier;
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new SuperConstructorReference(this);
    }


    public SourceElement getFirstElement() {
        return (accessPath == null) ? this : accessPath.getFirstElement();
    }

    public void accept(SourceVisitor v) {
        v.visitSuperConstructorReference(this);
    }
}
