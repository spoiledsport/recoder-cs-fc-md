package recoder.csharp.declaration;

import recoder.csharp.SourceVisitor;

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
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitRemoveAccessor(this);
	}

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new RemoveAccessor(this);
	}

}
