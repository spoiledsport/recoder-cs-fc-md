// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration.modifier;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;

/**
 Sealed.
 @author <TT>AutoDoc</TT>
 */

public class Sealed extends Modifier {

    /**
     Sealed.
     */

    public Sealed() {}

    /**
     Sealed.
     @param proto a final.
     */

    protected Sealed(Sealed proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Sealed(this);
    }

    public void accept(SourceVisitor v){
        v.visitSealed(this);
    }
}
