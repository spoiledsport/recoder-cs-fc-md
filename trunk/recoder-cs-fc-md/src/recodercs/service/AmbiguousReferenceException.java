// This file is part of the RECODER library and protected by the LGPL.

package recodercs.service;

import recodercs.ModelException;
import recodercs.abstraction.ProgramModelElement;
import recodercs.csharp.Reference;
import recodercs.list.*;

/**
   Exception indicating that a particular reference is ambiguous.
   @author AL
 */
public class AmbiguousReferenceException extends ModelException {

    private Reference reference;
    private ProgramModelElementList choices;

    /**
       Constructor without explanation text.
       @param r the ambiguous reference.
       @param choices the possible resolutions.
     */
    public AmbiguousReferenceException(Reference r, ProgramModelElementList choices) {
	reference = r;
	this.choices = choices;
    }

    /**
       Constructor without explanation text.
       @param r the ambiguous reference.
       @param choice1 one possible resolution.
       @param choice2 a second possible resolution.
     */
    public AmbiguousReferenceException(Reference r, ProgramModelElement choice1, ProgramModelElement choice2) {
	reference = r;
	ProgramModelElementMutableList list = new ProgramModelElementArrayList(2);
	list.add(choice1);
	list.add(choice2);
	this.choices = list;
    }

    /**
       Constructor with an explanation text.
       @param s an explanation.
       @param r the ambiguous reference.
       @param choices the possible resolutions.
     */
    public AmbiguousReferenceException(String s, Reference r, ProgramModelElementList choices) {
        super(s);
	reference = r;
	this.choices = choices;
    }


    /**
       Constructor with an explanation text.
       @param s an explanation.
       @param r the ambiguous reference.
       @param choice1 one possible resolution.
       @param choice2 a second possible resolution.
     */
    public AmbiguousReferenceException(String s, Reference r, ProgramModelElement choice1, ProgramModelElement choice2) {
        super(s);
	reference = r;
	ProgramModelElementMutableList list = new ProgramModelElementArrayList(2);
	list.add(choice1);
	list.add(choice2);
	this.choices = list;
    }

    /**
       Returns the reference that was found ambiguous.
     */
    public Reference getAmbiguousReference() {
	return reference;
    }

    /**
       Returns the possible choices for the ambiguous reference.
     */
    public ProgramModelElementList getPossibleResolutions() {
	return choices;
    }

}
