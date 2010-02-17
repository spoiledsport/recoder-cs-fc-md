package recodercs.csharp.attributes.targets;

import recodercs.csharp.NonTerminalProgramElement;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.attributes.AttributeTarget;

/**
 * @author orosz
 * Represents a property attribute target.
 */
public class PropertyTarget extends AttributeTarget {

	/**
	 * Constructor for FieldTarget.
	 * @param parent
	 */
	public PropertyTarget(NonTerminalProgramElement parent) {
		super(parent);
	}

	/**
	 * Constructor for FieldTarget.
	 */
	public PropertyTarget() {
		super();
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitPropertyTarget(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new PropertyTarget();
	}

}
