package recodercs.csharp.attributes;

import recodercs.csharp.NonTerminalProgramElement;

/**
 * @author orosz
 *
 * An abstract class for the global attribute targets.
 */
public abstract class GlobalAttributeTarget extends AttributeTarget {

	/**
	 * Constructor for GlobalAttributeTarget.
	 * @param parent
	 */
	public GlobalAttributeTarget(NonTerminalProgramElement parent) {
		super(parent);
	}

	/**
	 * Constructor for GlobalAttributeTarget.
	 */
	public GlobalAttributeTarget() {
		super();
	}

}
