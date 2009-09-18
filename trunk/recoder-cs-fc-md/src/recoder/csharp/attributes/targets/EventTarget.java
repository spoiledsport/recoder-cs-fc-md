package recoder.csharp.attributes.targets;

import recoder.csharp.NonTerminalProgramElement;
import recoder.csharp.SourceVisitor;
import recoder.csharp.attributes.AttributeTarget;

/**
 * @author orosz
 * Represents an event attribute target.
 */
public class EventTarget extends AttributeTarget {

	/**
	 * Constructor for FieldTarget.
	 * @param parent
	 */
	public EventTarget(NonTerminalProgramElement parent) {
		super(parent);
	}

	/**
	 * Constructor for FieldTarget.
	 */
	public EventTarget() {
		super();
	}

	/**
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitEventTarget(this);
	}

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new EventTarget();
	}

}
