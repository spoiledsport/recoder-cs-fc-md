// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.statement;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.list.*;

/**
 Checked block.
 @author <TT>AutoDoc</TT>
 */

public class CheckedBlock extends CSharpStatement implements StatementContainer {


    /**
     Body.
     */

    protected StatementBlock body;

    /**
     Checked block.
     */

    public CheckedBlock() {}

    /**
     Checked block.
     @param body a statement block.
     */

    public CheckedBlock(StatementBlock body) {
        setBody(body);
        makeParentRoleValid();
    }

    /**
     @deprecated DO NOT USE, WILL BE REMOVED
     Checked block.
     @param e an expression.
     @param body a statement block.
     */

    public CheckedBlock(Expression e, StatementBlock body) {
        setBody(body);
        makeParentRoleValid();
    }

    /**
     Checked block.
     @param proto a synchronized block.
     */

    protected CheckedBlock(CheckedBlock proto) {
        super(proto);
        if (proto.body != null) {
            body = (StatementBlock)proto.body.deepClone();
        }
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new CheckedBlock(this);
    }

    /**
     Make parent role valid.
     */

    public void makeParentRoleValid() {
        super.makeParentRoleValid();
        if (body != null) {
            body.setStatementContainer(this);
        }
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
        if (body == p) {
            StatementBlock r = (StatementBlock)q;
            body = r;
            if (r != null) {
                r.setStatementContainer(this);
            }
            return true;
        }
        return false;
    }

    /**
     Returns the number of children of this node.
     @return an int giving the number of children of this node
    */

    public int getChildCount() {
        int result = 0;
        if (body       != null) result++;
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
        if (body != null) {
            if (index == 0) return body;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getChildPositionCode(ProgramElement child) {
        // role 0: expression
        // role 1: body
        if (body == child) {
            return 1;
        }
        return -1;
    }

    /**
     Get body.
     @return the statement block.
     */

    public StatementBlock getBody() {
        return body;
    }

    /**
     Set body.
     @param body a statement block.
     */

    public void setBody(StatementBlock body) {
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
        v.visitCheckedBlock(this);
    }
}
