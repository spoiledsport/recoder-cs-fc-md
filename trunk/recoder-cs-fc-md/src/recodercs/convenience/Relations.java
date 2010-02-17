package recodercs.convenience;

import recodercs.ModelElement;
import recodercs.convenience.Format;
//import recodercs.convenience.ModelElementLexicalOrder;
import recodercs.util.HashCode;
import recodercs.util.Order;
import recodercs.*;
import recodercs.abstraction.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;
import recodercs.csharp.expression.*;
import recodercs.csharp.expression.literal.*;
import recodercs.csharp.reference.*;
import recodercs.util.*;

public class Relations {

    private Relations() {}

    /**
       Structural equality and hash code for program elements.
       
       To be considered equal, the types of the objects must match,
       except for certain allowed combinations with 
       {@link recodercs.csharp.reference.UncollatedReferenceQualifier}s.
       <BR>
       In case of {@link recodercs.csharp.Identifier} or 
       {@link recodercs.csharp.expression.Literal}s, the textual representations
       are compared, while {@link recodercs.csharp.declaration.Modifier} are
       compared by type only. 
       <BR>
       {@link recodercs.csharp.NonTerminalProgramElement}s are compared 
       child-by-child in the given order.
       Note that the corresponding iterator reports all children without
       separation. In case that two children of the same type play different
       roles (e.g. return types of methods and thrown exceptions if there
       was no need for a method name), this behavior must be overriden.
       <BR>
       The function does not compare comments or indentation information.
       Instead, the toSource method can be used to perform a more stringent
       comparison.
       <P>
       The hash code calculated takes into acount the type of the element,
       its name if it is a {@link recodercs.csharp.NamedProgramElement} and
       the number of its children. For reasons of efficiency, no further
       recursion is performed.

    */	    	   
    public final static HashCode STRUCTURAL_EQUALITY = new HashCode() {

	    public int hashCode(Object x) {
		if (x instanceof ProgramElement) {
		    int res = getClass().hashCode();
		    if (x instanceof NonTerminalProgramElement) {
			if (x instanceof NamedProgramElement) {
			    String name = ((NamedProgramElement)x).getName();
			    if (name != null) {
				// could be anonymous (class declaration!)
				res ^= name.hashCode();
			    }
			}
			res += ((NonTerminalProgramElement)x).getChildCount();
		    }
		    return res;
		} else if (x == null) {
		    return 0;
		} else {
		    throw new IllegalArgumentException("Structural hashcodes are only defined for program elements");
		}
	    }
	    
	    public boolean equals(Object x, Object y) {
		if (x == null || y == null) {
		    return false;
		}
		if (x instanceof NonTerminalProgramElement) {
		    if (x.getClass() != y.getClass()) {
			if (x instanceof UncollatedReferenceQualifier) {
			    if (!(y instanceof ArrayLengthReference) 
				&& !(y instanceof FieldReference) 
				&& !(y instanceof NamespaceReference) 
				&& !(y instanceof TypeReference) 
				&& !(y instanceof VariableReference) ) {
				return false;
			    }
			} else if (y instanceof UncollatedReferenceQualifier) {
			    if (!(x instanceof ArrayLengthReference) 
				&& !(x instanceof FieldReference) 
				&& !(x instanceof NamespaceReference) 
				&& !(x instanceof TypeReference) 
				&& !(x instanceof VariableReference) ) {
				return false;
			    }
			} else {
			    return false;
			}
		    }
		    NonTerminalProgramElement a = (NonTerminalProgramElement)x;
		    NonTerminalProgramElement b = (NonTerminalProgramElement)y;
		    int n = a.getChildCount();
		    int m = b.getChildCount();
		    if ((a instanceof ArrayLengthReference) &&
			(b instanceof UncollatedReferenceQualifier)) {
			m -= 1;
		    }
		    if ((b instanceof ArrayLengthReference) &&
			(a instanceof UncollatedReferenceQualifier)) {
			n -= 1;
		    }
		    if (n != m) {
			return false;
		    }
		    for (int i = 0; i < n; i += 1) {
			if (!equals(a.getChildAt(i), b.getChildAt(i))) {
			    return false;
			}
		    }
		    return true;
		} else if (x instanceof TerminalProgramElement) {
		    if (x.getClass() != y.getClass()) {
			return false;
		    }
		    if (x instanceof Identifier) {
			return ((Identifier)x).getText().equals(((Identifier)y).getText());
		    }
		    if (x instanceof Literal) {
			if (x instanceof IntLiteral) {
			    return ((IntLiteral)x).getValue().equals(((IntLiteral)y).getValue());
			}
			if (x instanceof BooleanLiteral) {
			    return ((BooleanLiteral)x).getValue() == ((BooleanLiteral)y).getValue();
			}
			if (x instanceof StringLiteral) {
			    return ((StringLiteral)x).getValue().equals(((StringLiteral)y).getValue());
			}
			if (x instanceof NullLiteral) {
			    return true;
			}
			if (x instanceof CharLiteral) {
			    return ((CharLiteral)x).getValue().equals(((CharLiteral)y).getValue());
			}
			if (x instanceof DoubleLiteral) {
			    return ((DoubleLiteral)x).getValue().equals(((DoubleLiteral)y).getValue());
			}
			if (x instanceof LongLiteral) {
			    return ((LongLiteral)x).getValue().equals(((LongLiteral)y).getValue());
			}
			if (x instanceof FloatLiteral) {
			    return ((FloatLiteral)x).getValue().equals(((FloatLiteral)y).getValue());
			    // modifiers and EmptyStatement are covered by the class test
			}
		    }
		    return true;
		} else {
		    throw new IllegalArgumentException("Structural equality is only defined for program elements");
		}
	    }
	};

    public static class ModelElementLexicalOrder extends Order.CustomLexicalOrder {

	private String format;
	
	public ModelElementLexicalOrder(String format) {
	    if (format == null) {
		throw new IllegalArgumentException("Format must not be nulL");
	    }
	    this.format = format;
	}

	public boolean isComparable(Object x, Object y) {
	    return (x == null && y == null) ||
		((x instanceof ModelElement) &&
		 (y instanceof ModelElement));
	}
	
	protected String toString(Object x) {
	    return Format.toString(format, (ModelElement)x);
	}
    }
	    

    /**
       Lexical order objects comparing (short) names of named model elements.
    */
    public final static Order NAMED_MODEL_ELEMENT_ORDER = new ModelElementLexicalOrder("%n");

    
    /**
       Lexical order objects comparing full names, positions and
       units if necessary.
    */
    public final static Order UNAMBIGUOUS_ORDER = new ModelElementLexicalOrder("%u|%p|%N");
    	
}
