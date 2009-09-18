// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.reference;

import recoder.*;
import recoder.list.*;
import recoder.csharp.*;
import recoder.csharp.declaration.*;

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
