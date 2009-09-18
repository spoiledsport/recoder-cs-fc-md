package recoder.csharp.attributes.targets;

import recoder.csharp.NonTerminalProgramElement;
import recoder.csharp.SourceVisitor;
import recoder.csharp.attributes.AttributeTarget;

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
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitPropertyTarget(this);
	}

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new PropertyTarget();
	}

}
