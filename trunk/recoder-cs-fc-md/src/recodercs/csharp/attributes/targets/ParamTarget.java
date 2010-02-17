package recodercs.csharp.attributes.targets;

import recodercs.csharp.NonTerminalProgramElement;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.attributes.AttributeTarget;

/**
 * @author orosz
 * Represents a parameter attribute target.
 */
public class ParamTarget extends AttributeTarget {

	/**
	 * Constructor for FieldTarget.
	 * @param parent
	 */
	public ParamTarget(NonTerminalProgramElement parent) {
		super(parent);
	}

	/**
	 * Constructor for FieldTarget.
	 */
	public ParamTarget() {
		super();
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitParamTarget(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new ParamTarget();
	}

}
