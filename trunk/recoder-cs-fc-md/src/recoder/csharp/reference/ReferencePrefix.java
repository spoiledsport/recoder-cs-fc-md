// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.reference;

import recoder.csharp.*;

/**
 Reference prefix.
 @author <TT>AutoDoc</TT>
 */

public interface ReferencePrefix extends ProgramElement {

    /**
     @return the parent qualifier, or null if there is none.
     */
    ReferenceSuffix getReferenceSuffix();

    /**
     Set reference suffix.
     @param path a reference suffix.
     */
    void setReferenceSuffix(ReferenceSuffix path);
}
