package recoder.csharp.declaration;

import recoder.csharp.Expression;
import recoder.csharp.Identifier;
import recoder.csharp.SourceVisitor;
import recoder.csharp.reference.TypeReference;
import recoder.list.ModifierMutableList;
import recoder.list.VariableSpecificationMutableList;

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
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitLocalConstantDeclaration(this);
	}

}
