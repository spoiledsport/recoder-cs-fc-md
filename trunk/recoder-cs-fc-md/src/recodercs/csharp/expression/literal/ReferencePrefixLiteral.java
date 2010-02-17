package recodercs.csharp.expression.literal;

import recodercs.csharp.NonTerminalProgramElement;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.expression.Literal;
import recodercs.csharp.reference.ReferencePrefix;
import recodercs.csharp.reference.ReferenceSuffix;

/**
 * @author kis
 *
 * A literal, which can hold a prefix reference. 
 * In JAVA, this literal was only the StringLiteral, but in C# basically
 * any literal can have a prefix and postfix, because of the boxing.
 */
 
public abstract class ReferencePrefixLiteral
	extends Literal
	implements ReferencePrefix {

    /**
     Reference parent.
     */

    protected ReferenceSuffix referenceParent;


	/**
	 * Constructor for ReferencePrefixLiteral.
	 */
	public ReferencePrefixLiteral() {
		super();
	}

	/**
	 * Constructor for ReferencePrefixLiteral.
	 * @param proto
	 */
	public ReferencePrefixLiteral(ReferencePrefixLiteral proto) {
		super(proto);
		if (proto.referenceParent != null) {
			this.referenceParent = (ReferenceSuffix) proto.referenceParent.deepClone();
		}

	}

	/**
	 * @see recodercs.csharp.reference.ReferencePrefix#getReferenceSuffix()
	 */
	public ReferenceSuffix getReferenceSuffix() {
		return referenceParent;
	}

	/**
	 * @see recodercs.csharp.reference.ReferencePrefix#setReferenceSuffix(ReferenceSuffix)
	 */
	public void setReferenceSuffix(ReferenceSuffix path) {
		referenceParent = path;
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public abstract Object deepClone();
	
    /**
     Get AST parent.
     @return the non terminal program element.
     */

    public NonTerminalProgramElement getASTParent() {
        if (expressionParent != null) {
            return expressionParent;
        } else {
            return referenceParent;
        }
    }


}
