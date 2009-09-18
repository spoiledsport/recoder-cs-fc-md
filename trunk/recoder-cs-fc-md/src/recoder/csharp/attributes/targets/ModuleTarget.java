package recoder.csharp.attributes.targets;

import recoder.csharp.NonTerminalProgramElement;
import recoder.csharp.SourceVisitor;
import recoder.csharp.attributes.AttributeTarget;

/**
 * @author orosz
 * Represents a module attribute target.
 */
public class ModuleTarget extends AttributeTarget {

	/**
	 * Constructor for FieldTarget.
	 * @param parent
	 */
	public ModuleTarget(NonTerminalProgramElement parent) {
		super(parent);
	}

	/**
	 * Constructor for FieldTarget.
	 */
	public ModuleTarget() {
		super();
	}

	/**
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitModuleTarget(this);
	}

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new ModuleTarget();
	}

}
