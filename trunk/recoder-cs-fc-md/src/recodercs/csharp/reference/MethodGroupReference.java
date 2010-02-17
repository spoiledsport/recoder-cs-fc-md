package recodercs.csharp.reference;

import recodercs.ModelException;
import recodercs.ProgramFactory;
import recodercs.csharp.CSharpNonTerminalProgramElement;
import recodercs.csharp.Expression;
import recodercs.csharp.ExpressionContainer;
import recodercs.csharp.Identifier;
import recodercs.csharp.NonTerminalProgramElement;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.SourceElement;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.SourceElement.Position;
import recodercs.csharp.statement.Return;
import recodercs.list.CommentMutableList;
import recodercs.util.Debug;

/**
 * MethodGroupReference.java
 * @author orosz
 *
 * 
 */
public class MethodGroupReference
 	extends CSharpNonTerminalProgramElement
	implements MemberReference, Expression, ExpressionContainer, ReferenceSuffix, NameReference {

	/** Reference prefix (e.g. namespace) */
	protected ReferencePrefix prefix;

	/** The container of this expression */
	protected ExpressionContainer parent;

	/** The identifier of the name reference */
	protected Identifier name;

	/**
	 * Constructor for MethodGroupReference.
	 */
	public MethodGroupReference() {}

	/**
	 * Copy constructor for MethodGroupReference.
	 */
	public MethodGroupReference(MethodGroupReference proto) {
		if (proto.prefix != null)
			prefix = (ReferencePrefix) proto.prefix.deepClone();
		if (proto.name != null)
			name = (Identifier) proto.name.deepClone();
	}

	/**
	 * @see recodercs.csharp.Expression#getExpressionContainer()
	 */
	public ExpressionContainer getExpressionContainer() {
		return parent;
	}

	/**
	 * @see recodercs.csharp.Expression#setExpressionContainer(ExpressionContainer)
	 */
	public void setExpressionContainer(ExpressionContainer c) {
		parent = c;
	}

	/**
	 * @see recodercs.csharp.ExpressionContainer#getExpressionCount()
	 */
	public int getExpressionCount() {
		return (prefix != null && prefix instanceof Expression) ? 1 : 0;
	}

	/**
	 * @see recodercs.csharp.ExpressionContainer#getExpressionAt(int)
	 */
	public Expression getExpressionAt(int index) {
		if (prefix instanceof Expression && index == 0) {
			return (Expression) prefix;
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recodercs.csharp.ProgramElement#getASTParent()
	 */
	public NonTerminalProgramElement getASTParent() {
		return parent;
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitMethodGroupReference(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new MethodGroupReference(this);
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		int result = 0;
		if (name != null) result++;
		if (prefix != null) result++;
		return result;
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
		if (prefix != null) {
			if (index == 0) return prefix;
			index--;
		}
		if (name != null) {
			if (index == 0) return name;
			index--;
		}
		throw new ArrayIndexOutOfBoundsException();		
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
	 */
	public int getChildPositionCode(ProgramElement child) {
		// role 0: prefix
		// role 1: name
		
		if (child == prefix) return 0;
		if (child == name) return 1;
		
		return -1;
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#makeParentRoleValid()
	 */
	public void makeParentRoleValid() {
		if (name != null)
			name.setParent(this);
		if (prefix != null)
			prefix.setReferenceSuffix(this);
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
	 */
	public boolean replaceChild(ProgramElement p, ProgramElement q) {
		Debug.asserta(p);
		
		if (name == p) {
			Identifier r = (Identifier) q;
			name = r;
			if (r != null)
				r.setParent(this);
			return true;
		}
		if (prefix == p) {
			ReferencePrefix r = (ReferencePrefix) q;
			prefix = r;
			if (r != null)
				r.setReferenceSuffix(this);
			return true;
		}
	
		return false;

	}

	/**
	 * @see recodercs.csharp.reference.ReferenceSuffix#getReferencePrefix()
	 */
	public ReferencePrefix getReferencePrefix() {
		return prefix;
	}

	/**
	 * @see recodercs.csharp.NamedProgramElement#getIdentifier()
	 */
	public Identifier getIdentifier() {
		return name;
	}

	/**
	 * @see recodercs.csharp.NamedProgramElement#setIdentifier(Identifier)
	 */
	public void setIdentifier(Identifier id) {
		name = id;
	}

	/**
	 * @see recodercs.NamedModelElement#getName()
	 */
	public String getName() {
		if (name != null) return name.getText();
		return "";
	}
	
	/** Sets the reference prefix. */
	public void setReferencePrefix (ReferencePrefix x) {
		prefix = x;
	}

}
