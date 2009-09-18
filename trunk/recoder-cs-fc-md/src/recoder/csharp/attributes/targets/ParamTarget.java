package recoder.csharp.attributes.targets;

import recoder.csharp.NonTerminalProgramElement;
import recoder.csharp.SourceVisitor;
import recoder.csharp.attributes.AttributeTarget;

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
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitParamTarget(this);
	}

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new ParamTarget();
	}

}
