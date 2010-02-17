// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration.modifier;

import recodercs.*;
import recodercs.csharp.*;

/**
 Public.
 @author <TT>AutoDoc</TT>
 */

public class Public extends VisibilityModifier {

    /**
     Public.
     */

    public Public() {}

    /**
     Public.
     @param proto a public.
     */

    protected Public(Public proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Public(this);
    }

    public void accept(SourceVisitor v){
        v.visitPublic(this);
    }
}
