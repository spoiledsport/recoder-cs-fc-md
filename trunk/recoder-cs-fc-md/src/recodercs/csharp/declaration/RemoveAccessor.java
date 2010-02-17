package recodercs.csharp.declaration;

import recodercs.csharp.SourceVisitor;

/**
 * @author kis
 *
 * Accessor type for the remove type accessor in event declaration
 */
public class RemoveAccessor extends Accessor {

	/**
	 * Constructor for RemoveAccessor.
	 */
	public RemoveAccessor() {
		super();
	}

	/**
	 * Constructor for RemoveAccessor.
	 * @param proto
	 */
	protected RemoveAccessor(RemoveAccessor proto) {
		super(proto);
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitRemoveAccessor(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new RemoveAccessor(this);
	}

}
