// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp;

import recodercs.*;

/**
   General expression. In Java, any {@link recodercs.csharp.expression.Assignment}
   is also a valid expression.
   @author AL
   @author <TT>AutoDoc</TT>
 */

public interface Expression extends ProgramElement {

    /**
     Get expression container.
     @return the expression container.
     */
    ExpressionContainer getExpressionContainer();

    /**
     Set expression container.
     @param c an expression container.
     */
    void setExpressionContainer(ExpressionContainer c);
}
