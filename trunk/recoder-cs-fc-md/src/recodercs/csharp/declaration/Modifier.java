// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration;

import recodercs.*;
import recodercs.csharp.*;

/**
 Modifier.
 @author <TT>AutoDoc</TT>
 */

public abstract class Modifier extends CSharpProgramElement implements TerminalProgramElement {

    /**
     Parent.
     */

    protected Declaration parent;

    /**
     Modifier.
     */

    public Modifier() {}

    /**
     Modifier.
     @param proto a modifier.
     */

    protected Modifier(Modifier proto) {
        super(proto);
    }

    /**
     Get AST parent.
     @return the non terminal program element.
     */

    public NonTerminalProgramElement getASTParent() {
        return parent;
    }

    /**
     Get parent.
     @return the declaration.
     */

    public Declaration getParent() {
        return parent;
    }

    /**
     Set parent.
     @param parent a declaration.
     */

    public void setParent(Declaration parent) {
        this.parent = parent;
    }
    
}

