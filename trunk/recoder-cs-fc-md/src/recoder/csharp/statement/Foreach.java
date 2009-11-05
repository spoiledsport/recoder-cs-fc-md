// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.statement;

import recoder.*;
import recoder.csharp.*;
import recoder.csharp.declaration.LocalVariableDeclaration;
import recoder.csharp.declaration.VariableSpecification;
import recoder.csharp.expression.ExpressionStatement;
import recoder.csharp.reference.TypeReference;
import recoder.csharp.reference.TypeReferenceContainer;
import recoder.list.*;
import recoder.util.Debug;

/**
 Synchronized block.
 @author <TT>AutoDoc</TT>
 */

public class Foreach extends CSharpStatement 
                      implements StatementContainer, ExpressionContainer,
                      			   VariableScope, ProgramElement
                       {

	/** The identifier */
	@Deprecated
	protected Identifier identifier;

	/** The type reference */
	@Deprecated
	protected TypeReference reference;
	
    /**
    Inits.
    */
	protected LocalVariableDeclaration lvd;

//	public TypeReference getReference() {
//		return reference;
//	}
//
//	public void setReference(TypeReference reference) {
//		this.reference = reference;
//	}

	public LocalVariableDeclaration getLvd() {
		return lvd;
	}

	public void setLvd(LocalVariableDeclaration lvd) {
		this.lvd = lvd;
	}

	/**
     Expression.
     */

    protected Expression expression;

    /**
     Body.
     */

    protected Statement body;

    /**
     Foreach block.
     */

    public Foreach() {}

    /**
     Foreach block.
     @param body a statement block.
     */

    public Foreach(Statement body) {
        setBody(body);
        makeParentRoleValid();
    }

    /**
     Foreach block.
     @param e an expression.
     @param body a statement block.
     */

    public Foreach(Expression e, Statement body) {
        setExpression(e);
        setBody(body);
        makeParentRoleValid();
    }

    /**
     Foreach block.
     @param proto a synchronized block.
     */

    protected Foreach(Foreach proto) {
        super(proto);
        if (proto.expression != null) {
            expression = (Expression)proto.expression.deepClone();
        }
        if (proto.body != null) {
            body = (Statement)proto.body.deepClone();
        }
        if (proto.lvd != null) {
        	lvd = (LocalVariableDeclaration)proto.lvd.deepClone();
        }
//        if (proto.reference != null) {
//            reference= (TypeReference)proto.reference.deepClone();
//        }
//        if (proto.identifier != null) {
//            identifier= (Identifier)proto.identifier.deepClone();
//        }
        
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Foreach(this);
    }

    /**
     Make parent role valid.
     */

    public void makeParentRoleValid() {
        super.makeParentRoleValid();
        if (body != null) {
            body.setStatementContainer(this);
        }
        if (expression != null) {
            expression.setExpressionContainer(this);
        }
        if (lvd != null) {
        	lvd.setStatementContainer(this);
        }
//        if (identifier != null) {
//            identifier.setParent(this);
//        }
//        if (reference != null) {
//            reference.setParent(this);
//        }
        
        
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
        if (expression == p) {
            Expression r = (Expression)q;
            expression = r;
            if (r != null) {
                r.setExpressionContainer(this);
            }
            return true;
        }
        if (body == p) {
            Statement r = (Statement)q;
            body = r;
            if (r != null) {
                r.setStatementContainer(this);
            }
            return true;
        }
//        if (reference == p) {
//            TypeReference r = (TypeReference)q;
//            reference = r;
//            if (r != null) {
//                r.setParent(this);
//            }
//            return true;
//        }
//        if (identifier == p) {
//            Identifier r = (Identifier)q;
//            identifier = r;
//            if (r != null) {
//                r.setParent(this);
//            }
//            return true;
//        }
        if (lvd == p) {
            LocalVariableDeclaration r = (LocalVariableDeclaration)q;
            lvd = r;
            if (lvd != null) {
            	r.setStatementContainer(this);
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
        return (expression != null) ? 1 : 0;
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
        if (expression != null && index == 0) {
            return expression;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
     Set expression.
     @param e an expression.
     */

    public void setExpression(Expression e) {
        expression = e;
    }

    /**
     Get expression.
     @return the expression.
     */

    public Expression getExpression() {
        return expression;
    }

    /**
     Returns the number of children of this node.
     @return an int giving the number of children of this node
    */

    public int getChildCount() {
        int result = 0;
        if (expression != null) result++;
        if (body       != null) result++;
//        if (identifier != null) result++;
//        if (reference  != null) result++;
        if (lvd != null)  result++;
        return result;
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
        if (expression != null) {
            if (index == 0) return expression;
            index--;
        }
        if (body != null) {
            if (index == 0) return body;
            index--;
        }
        if (lvd != null) {
            if (index == 0) return lvd;
            index--;
        }
//        if (reference != null) {
//            if (index == 0) return reference;
//            index--;
//        }
//        if (identifier != null) {
//            if (index == 0) return identifier;
//            index--;
//        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getChildPositionCode(ProgramElement child) {
        // role 0: expression
        // role 1: body
        if (expression == child) {
            return 0;
        }
        if (body == child) {
            return 1;
        }
        if (lvd == child) {
        	return 2;
        }
//        if (body == reference) {
//            return 2;
//        }
//        if (body == identifier) {
//            return 3;
//        }
        return -1;
    }

    /**
     Get body.
     @return the statement block.
     */

    public Statement getBody() {
        return body;
    }

    /**
     Set body.
     @param body a statement block.
     */

    public void setBody(Statement body) {
        this.body = body;
    }

    /**
     Get the number of statements in this container.
     @return the number of statements.
     */

    public int getStatementCount() {
        return (body != null) ? 1 : 0;
    }

    /*
      Return the statement at the specified index in this node's
      "virtual" statement array.
      @param index an index for a statement.
      @return the statement with the given index.
      @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
      of bounds.
    */

    public Statement getStatementAt(int index) {
        if (body != null && index == 0) {
            return body;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void accept(SourceVisitor v) {
        v.visitForeach(this);
    }
    
//	/**
//	 * Returns the identifier.
//	 * @return Identifier
//	 */
//	public Identifier getIdentifier() {
//		return identifier;
//	}
//
//	/**
//	 * Sets the identifier.
//	 * @param identifier The identifier to set
//	 */
//	public void setIdentifier(Identifier identifier) {
//		this.identifier = identifier;
//	}

	/**
	 * Returns the identifier.
	 * @return Identifier
	 */
	public String getName() {
		VariableSpecificationList vsal = getLvd().getVariables();
		return vsal.getVariableSpecification(0).getName();
	}

//	/**
//	 * Sets the identifier.
//	 * @param identifier The identifier to set
//	 */
//	public void setName(Identifier identifier) {
//		setIdentifier(identifier);
//	}

    /*
      Return the type reference at the specified index in this node's
      "virtual" type reference array.
      @param index an index for a type reference.
      @return the type reference with the given index.
      @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
      of bounds.
    */

//    public TypeReference getTypeReferenceAt(int index) {
//    	if (index == 0 && reference != null) return reference;
//    	throw new ArrayIndexOutOfBoundsException(); 
//    }
//    
//    public int getTypeReferenceCount() {
//    	return reference != null ? 1 : 0;
//    }
//
//	
//	/**
//	 * Sets the reference.
//	 * @param reference The reference to set
//	 */
//	public void setTypeReference(TypeReference reference) {
//		this.reference = reference;
//	}

	public void addVariableToScope(VariableSpecification var) {
		Debug.asserta(var);		
	}

	public VariableSpecification getVariableInScope(String name) {
		VariableSpecificationList vars =
            ((LocalVariableDeclaration)lvd).getVariables();
        for (int i = 0, s = vars.size(); i < s; i += 1) {
            VariableSpecification v = vars.getVariableSpecification(i);
            if (name.equals(v.getName())) {
                return v;
            }
        }
        return null;

	}

	public VariableSpecificationList getVariablesInScope() {
		System.out.println("getting variables from foreach");
		if (lvd != null) {
			VariableSpecificationMutableList vsal = lvd.getVariableSpecifications();
			return vsal;
	    }
	    return VariableSpecificationList.EMPTY_LIST;
	}

	public void removeVariableFromScope(String name) {
	    Debug.asserta(name);		
	}

	public boolean isDefinedScope() {
	    return true;
	}

	public void setDefinedScope(boolean defined) {}

}
