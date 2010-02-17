// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration.modifier;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;

/**
 Volatile.
 @author <TT>AutoDoc</TT>
 */

public class Volatile extends Modifier {

    /**
     Volatile.
     */

    public Volatile() {}

    /**
     Volatile.
     @param proto a volatile.
     */

    protected Volatile(Volatile proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Volatile(this);
    }

    public void accept(SourceVisitor v){
        v.visitVolatile(this);
    }
}
