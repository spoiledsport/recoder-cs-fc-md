// This file is part of the RECODER library and protected by the LGPL.

package recodercs.service;

import recodercs.convenience.Format;
import recodercs.service.TreeChange;
import recodercs.convenience.*;
import recodercs.csharp.*;

/**
   An attachment change of a syntax tree.
 */
public class AttachChange extends TreeChange {

    AttachChange(ProgramElement root) {
	super(root);
    }

    public final NonTerminalProgramElement getChangeRootParent() {
	return getChangeRoot().getASTParent();
    }

    public String toString() {
	StringBuffer buf = new StringBuffer();
	if (isMinor()) {
	    buf.append("Minor ");
	}
	buf.append("Attached: ");
	if (getChangeRoot() instanceof CompilationUnit) {
	    buf.append(Format.toString("%c %u", getChangeRoot()));
	} else {
	    buf.append(Format.toString("%c %n", getChangeRoot()));
	    buf.append(Format.toString(" to %c %n in %u @%p", getChangeRootParent()));
	}
	return buf.toString();
    }    
}
