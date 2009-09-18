package recoder.csharp.reference;

import recoder.csharp.Identifier;

/**
 * EnumMemberReference.java
 * @author orosz
 *
 * 
 */
public class EnumMemberReference extends FieldReference {

	/**
	 * Constructor for EnumMemberReference.
	 */
	public EnumMemberReference() {
		super();
	}

	/**
	 * Constructor for EnumMemberReference.
	 * @param id
	 */
	public EnumMemberReference(Identifier id) {
		super(id);
	}

	/**
	 * Constructor for EnumMemberReference.
	 * @param prefix
	 * @param id
	 */
	public EnumMemberReference(ReferencePrefix prefix, Identifier id) {
		super(prefix, id);
	}

	/**
	 * Constructor for EnumMemberReference.
	 * @param proto
	 */
	public EnumMemberReference(EnumMemberReference proto) {
		super(proto);
	}

}
