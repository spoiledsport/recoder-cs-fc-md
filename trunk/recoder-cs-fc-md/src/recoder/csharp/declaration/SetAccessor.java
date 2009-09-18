package recoder.csharp.declaration;

import recoder.csharp.SourceVisitor;

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
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitSetAccessor(this);
	}

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new SetAccessor(this);
	}

}
