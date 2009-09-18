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
 * @author kis
 *
 * An AttributeArgument with a name.
 */
public class NamedAttributeArgument extends AttributeArgument
				 				 implements NamedProgramElement
{
	/**
	 * Constructor AttributeArgument.
	 * @param attributeParameter
	 */
	public NamedAttributeArgument(NamedAttributeArgument proto) {
	  super(proto);
	  if (proto.id != null) id = (Identifier) proto.id.deepClone();
	  makeParentRoleValid();
	}

	/**
	 * Constructor NamedAttributeArgument.
	 * @param id
	 * @param expr
	 */
	public NamedAttributeArgument(Identifier id, Expression expr) {
		super(expr);
		setIdentifier(id);
		makeParentRoleValid();
	}


	/** The identifier that may have been defined in the parameter */
	protected Identifier id;

	/** The reference to the parent in the AST. */
	private AttributeArgumentContainer parent;
	

	/**
	 * @see recoder.csharp.NamedProgramElement#getIdentifier()
	 */
	public Identifier getIdentifier() {
		return id;
	}

	/**
	 * @see recoder.csharp.NamedProgramElement#setIdentifier(Identifier)
	 */
	public void setIdentifier(Identifier id) {
		this.id = id;
	}

	/**
	 * @see recoder.NamedModelElement#getName()
	 */
	public String getName() {
		if ( id == null) return null;
		return id.getText();
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
		if (id != null) {
			if (index == 0) return id;
			index--;
		}
		return super.getChildAt(index);
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		return super.getChildCount() + ( id == null ? 0 : 1);
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
	 */
	public int getChildPositionCode(ProgramElement child) {
		// role 1 : name
		if (id == child) return 1;
		return super.getChildPositionCode(child);
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
	 */
	public boolean replaceChild(ProgramElement p, ProgramElement q) {
		if (super.replaceChild(p,q)) return true;
		if (p == id) { id = (Identifier) p; makeParentRoleValid(); return true; }
		return false;
	}

	/**
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitNamedAttributeArgument(this);
	}

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new NamedAttributeArgument(this);
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#makeParentRoleValid()
	 */
	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		if (id != null) id.setParent(this);
	}

}
