package recoder.csharp.attributes.targets;

import recoder.csharp.NonTerminalProgramElement;
import recoder.csharp.SourceVisitor;
import recoder.csharp.attributes.GlobalAttributeTarget;

/**
 * @author orosz
 * Superclass of all assembly targets. Represents an assembly target.
 */
public class AssemblyTarget extends GlobalAttributeTarget {

	/**
	 * Constructor for AssemblyTarget.
	 * @param parent
	 */
	public AssemblyTarget(NonTerminalProgramElement parent) {
		super(parent);
	}

	/**
	 * Constructor for AssemblyTarget.
	 */
	public AssemblyTarget() {
		super();
	}

	/**
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitAssemblyTarget(this);
	}

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new AssemblyTarget();
	}

}
