// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.reference;

import recoder.*;
import recoder.csharp.*;
import recoder.csharp.expression.*;
import recoder.csharp.declaration.*;
import recoder.list.*;

/**
 Method reference.
 @author <TT>AutoDoc</TT>
 */

public class MethodReference extends CSharpNonTerminalProgramElement
 implements MemberReference, ReferencePrefix, ReferenceSuffix, ExpressionStatement, TypeReferenceContainer, NameReference {

    /**
     Expression parent.
     */

    protected ExpressionContainer expressionParent;

    /**
     Statement parent.
     */

    protected StatementContainer statementParent;

    /**
     Qualifier parent.
     */

    protected ReferenceSuffix qualifierParent;

    /**
     Access path.
     */

    protected ReferencePrefix accessPath;

    /**
     Name.
     */

    protected Identifier name;

    /**
     Arguments.
     */

    protected ExpressionMutableList arguments;

    /**
     Method reference.
     */

    public MethodReference() {}

    /**
     Method reference.
     @param name an identifier.
     */

    public MethodReference(Identifier name) {
        setIdentifier(name);
        makeParentRoleValid();
    }

    /**
     Method reference.
     @param accessPath a reference prefix.
     @param name an identifier.
     */

    public MethodReference(ReferencePrefix accessPath, Identifier name) {
        setReferencePrefix(accessPath);
        setIdentifier(name);
        makeParentRoleValid();
    }

    /**
     Method reference.
     @param name an identifier.
     @param args an expression mutable list.
     */

    public MethodReference(Identifier name, ExpressionMutableList args) {
        setIdentifier(name);
        setArguments(args);
        makeParentRoleValid();
    }

    /**
     Method reference.
     @param accessPath a reference prefix.
     @param name an identifier.
     @param args an expression mutable list.
     */

    public MethodReference(ReferencePrefix accessPath, Identifier name, ExpressionMutableList args) {
        setReferencePrefix(accessPath);
        setIdentifier(name);
        setArguments(args);
        makeParentRoleValid();
    }

    /**
     Method reference.
     @param proto a method reference.
     */

    protected MethodReference(MethodReference proto) {
        super(proto);
        if (proto.accessPath != null) {
            accessPath = (ReferencePrefix)proto.accessPath.deepClone();
        }
        if (proto.name != null) {
            name = (Identifier)proto.name.deepClone();
        }
        if (proto.arguments != null) {
            arguments = (ExpressionMutableList)proto.arguments.deepClone();
        }
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new MethodReference(this);
    }

    /**
     Make parent role valid.
     */

    public void makeParentRoleValid() {
        super.makeParentRoleValid();
        if (accessPath != null) {
            accessPath.setReferenceSuffix(this);
        }
        if (name != null) {
            name.setParent(this);
        }
        if (arguments != null) {
            for (int i = arguments.size() - 1; i >= 0; i -= 1) {
                arguments.getExpression(i).setExpressionContainer(this);
            }
        }
    }

    public SourceElement getFirstElement() {
        return (accessPath == null) ? getChildAt(0).getFirstElement() : accessPath.getFirstElement();
    }

    /**
     Get AST parent.
     @return the non terminal program element.
     */

    public NonTerminalProgramElement getASTParent() {
        if (statementParent != null) {
            return statementParent;
        } else if (expressionParent != null) {
            return expressionParent;
        } else {
            return qualifierParent;
        }
    }

    /**
     Get reference prefix.
     @return the reference prefix.
     */

    public ReferencePrefix getReferencePrefix() {
        return accessPath;
    }

    /**
     Set reference prefix.
     @param qualifier a reference prefix.
     */

    public void setReferencePrefix(ReferencePrefix qualifier) {
        accessPath = qualifier;
    }

    /**
     Get reference suffix.
     @return the reference suffix.
     */

    public ReferenceSuffix getReferenceSuffix() {
        return qualifierParent;
    }

    /**
     Set reference suffix.
     @param path a reference suffix.
     */

    public void setReferenceSuffix(ReferenceSuffix path) {
        qualifierParent = path;
	expressionParent = null;
	statementParent = null;
    }

    /**
     Get statement container.
     @return the statement container.
     */

    public StatementContainer getStatementContainer() {
        return statementParent;
    }

    /**
     Get expression container.
     @return the expression container.
     */

    public ExpressionContainer getExpressionContainer() {
        return expressionParent;
    }

    /**
     Set expression container.
     @param parent an expression container.
     */

    public void setExpressionContainer(ExpressionContainer parent) {
        expressionParent = parent;
	statementParent = null;
	qualifierParent = null;
    }

    /**
     Set statement container.
     @param parent a statement container.
     */

    public void setStatementContainer(StatementContainer parent) {
        statementParent = parent;
	expressionParent = null;
	qualifierParent = null;
    }

    /**
     Returns the number of children of this node.
     @return an int giving the number of children of this node
    */

    public int getChildCount() {
        int result = 0;
        if (accessPath != null) result++;
        if (name       != null) result++;
        if (arguments  != null) result += arguments.size();
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
        if (accessPath != null) {
            if (index == 0) return accessPath;
            index--;
        }
        if (name != null) {
            if (index == 0) return name;
            index--;
        }
        if (arguments != null) {
            return arguments.getProgramElement(index);
        }
        throw new ArrayIndexOutOfBoundsException();
    }


    public int getChildPositionCode(ProgramElement child) {
        // role 0: prefix
        // role 1: name
        // role 2 (IDX): parameters
        if (accessPath == child) {
            return 0;
        }
        if (name == child) {
            return 1;
        }
        if (arguments != null) {
            int index = arguments.indexOf(child);
            if (index >= 0) {
                return (index << 4) | 2;
            }
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
        int count;
        if (accessPath == p) {
            ReferencePrefix r = (ReferencePrefix)q;
            accessPath = r;
            if (r != null) {
                r.setReferenceSuffix(this);
            }
            return true;
        }
        if (name == p) {
            Identifier r = (Identifier)q;
            name = r;
            if (r != null) {
                r.setParent(this);
            }
            return true;
        }
        count = (arguments == null) ? 0 : arguments.size();
        for (int i = 0; i < count; i++) {
            if (arguments.getProgramElement(i) == p) {
                if (q == null) {
                    arguments.remove(i);
                } else {
                    Expression r = (Expression)q;
                    arguments.set(i, r);
                    r.setExpressionContainer(this);
                }
                return true;
            }
        }
        return false;
    }

    /**
     Get the number of type references in this container.
     @return the number of type references.
     */

    public int getTypeReferenceCount() {
        return (accessPath instanceof TypeReference) ? 1 : 0;
    }

    /*
      Return the type reference at the specified index in this node's
      "virtual" type reference array.
      @param index an index for a type reference.
      @return the type reference with the given index.
      @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
      of bounds.
    */

    public TypeReference getTypeReferenceAt(int index) {
        if (accessPath instanceof TypeReference && index == 0) {
            return (TypeReference)accessPath;
        }
        throw new ArrayIndexOutOfBoundsException();
    }


    /**
     Get the number of expressions in this container.
     @return the number of expressions.
     */

    public int getExpressionCount() {
        int result = 0;
        if (accessPath instanceof Expression) result += 1;
        if (arguments != null) {
            result += arguments.size();
        }
        return result;
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
        if (accessPath instanceof Expression) {
            if (index == 0) {
                return (Expression)accessPath;
            }
            index -= 1;
        }
        if (arguments != null) {
            return arguments.getExpression(index);
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
     Get name.
     @return the string.
     */

    public final String getName() {
        return (name == null) ? null : name.getText();
    }

    /**
     Get identifier.
     @return the identifier.
     */

    public Identifier getIdentifier() {
        return name;
    }

    /**
     Set identifier.
     @param id an identifier.
     */

    public void setIdentifier(Identifier id) {
        name = id;
    }

    /**
     Get arguments.
     @return the expression mutable list.
     */

    public ExpressionMutableList getArguments() {
        return arguments;
    }

    /**
     Set arguments.
     @param list an expression mutable list.
     */

    public void setArguments(ExpressionMutableList list) {
        arguments = list;
    }

    public void accept(SourceVisitor v) {
        v.visitMethodReference(this);
    }
}
