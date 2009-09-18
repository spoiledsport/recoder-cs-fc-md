// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration;

import recoder.*;
import recoder.list.*;
import recoder.csharp.*;
import recoder.csharp.reference.*;

/**
 Implements.
 @author <TT>AutoDoc</TT>
 */

public class Implements extends InheritanceSpecification {

    /**
     Implements.
     */

    public Implements() {}

    /**
     Implements.
     @param supertype a type reference.
     */

    public Implements(TypeReference supertype) {
        super(supertype);
        makeParentRoleValid();
    }

    /**
     Implements.
     @param list a type reference mutable list.
     */

    public Implements(TypeReferenceMutableList list) {
        super(list);
        makeParentRoleValid();
    }

    /**
     Implements.
     @param proto an implements.
     */

    protected Implements(Implements proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Implements(this);
    }

    public void accept(SourceVisitor v) {
        v.visitImplements(this);
    }
}
