package recoder.csharp.attributes.targets;

import recoder.csharp.NonTerminalProgramElement;
import recoder.csharp.SourceVisitor;
import recoder.csharp.attributes.AttributeTarget;

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
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitTypeTarget(this);
	}

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new TypeTarget();
	}

}
