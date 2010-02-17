package recodercs.csharp.declaration;

import recodercs.csharp.Identifier;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.StatementBlock;
import recodercs.csharp.declaration.modifier.VisibilityModifier;
import recodercs.list.ParameterDeclarationMutableList;

/**
 * @author kis
 *
 * TODO: Consider converting the ConstructorDeclaration to a pure 
 * implementor of the MemberDeclaration interface. Currently it is a
 * subtype of method, which does not correspond to the grammar of the 
 * language. However it makes the semantic analysis a lot easier. 
 */
public class StaticConstructorDeclaration extends ConstructorDeclaration {

	/**
	 * Constructor for StaticConstructorDeclaration.
	 */
	public StaticConstructorDeclaration() {
		super();
	}

	/**
	 * Constructor for StaticConstructorDeclaration.
	 * @param modifier
	 * @param name
	 * @param parameters
	 * @param exceptions
	 * @param body
	 */
	public StaticConstructorDeclaration(
		VisibilityModifier modifier,
		Identifier name,
		ParameterDeclarationMutableList parameters,
		Throws exceptions,
		StatementBlock body) {
		super(modifier, name, parameters, exceptions, body);
	}

	/**
	 * Constructor for StaticConstructorDeclaration.
	 * @param proto
	 */
	public StaticConstructorDeclaration(StaticConstructorDeclaration proto) {
		super(proto);
	}

	/**
	 * Static constructors are always static
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isStatic()
	 */
	public boolean isStatic() {
		return true;
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
		return super.getChildAt(index);
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		return super.getChildCount();
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitStaticConstructorDeclaration(this);
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isInternal()
	 */
	public boolean isInternal() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isOverride()
	 */
	public boolean isOverride() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isPrivate()
	 */
	public boolean isPrivate() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isProtected()
	 */
	public boolean isProtected() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isPublic()
	 */
	public boolean isPublic() {
		return true;
	}

}
