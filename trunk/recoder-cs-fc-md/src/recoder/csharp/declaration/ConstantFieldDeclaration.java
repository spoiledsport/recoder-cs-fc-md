package recoder.csharp.declaration;

import recoder.csharp.Expression;
import recoder.csharp.Identifier;
import recoder.csharp.SourceVisitor;
import recoder.csharp.reference.TypeReference;
import recoder.list.FieldSpecificationMutableList;
import recoder.list.ModifierMutableList;

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
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitConstantFieldDeclaration(this);
	}

}
