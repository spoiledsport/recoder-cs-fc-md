// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.reference.*;
import recodercs.list.*;

/**
 Extends.
 @author <TT>AutoDoc</TT>
 */

public class Extends extends InheritanceSpecification {

    /**
     Extends.
     */

    public Extends() {}

    /**
     Extends.
     @param supertype a type reference.
     */

    public Extends(TypeReference supertype) {
        super(supertype);
        makeParentRoleValid();
    }

    /**
     Extends.
     @param list a type reference mutable list.
     */

    public Extends(TypeReferenceMutableList list) {
        super(list);
        makeParentRoleValid();
    }

    /**
     Extends.
     @param proto an extends.
     */

    protected Extends(Extends proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Extends(this);
    }

    public void accept(SourceVisitor v) {
        v.visitExtends(this);
    }
}
