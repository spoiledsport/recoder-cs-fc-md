// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration.modifier;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;

/**
 Static.
 @author <TT>AutoDoc</TT>
 */

public class Static extends Modifier {

    /**
     Static.
     */

    public Static() {}

    /**
     Static.
     @param proto a static.
     */

    protected Static(Static proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Static(this);
    }

    public void accept(SourceVisitor v){
        v.visitStatic(this);
    }
}
