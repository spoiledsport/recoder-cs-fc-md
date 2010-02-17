// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration.modifier;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;

/**
 Out.
 @author <TT>AutoDoc</TT>
 */

public class Out extends Modifier {

    /**
     Out
     */

    public Out() {}

    /**
     Out
     @param proto an Out.
     */

    protected Out(Out proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Out(this);
    }

    public void accept(SourceVisitor v){
        v.visitOut(this);
    }

}
