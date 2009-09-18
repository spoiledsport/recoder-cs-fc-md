package recoder.csharp.attributes;

import recoder.csharp.CSharpProgramElement;
import recoder.csharp.NonTerminalProgramElement;
import recoder.csharp.SourceVisitor;
import recoder.csharp.TerminalProgramElement;

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
	 * @see recoder.csharp.ProgramElement#getASTParent()
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
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public abstract void accept(SourceVisitor v) ;

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
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
