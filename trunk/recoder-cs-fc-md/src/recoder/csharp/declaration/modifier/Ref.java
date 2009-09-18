// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration.modifier;

import recoder.*;
import recoder.csharp.*;
import recoder.csharp.declaration.*;

/**
 Strict fp.
 @author <TT>AutoDoc</TT>
 */

public class Ref extends Modifier {

    /**
     Strict fp.
     */

    public Ref() {}

    /**
     Strict fp.
     @param proto a strict fp.
     */

    protected Ref(Ref proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Ref(this);
    }

    public void accept(SourceVisitor v){
        v.visitRef(this);
    }

}
