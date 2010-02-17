package recodercs.csharp.declaration;

import recodercs.csharp.SourceVisitor;

/**
 * @author kis
 *
 * Accessor type for the get type accessor in property declaration
 */
public class GetAccessor extends Accessor {

	/**
	 * Constructor for GetAccessor.
	 */
	public GetAccessor() {
		super();
	}

	/**
	 * Constructor for GetAccessor.
	 * @param proto
	 */
	protected GetAccessor(GetAccessor proto) {
		super(proto);
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitGetAccessor(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new GetAccessor(this);
	}

}
