package recodercs.csharp.attributes;

import recodercs.csharp.CSharpNonTerminalProgramElement;
import recodercs.csharp.Expression;
import recodercs.csharp.ExpressionContainer;
import recodercs.csharp.Identifier;
import recodercs.csharp.NamedProgramElement;
import recodercs.csharp.NonTerminalProgramElement;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.SourceVisitor;

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
	 * @see recodercs.csharp.NamedProgramElement#getIdentifier()
	 */
	public Identifier getIdentifier() {
		return id;
	}

	/**
	 * @see recodercs.csharp.NamedProgramElement#setIdentifier(Identifier)
	 */
	public void setIdentifier(Identifier id) {
		this.id = id;
	}

	/**
	 * @see recodercs.NamedModelElement#getName()
	 */
	public String getName() {
		if ( id == null) return null;
		return id.getText();
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
		if (id != null) {
			if (index == 0) return id;
			index--;
		}
		return super.getChildAt(index);
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		return super.getChildCount() + ( id == null ? 0 : 1);
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
	 */
	public int getChildPositionCode(ProgramElement child) {
		// role 1 : name
		if (id == child) return 1;
		return super.getChildPositionCode(child);
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
	 */
	public boolean replaceChild(ProgramElement p, ProgramElement q) {
		if (super.replaceChild(p,q)) return true;
		if (p == id) { id = (Identifier) p; makeParentRoleValid(); return true; }
		return false;
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitNamedAttributeArgument(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new NamedAttributeArgument(this);
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#makeParentRoleValid()
	 */
	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		if (id != null) id.setParent(this);
	}

}
