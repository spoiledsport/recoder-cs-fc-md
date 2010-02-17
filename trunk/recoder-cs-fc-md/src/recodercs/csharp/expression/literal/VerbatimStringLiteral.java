package recodercs.csharp.expression.literal;

import recodercs.csharp.SourceVisitor;

/**
 * @author kisg
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class VerbatimStringLiteral extends StringLiteral {

	/**
	 * Constructor for VerbatimStringLiteral.
	 */
	public VerbatimStringLiteral() {
		super();
	}

	/**
	 * Constructor for VerbatimStringLiteral.
	 * @param value
	 */
	public VerbatimStringLiteral(String value) {
		super(value);
	}

	/**
	 * Constructor for VerbatimStringLiteral.
	 * @param proto
	 */
	public VerbatimStringLiteral(VerbatimStringLiteral proto) {
		super(proto);
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitVerbatimStringLiteral(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new VerbatimStringLiteral(this);
	}

    /**
     Set value.
     @param str a string value.
     */

    public void setValue(String str) {
        if (!str.startsWith("@\"") || !str.endsWith("\"")) {
            throw new IllegalArgumentException("Bad string literal " + str);
        }
        this.value = str.intern();
    }


}
