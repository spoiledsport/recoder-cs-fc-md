// This file is part of the RECODER library and protected by the LGPL.

package recodercs.service;

import recodercs.ModelException;
import recodercs.abstraction.ProgramModelElement;
import recodercs.csharp.Declaration;

/**
   Exception indicating that a particular declaration is ambiguous.
   @author AL
 */
public class AmbiguousDeclarationException extends ModelException {

	private Declaration declaration;
	private ProgramModelElement conflictingElement;

	/**
	   Empty constructor.
	 */
	public AmbiguousDeclarationException() {}

	/**
	   Constructor without explanation text.
	   @param declaration the declaration found to be ambiguous.
	   @param conflictingElement the alternative declaration, found earlier.
	 */
	public AmbiguousDeclarationException(
		Declaration declaration,
		ProgramModelElement conflictingElement) {
		this.declaration = declaration;
		this.conflictingElement = conflictingElement;
	}

	/**
	   Constructor with an explanation text.
	   @param s an explanation.
	   @param declaration the declaration found to be ambiguous.
	   @param conflictingElement the alternative declaration, found earlier.
	 */
	public AmbiguousDeclarationException(
		String s,
		Declaration declaration,
		ProgramModelElement conflictingElement) {
		super(s);
		this.declaration = declaration;
		this.conflictingElement = conflictingElement;
	}

	/**
	   Returns the declaration that was found ambiguous.
	 */
	public Declaration getAmbiguousDeclaration() {
		return declaration;
	}

	/**
	   Returns the conflicting element.
	 */
	public ProgramModelElement getConflictingElement() {
		return conflictingElement;
	}

}
