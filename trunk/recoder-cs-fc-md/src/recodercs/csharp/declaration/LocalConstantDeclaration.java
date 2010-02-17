package recodercs.csharp.declaration;

import recodercs.csharp.Expression;
import recodercs.csharp.Identifier;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.reference.TypeReference;
import recodercs.list.ModifierMutableList;
import recodercs.list.VariableSpecificationMutableList;

/**
 * @author kis
 * Represents a local constant declaration.
 */
public class LocalConstantDeclaration extends LocalVariableDeclaration {

	/**
	 * Constructor for LocalConstantDeclaration.
	 */
	public LocalConstantDeclaration() {
		super();
	}

	/**
	 * Constructor for LocalConstantDeclaration.
	 * @param typeRef
	 * @param name
	 */
	public LocalConstantDeclaration(TypeReference typeRef, Identifier name) {
		super(typeRef, name);
	}

	/**
	 * Constructor for LocalConstantDeclaration.
	 * @param mods
	 * @param typeRef
	 * @param vars
	 */
	public LocalConstantDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		VariableSpecificationMutableList vars) {
		super(mods, typeRef, vars);
	}

	/**
	 * Constructor for LocalConstantDeclaration.
	 * @param mods
	 * @param typeRef
	 * @param name
	 * @param init
	 */
	public LocalConstantDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		Identifier name,
		Expression init) {
		super(mods, typeRef, name, init);
	}

	/**
	 * Constructor for LocalConstantDeclaration.
	 * @param proto
	 */
	public LocalConstantDeclaration(LocalVariableDeclaration proto) {
		super(proto);
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitLocalConstantDeclaration(this);
	}

}
