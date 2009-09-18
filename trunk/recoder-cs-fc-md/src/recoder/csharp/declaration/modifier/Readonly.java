// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration.modifier;

import recoder.*;
import recoder.csharp.*;
import recoder.csharp.declaration.*;

/**
 Readonly.
 @author <TT>AutoDoc</TT>
 */

public class Readonly extends Modifier {

    /**
     Readonly
     */

    public Readonly() {}

    /**
     Readonly
     @param proto an Readonly.
     */

    protected Readonly(Readonly proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Readonly(this);
    }

    public void accept(SourceVisitor v){
        v.visitReadonly(this);
    }

}
