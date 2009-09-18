// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration.modifier;

import recoder.*;
import recoder.csharp.*;
import recoder.csharp.declaration.*;

/**
 Abstract.
 @author <TT>AutoDoc</TT>
 */

public class Abstract extends Modifier {

    /**
     Abstract.
     */

    public Abstract() {}

    /**
     Abstract.
     @param proto an abstract.
     */

    protected Abstract(Abstract proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Abstract(this);
    }

    public void accept(SourceVisitor v){
        v.visitAbstract(this);
    }
}
