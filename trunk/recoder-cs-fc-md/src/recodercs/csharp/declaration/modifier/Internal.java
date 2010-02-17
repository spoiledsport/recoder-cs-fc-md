// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration.modifier;

import recodercs.*;
import recodercs.csharp.*;

/**
 Internal
 @author <TT>AutoDoc</TT>
 */

public class Internal extends VisibilityModifier {

    /**
     Internal
     */

    public Internal() {}

    /**
     Internal.
     @param proto a protected.
     */

    protected Internal(Internal proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Internal(this);
    }

    public void accept(SourceVisitor v){
        v.visitInternal(this);
    }
}
