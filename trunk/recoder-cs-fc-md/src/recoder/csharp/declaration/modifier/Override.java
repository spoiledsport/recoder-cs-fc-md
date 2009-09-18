// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration.modifier;

import recoder.*;
import recoder.csharp.*;
import recoder.csharp.declaration.*;

/**
 Override.
 @author <TT>AutoDoc</TT>
 */

public class Override extends Modifier {

    /**
     Override
     */

    public Override() {}

    /**
     Override
     @param proto an Override.
     */

    protected Override(Override proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Override(this);
    }

    public void accept(SourceVisitor v){
        v.visitOverride(this);
    }

}
