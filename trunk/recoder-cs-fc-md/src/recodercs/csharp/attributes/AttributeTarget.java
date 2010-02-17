package recodercs.csharp.attributes;

import recodercs.csharp.CSharpProgramElement;
import recodercs.csharp.NonTerminalProgramElement;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.TerminalProgramElement;

/**
 * @author orosz
 *
 * Abstract base class for attribute targets (optional 
 */
public abstract class AttributeTarget
	extends CSharpProgramElement
	implements TerminalProgramElement {
		
	/** The reference to the AST parent. */
	protected NonTerminalProgramElement parent;

	/** Returns the parent in the AST.
	 * 
	 * @see recodercs.csharp.ProgramElement#getASTParent()
	 */
	public NonTerminalProgramElement getASTParent() {
		return parent;
	}

	/** Creates a new AttributeTarget with the given parent. */
	public AttributeTarget(NonTerminalProgramElement parent) {
		this.parent = parent;
	}

	/** Default constructor (does nothing). */
	public AttributeTarget() {}
	 
	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public abstract void accept(SourceVisitor v) ;

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public abstract Object deepClone();
	
	/**
	 * Sets the parent.
	 * @param parent The parent to set
	 */
	public void setParent(NonTerminalProgramElement parent) {
		this.parent = parent;
	}

}
