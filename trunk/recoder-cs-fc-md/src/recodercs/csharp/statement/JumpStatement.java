// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.statement;

import recodercs.*;
import recodercs.csharp.*;

/**
 Jump statement.
 @author <TT>AutoDoc</TT>
 */

public abstract class JumpStatement extends CSharpStatement {

    /**
     Jump statement.
     */

    public JumpStatement() {}

    /**
     Jump statement.
     @param proto a jump statement.
     */

    protected JumpStatement(JumpStatement proto) {
        super(proto);
    }
	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		return 0;
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
	 */
	public int getChildPositionCode(ProgramElement child) {
		return -1;
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
	 */
	public boolean replaceChild(ProgramElement p, ProgramElement q) {
		return false;
	}

}
