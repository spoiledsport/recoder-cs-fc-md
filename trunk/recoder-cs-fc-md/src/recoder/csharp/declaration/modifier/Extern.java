// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration.modifier;

import recoder.*;
import recoder.csharp.*;
import recoder.csharp.declaration.*;

/**
 Extern.
 @author <TT>AutoDoc</TT>
 */

public class Extern extends Modifier {

    /**
     Extern
     */

    public Extern() {}

    /**
     Extern
     @param proto an Extern.
     */

    protected Extern(Extern proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Extern(this);
    }

    public void accept(SourceVisitor v){
        v.visitExtern(this);
    }

}
