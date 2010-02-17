package recodercs.csharp.declaration;

import recodercs.csharp.Expression;
import recodercs.csharp.Identifier;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.reference.TypeReference;
import recodercs.list.FieldSpecificationMutableList;
import recodercs.list.ModifierMutableList;

/**
 * Declaration of constant fields (normal fields marked with the "const" keyword.
 * 
 */

public class ConstantFieldDeclaration extends FieldDeclaration {

	/**
	 * Constructor for ConstantFieldDeclaration.
	 */
	public ConstantFieldDeclaration() {
		super();
	}

	/**
	 * Constructor for ConstantFieldDeclaration.
	 * @param typeRef
	 * @param name
	 */
	public ConstantFieldDeclaration(TypeReference typeRef, Identifier name) {
		super(typeRef, name);
	}

	/**
	 * Constructor for ConstantFieldDeclaration.
	 * @param mods
	 * @param typeRef
	 * @param name
	 * @param init
	 */
	public ConstantFieldDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		Identifier name,
		Expression init) {
		super(mods, typeRef, name, init);
	}

	/**
	 * Constructor for ConstantFieldDeclaration.
	 * @param mods
	 * @param typeRef
	 * @param vars
	 */
	public ConstantFieldDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		FieldSpecificationMutableList vars) {
		super(mods, typeRef, vars);
	}

	/**
	 * Constructor for ConstantFieldDeclaration.
	 * @param proto
	 */
	public ConstantFieldDeclaration(FieldDeclaration proto) {
		super(proto);
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitConstantFieldDeclaration(this);
	}

}
