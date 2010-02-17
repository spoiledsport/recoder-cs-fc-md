package recodercs.csharp.attributes.targets;

import recodercs.csharp.NonTerminalProgramElement;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.attributes.AttributeTarget;

/**
 * @author orosz
 * Represents a method attribute target.
 */
public class MethodTarget extends AttributeTarget {

	/**
	 * Constructor for FieldTarget.
	 * @param parent
	 */
	public MethodTarget(NonTerminalProgramElement parent) {
		super(parent);
	}

	/**
	 * Constructor for FieldTarget.
	 */
	public MethodTarget() {
		super();
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitMethodTarget(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new MethodTarget();
	}

}
