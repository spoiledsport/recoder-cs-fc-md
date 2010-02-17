package recodercs.csharp.declaration;

import recodercs.csharp.SourceVisitor;

/**
 * @author kis
 *
 * Accessor type for the set type accessor in property declaration
 */
public class SetAccessor extends Accessor {

	/**
	 * Constructor for SetAccessor.
	 */
	public SetAccessor() {
		super();
	}

	/**
	 * Constructor for SetAccessor.
	 * @param proto
	 */
	protected SetAccessor(SetAccessor proto) {
		super(proto);
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitSetAccessor(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new SetAccessor(this);
	}

}
