package recodercs.csharp.declaration;

import recodercs.csharp.SourceVisitor;

/**
 * @author kis
 *
 * Accessor type for the add type accessor in event declaration
 */
public class AddAccessor extends Accessor {

	/**
	 * Constructor for AddAccessor.
	 */
	public AddAccessor() {
		super();
	}

	/**
	 * Constructor for AddAccessor.
	 * @param proto
	 */
	protected AddAccessor(AddAccessor proto) {
		super(proto);
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitAddAccessor(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new AddAccessor(this);
	}

}
