package recoder.csharp.attributes;

import recoder.csharp.CSharpNonTerminalProgramElement;
import recoder.csharp.Expression;
import recoder.csharp.ExpressionContainer;
import recoder.csharp.Identifier;
import recoder.csharp.NamedProgramElement;
import recoder.csharp.NonTerminalProgramElement;
import recoder.csharp.ProgramElement;
import recoder.csharp.SourceVisitor;

/**
 * @author orosz
 *
 * A simple AttributeArgument. It represents an argument for an attribute usage.
 */
public class AttributeArgument extends CSharpNonTerminalProgramElement
				 				 implements 
				 				 			 ExpressionContainer
{
	/**
	 * Constructor AttributeArgument.
	 * @param attributeParameter
	 */
	public AttributeArgument(AttributeArgument proto) {
	  if (proto.ex != null) ex = (Expression) proto.ex.deepClone();
	  makeParentRoleValid();
	}

	/**
	 * Constructor AttributeArgument.
	 * @param expr
	 */
	public AttributeArgument(Expression expr) {
		ex = expr;
		makeParentRoleValid();
	}


	
	/** The expression contained in this argument. */
	protected Expression ex;
	
	/** The reference to the parent in the AST. */
	private AttributeArgumentContainer parent;
	
	/**
	 * @see recoder.csharp.ExpressionContainer#getExpressionAt(int)
	 */
	public Expression getExpressionAt(int index) {
		if (index == 0 && ex != null) return ex;
		throw new IndexOutOfBoundsException();
	}

	/**
	 * @see recoder.csharp.ExpressionContainer#getExpressionCount()
	 */
	public int getExpressionCount() {
		return ( ex == null ? 0 : 1);
	}


	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
		if (ex != null) {
			if (index == 0) return ex;
			index--;
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		return ( ex == null ? 0 : 1) ;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
	 */
	public int getChildPositionCode(ProgramElement child) {
		// role 0: expression
		if (ex == child) return 0;
		return -1;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
	 */
	public boolean replaceChild(ProgramElement p, ProgramElement q) {
		if (p == null) {
			throw new NullPointerException();
		}
		if (p == ex) 
                    { 
                        ex = (Expression) q; 
                        makeParentRoleValid(); 
                        return true; 
                    }
		return false;
	}

	/**
	 * @see recoder.csharp.ProgramElement#getASTParent()
	 */
	public NonTerminalProgramElement getASTParent() {
		return parent;
	}

	/**
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitAttributeArgument(this);
	}

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new AttributeArgument(this);
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#makeParentRoleValid()
	 */
	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		if (ex != null) ex.setExpressionContainer(this);
	}

	/**
	 * Returns the parent.
	 * @return Attribute
	 */
	public AttributeArgumentContainer getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 * @param parent The parent to set
	 */
	public void setParent(Attribute parent) {
		this.parent = parent;
	}

}
