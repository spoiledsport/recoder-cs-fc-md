// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp;

import recoder.*;
import recoder.list.*;
import recoder.csharp.statement.*;
import recoder.csharp.declaration.*;
import recoder.convenience.*;
import recoder.abstraction.*;
import recoder.util.*;

import java.util.Enumeration;

/**
   Statement block.
   @author AL
   @author <TT>AutoDoc</TT>
 */

// TODO: Remove TypeScope interface from the statement block

public class StatementBlock
    extends CSharpStatement
    implements StatementContainer, TypeDeclarationContainer,
               VariableScope, TypeScope {

    /**
     Body.
     */
    protected StatementMutableList body;

    /**
       Undefined scope tag.
     */
    protected final static MutableMap UNDEFINED_SCOPE = new NaturalHashTable();

    /**
       Scope table for types.
    */
    protected MutableMap name2type = UNDEFINED_SCOPE;

    /**
       Scope table for fields.
    */
    protected MutableMap name2var = UNDEFINED_SCOPE;

    /**
     Statement block.
     */
    public StatementBlock() {}

    /**
     Statement block.
     @param block a statement mutable list.
     */
    public StatementBlock(StatementMutableList block) {
        setBody(block);
        makeParentRoleValid();
    }

    /**
     Statement block.
     @param proto a statement block.
     */

    protected StatementBlock(StatementBlock proto) {
        super(proto);
        if (proto.body != null) {
            body = (StatementMutableList)proto.body.deepClone();
        }
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new StatementBlock(this);
    }

    /**
     Make parent role valid.
     */

    public void makeParentRoleValid() {
        super.makeParentRoleValid();
        if (body != null) {
            for (int i = body.size() - 1; i >= 0; i -= 1) {
                body.getStatement(i).setStatementContainer(this);
            }
        }
    }

    /**
     Get body.
     @return the statement mutable list.
     */

    public StatementMutableList getBody() {
        return body;
    }

    /**
     Set body.
     @param list a statement mutable list.
     */

    public void setBody(StatementMutableList list) {
        body = list;
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
        count = (body == null) ? 0 : body.size();
        for (int i = 0; i < count; i++) {
            if (body.getProgramElement(i) == p) {
                if (q == null) {
                    body.remove(i);
                } else {
                    Statement r = (Statement)q;
                    body.set(i, r);
                    r.setStatementContainer(this);
                }
                return true;
            }
        }
        return false;
    }


    /**
     Returns the number of children of this node.
     @return an int giving the number of children of this node
    */

    public int getChildCount() {
        return (body != null) ? body.size() : 0;
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
        if (body != null) {
            int len = body.size();
            if (len > index) {
                return body.getProgramElement(index);
            }
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getChildPositionCode(ProgramElement child) {
        // role 0 (IDX): statements
        if (body != null) {
            int index = body.indexOf(child);
            if (index >= 0) {
                return (index << 4) | 0;
            }
        }
        return -1;
    }

    /**
     Get the number of statements in this container.
     @return the number of statements.
     */

    public int getStatementCount() {
        return (body != null) ? body.size() : 0;
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
        if (body != null) {
            return body.getStatement(index);
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
     Get the number of type declarations in this container.
     @return the number of type declarations.
     */

    public int getTypeDeclarationCount() {
        int count = 0;
        if (body != null) {
            for (int i = body.size() - 1; i >= 0; i -= 1) {
                if (body.getStatement(i) instanceof TypeDeclaration) {
                    count += 1;
                }
            }
        }
        return count;
    }

    /*
      Return the type declaration at the specified index in this node's
      "virtual" type declaration array.
      @param index an index for a type declaration.
      @return the type declaration with the given index.
      @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
      of bounds.
    */

    public TypeDeclaration getTypeDeclarationAt(int index) {
        if (body != null) {
            int s = body.size();
            for (int i = 0; i < s && index >= 0; i += 1) {
                Statement st = body.getStatement(i);
                if (st instanceof TypeDeclaration) {
                    if (index == 0) {
                        return (TypeDeclaration)st;
                    }
                    index -= 1;
                }
            }
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public boolean isDefinedScope() {
        return name2type != UNDEFINED_SCOPE;
    }

    public void setDefinedScope(boolean defined) {
        if (!defined) {
            name2type = UNDEFINED_SCOPE;
            name2var = UNDEFINED_SCOPE;
        } else {
            name2type = null;
            name2var = null;
        }
    }

    public DeclaredTypeList getTypesInScope() {
        if (name2type == null || name2type.isEmpty()) {
            return DeclaredTypeList.EMPTY_LIST;
        }
        DeclaredTypeMutableList res = new DeclaredTypeArrayList();
        Enumeration menum = name2type.elements();
        while (menum.hasMoreElements()) {
            res.add((DeclaredType)menum.nextElement());
        }
        return res;
    }

    public DeclaredType getTypeInScope(String name) {
        Debug.asserta(name);
        if (name2type == null) {
            return null;
        }
        return (DeclaredType)name2type.get(name);
    }

    public void addTypeToScope(DeclaredType type, String name) {
        Debug.asserta(type, name);
        if (name2type == null || name2type == UNDEFINED_SCOPE) {
            name2type = new NaturalHashTable();
        }
        name2type.put(name, type);
    }

    public void removeTypeFromScope(String name) {
        Debug.asserta(name);
        if (name2type == null || name2type == UNDEFINED_SCOPE) {
            return;
        }
        name2type.remove(name);
    }

    public VariableSpecificationList getVariablesInScope() {
        if (name2var == null || name2var.isEmpty()) {
            return VariableSpecificationList.EMPTY_LIST;
        }
        VariableSpecificationMutableList res =
            new VariableSpecificationArrayList();
        Enumeration menum = name2var.elements();
        while (menum.hasMoreElements()) {
            res.add((VariableSpecification)menum.nextElement());
        }
        return res;
    }

    public VariableSpecification getVariableInScope(String name) {
        Debug.asserta(name);
        if (name2var == null) {
            return null;
        }
        return (VariableSpecification)name2var.get(name);
    }

    public void addVariableToScope(VariableSpecification var) {
        Debug.asserta(var);
        if (name2var == null || name2var == UNDEFINED_SCOPE) {
            name2var = new NaturalHashTable();
        }
        name2var.put(var.getName(), var);
    }

    public void removeVariableFromScope(String name) {
        Debug.asserta(name);
        if (name2var == null || name2var == UNDEFINED_SCOPE) {
            return;
        }
        name2var.remove(name);
    }

    public void accept(SourceVisitor v) {
        v.visitStatementBlock(this);
    }
}
