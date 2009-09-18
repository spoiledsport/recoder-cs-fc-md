// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.reference;

import recoder.csharp.*;

/**
   Reference suffix. There are only few pure suffices, e.g.
   {@link SuperConstructorReference}.
   This interface does not extend {@link Reference}, as
   {@link recoder.csharp.expression.ParenthesizedExpression} is a qualifier
   but not a reference per se.
 */

public interface ReferenceSuffix extends NonTerminalProgramElement {

    /**
     @return the prefix in the access path, or null if there is none.
     */
    ReferencePrefix getReferencePrefix();
}
