// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp;

import recodercs.*;

/**
 Statement.
 @author <TT>AutoDoc</TT>
 */

public interface Statement extends ProgramElement {

    /**
     Get statement container.
     @return the statement container.
     */
    StatementContainer getStatementContainer();

    /**
     Set statement container.
     @param c a statement container.
     */
    void setStatementContainer(StatementContainer c);
}
