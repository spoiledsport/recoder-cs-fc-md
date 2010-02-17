// This file is part of the RECODER library and protected by the LGPL.

package recodercs.service;

import recodercs.convenience.Format;
import recodercs.service.AttachChange;
import recodercs.service.IllegalChangeReportException;
import recodercs.service.TreeChange;
import recodercs.util.Debug;
import recodercs.convenience.*;
import recodercs.csharp.*;
import recodercs.util.*;

/**
   A detachment change of a syntax tree.
 */
public class DetachChange extends TreeChange {

    /**
       The former parent of the root.
     */
    private NonTerminalProgramElement parent;

    /**
       The former position code in the former parent.
     */
    private int position;

    /**
       An attach change that effectively replaced this 
       This information may be redundant, but is not in general.
     */
    private AttachChange replacement;

    DetachChange(ProgramElement root, 
		 NonTerminalProgramElement parent, 
		 int position) {
	super(root);
	this.parent = parent;
	this.position = position;	    
	if (position < 0) {
	    throw new IllegalChangeReportException("Illegal position code in " + toString());
	}
    }

    DetachChange(ProgramElement root, AttachChange replacement) {
	super(root);
	Debug.asserta(replacement);
	this.replacement = replacement;
	ProgramElement rep = replacement.getChangeRoot();
	this.parent = rep.getASTParent();
	if (parent != null) {
	    // could be a compilation unit!
	    this.position = parent.getChildPositionCode(rep);
	    if (position < 0) {
		throw new IllegalChangeReportException("Illegal position code in " + replacement.toString());
	    }
	}
    }
	
    public final NonTerminalProgramElement getChangeRootParent() {
	return parent;
    }

    public final int getChangePositionCode() {
	return position;
    }

    public final AttachChange getReplacement() {
	return replacement;
    }

    // currently unused
    final void setReplacement(AttachChange ac) {
	replacement = ac;
    }

    public String toString() {
	StringBuffer buf = new StringBuffer();
	if (isMinor()) {
	    buf.append("Minor ");
	}
	buf.append("Detached: ");
	if (getChangeRoot() instanceof CompilationUnit) {
	    buf.append(Format.toString("%c %u", getChangeRoot()));
	} else {
	    buf.append(Format.toString("%c %n", getChangeRoot()));
	    buf.append(Format.toString(" from %c %n in %u @%p", getChangeRootParent()));
	    /*	    buf.append(" in role ");
		    buf.append(getChangePositionCode() & 15);
		    buf.append(" at index ");
		    buf.append(getChangePositionCode() >> 4);
	    */
	}
	return buf.toString();
    }    
}



