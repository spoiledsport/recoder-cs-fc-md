package recoder.csharp.declaration;

import recoder.csharp.SourceVisitor;

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
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitAddAccessor(this);
	}

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new AddAccessor(this);
	}

}
