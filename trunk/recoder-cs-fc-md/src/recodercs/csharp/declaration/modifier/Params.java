// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration.modifier;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;

/**
 Params - a modifier for parameter passing.
 @author <TT>AutoDoc</TT>
 */

public class Params extends Modifier {

    /**
     Native.
     */

    public Params() {}

    /**
     Native.
     @param proto a native.
     */

    protected Params(Params proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Params(this);
    }

    public void accept(SourceVisitor v){
        v.visitParams(this);
    }

}
