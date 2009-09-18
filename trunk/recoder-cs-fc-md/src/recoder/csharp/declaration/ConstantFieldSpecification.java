package recoder.csharp.declaration;

import recoder.csharp.Expression;
import recoder.csharp.Identifier;

/**
 * @author kis
 *
 * Constant fileld specification. Constant fields are fields, preserved
 * by the "const" keyword. These fields must always have an initializer.
 * 
 */
public class ConstantFieldSpecification extends FieldSpecification {

	/**
	 * Constructor for ConstantFieldSpecification.
	 */
	public ConstantFieldSpecification() {
		super();
	}

	/**
	 * Constructor for ConstantFieldSpecification.
	 * @param name
	 */
	public ConstantFieldSpecification(Identifier name) {
		super(name);
	}

	/**
	 * Constructor for ConstantFieldSpecification.
	 * @param name
	 * @param init
	 */
	public ConstantFieldSpecification(Identifier name, Expression init) {
		super(name, init);
	}

	/**
	 * Constructor for ConstantFieldSpecification.
	 * @param proto
	 */
	public ConstantFieldSpecification(FieldSpecification proto) {
		super(proto);
	}

	/**
	 * @see recoder.abstraction.Variable#isReadOnly()
	 */
	public boolean isReadOnly() {
		return true;
	}

}
