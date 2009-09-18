// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.statement;

import recoder.*;
import recoder.csharp.*;
import recoder.list.*;

/**
 Goto.
 @author <TT>AutoDoc</TT>
 The name field contains one of:
 	-<IDENTIFIER>
 	-case (The caseExpression field contains the Expression)
 	-default
 */

public class Goto extends LabelJumpStatement 
implements ExpressionContainer {

	private Expression caseExpression;


    /**
     Goto.
     */

    public Goto() {}

    /**
     Goto.
     @param label an identifier.
     */

    public Goto(Identifier label) {
        super(label);
        makeParentRoleValid();
    }

    /**
     Goto.
     @param proto a Goto.
     */

    protected Goto(Goto proto) {
        super(proto);
        if (proto.caseExpression != null) {
        	caseExpression = (Expression) proto.caseExpression.deepClone();
        }
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Goto(this);
    }

    public void accept(SourceVisitor v) {
        v.visitGoto(this);
    }
	
	/**
	 * Returns the caseExpression.
	 * @return Expression
	 */
	public Expression getCaseExpression() {
		return caseExpression;
	}

	/**
	 * Sets the caseExpression.
	 * @param caseExpression The caseExpression to set
	 */
	public void setCaseExpression(Expression caseExpression) {
		this.caseExpression = caseExpression;
		makeParentRoleValid();
	}

   /**
     Returns the number of children of this node.
     @return an int giving the number of children of this node
    */

    public int getChildCount() {
		if (name == null) 
			return 0;
		else if (caseExpression == null) 
			return 1;
		else
			return 2;
    }

    /**
     Returns the child at the specified index in this node's "virtual"
     child array
     @param index an index into this node's "virtual" child array
     @return the program element at the given position
     @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
                of bounds
    */

    public ProgramElement getChildAt(int index) {
        if (name != null) {
            if (index == 0) return name;
        	if (caseExpression != null) {
        		if (index == 1) return caseExpression;
        	}
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getChildPositionCode(ProgramElement child) {
        // role 0: label
        // role 1: 
        if (name == child) {
            return 0;
        }
        if (caseExpression == child) {
        	return 1;
        }
        return -1;
    }


    /**
     * Replace a single child in the current node.
     * The child to replace is matched by identity and hence must be known
     * exactly. The replacement element can be null - in that case, the child
     * is effectively removed.
     * The parent role of the new child is validated, while the
     * parent link of the replaced child is left untouched.
     * @param p the old child.
     * @param p the new child.
     * @return true if a replacement has occured, false otherwise.
     * @exception ClassCastException if the new child cannot take over
     *            the role of the old one.
     */

    public boolean replaceChild(ProgramElement p, ProgramElement q) {
        if (p == null) {
            throw new NullPointerException();
        }
        if (name == p) {
            Identifier r = (Identifier)q;
            name = r;
            if (r != null) {
                r.setParent(this);
            }
            return true;
        }
        if (caseExpression == p) {
        	Expression z = (Expression) q;
        	caseExpression = z;
        	if ( z != null) {
        		z.setExpressionContainer(this);
    		}
    		return true;
        }
        return false;
    }

   /**
     Get the number of expressions in this container.
     @return the number of expressions.
     */
    public int getExpressionCount() {
    	if (caseExpression != null)
    		return 1;
    	else
    		return 0;
    }
    

    /*
      Return the expression at the specified index in this node's
      "virtual" expression array.
      @param index an index for an expression.
      @return the expression with the given index.
      @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
      of bounds.
    */
    public Expression getExpressionAt(int index) {
    	if (caseExpression != null && index == 0)
			return caseExpression;
		throw new ArrayIndexOutOfBoundsException();
    }

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#makeParentRoleValid()
	 */
	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		if (caseExpression != null) {
			caseExpression.setExpressionContainer(this);
		}
	}

}



