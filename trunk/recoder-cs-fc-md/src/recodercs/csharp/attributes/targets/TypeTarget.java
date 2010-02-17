package recodercs.csharp.attributes.targets;

import recodercs.csharp.NonTerminalProgramElement;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.attributes.AttributeTarget;

/**
 * @author orosz
 * Represents a type attribute target.
 */
public class TypeTarget extends AttributeTarget {

	/**
	 * Constructor for FieldTarget.
	 * @param parent
	 */
	public TypeTarget(NonTerminalProgramElement parent) {
		super(parent);
	}

	/**
	 * Constructor for FieldTarget.
	 */
	public TypeTarget() {
		super();
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitTypeTarget(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new TypeTarget();
	}

}
