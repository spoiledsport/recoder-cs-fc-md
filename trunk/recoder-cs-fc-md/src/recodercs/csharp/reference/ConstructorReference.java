// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.reference;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;
import recodercs.list.*;

/**
 Constructor reference.
 @author <TT>AutoDoc</TT>
 */

public interface ConstructorReference extends MemberReference, Statement {

    /**
     Get arguments.
     @return the expression mutable list.
     */
    ExpressionMutableList getArguments();

    /**
     Set arguments.
     @param list an expression mutable list.
     */
    void setArguments(ExpressionMutableList list);
}
