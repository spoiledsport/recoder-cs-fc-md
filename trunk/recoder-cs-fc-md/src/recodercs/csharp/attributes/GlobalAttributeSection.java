package recodercs.csharp.attributes;

import recodercs.util.Debug;



/**
 * @author orosz
 * Represents a global attribute section in a C# compilation unit.
 */
public class GlobalAttributeSection extends AttributeSection {

	/** Constructor with a GlobalTarget. */
	public GlobalAttributeSection(GlobalAttributeTarget target) {
		super (target);
	}	

	/**
	 * Constructor for GlobalAttributeSection.
	 * @param proto
	 */
	public GlobalAttributeSection(GlobalAttributeSection proto) {
		super(proto);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new GlobalAttributeSection(this);
	}

	/**
	 * Sets the target. Has been redefined, so that assertion fails, if
	 * somebody tries to insert a non-global attribute target.
	 * 
	 * @see recodercs.csharp.attributes.AttributeSection#setTarget(AttributeTarget)
	 */
	public void setTarget(AttributeTarget target) {
		Debug.asserta(target instanceof GlobalAttributeTarget);
		super.setTarget(target);
	}

}
