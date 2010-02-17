package recodercs.csharp.attributes.targets;

import recodercs.csharp.NonTerminalProgramElement;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.attributes.GlobalAttributeTarget;

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
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitAssemblyTarget(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new AssemblyTarget();
	}

}
