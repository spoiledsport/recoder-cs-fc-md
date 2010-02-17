// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration.modifier;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;

/**
 Virtual.
 @author <TT>AutoDoc</TT>
 */

public class Virtual extends Modifier {

    /**
     Virtual
     */

    public Virtual() {}

    /**
     Virtual
     @param proto an Virtual.
     */

    protected Virtual(Virtual proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Virtual(this);
    }

    public void accept(SourceVisitor v){
        v.visitVirtual(this);
    }

}
