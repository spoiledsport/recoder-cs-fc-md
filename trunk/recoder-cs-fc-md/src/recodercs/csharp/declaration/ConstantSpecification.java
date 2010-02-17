package recodercs.csharp.declaration;

import recodercs.csharp.Expression;
import recodercs.csharp.Identifier;

/**
 * @author kis
 * Represents a ConstantSpecification.
 */
public class ConstantSpecification extends VariableSpecification {

	/**
	 * Constructor for ConstantSpecification.
	 */
	public ConstantSpecification() {
		super();
	}

	/**
	 * Constructor for ConstantSpecification.
	 * @param name
	 */
	public ConstantSpecification(Identifier name) {
		super(name);
	}

	/**
	 * Constructor for ConstantSpecification.
	 * @param name
	 * @param init
	 */
	public ConstantSpecification(Identifier name, Expression init) {
		super(name, init);
	}

	/**
	 * Constructor for ConstantSpecification.
	 * @param proto
	 */
	public ConstantSpecification(VariableSpecification proto) {
		super(proto);
	}

	/**
	 * Constants are always readonly.
	 * @see recodercs.abstraction.Variable#isReadOnly()
	 */
	public boolean isReadOnly() {
		return true;
	}

}
