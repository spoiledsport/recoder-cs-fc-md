package recoder.csharp.declaration;

import recoder.abstraction.OperatorOverload;
import recoder.csharp.Identifier;
import recoder.csharp.ProgramElement;
import recoder.csharp.SourceElement;
import recoder.csharp.SourceVisitor;
import recoder.csharp.Statement;
import recoder.csharp.StatementBlock;
import recoder.csharp.reference.TypeReference;
import recoder.list.ModifierMutableList;
import recoder.list.ParameterDeclarationMutableList;

/**
 * @author kis
 *
 * Indexer operator declaration
 */
public class IndexerDeclaration
    extends MethodDeclaration
    implements AccessorContainer, OperatorOverload {

    /** Accessor blocks */
    protected GetAccessor getBlock;
    protected SetAccessor setBlock;

    /**
     * Constructor for IndexerDeclaration.
     */
    public IndexerDeclaration() {
        super();
    }

    /**
     * Constructor for IndexerDeclaration.
     * @param modifiers
     * @param returnType
     * @param name
     * @param parameters
     * @param exceptions
     * @param body
     */
    public IndexerDeclaration(
        ModifierMutableList modifiers,
        TypeReference returnType,
        Identifier name,
        ParameterDeclarationMutableList parameters) {
        super(modifiers, returnType, name, parameters, null, null);
    }

    /**
     * Constructor for IndexerDeclaration.
     * @param proto
     */
    public IndexerDeclaration(IndexerDeclaration proto) {
        super(proto);
        if (proto.getBlock != null) {
            this.getBlock = (GetAccessor) proto.getBlock.deepClone();
        }
        if (proto.setBlock != null) {
            this.setBlock = (SetAccessor) proto.setBlock.deepClone();
        }
        makeParentRoleValid();
    }

    /**
     * @see recoder.csharp.SourceElement#deepClone()
     */
    public Object deepClone() {
        return new IndexerDeclaration(this);
    }

    /**
     * @see recoder.csharp.NonTerminalProgramElement#getChildAt(int)
     */
    public ProgramElement getChildAt(int index) {
        int len;
        if (attrSections != null) {
            len = attrSections.size();
            if (len > index) {
                return attrSections.getAttributeSection(index);
            }
            index -= len;
        }

        if (modifiers != null) {
            len = modifiers.size();
            if (len > index) {
                return modifiers.getProgramElement(index);
            }
            index -= len;
        }
        if (returnType != null) {
            if (index == 0)
                return returnType;
            index--;
        }
        if (name != null) {
            if (index == 0)
                return name;
            index--;
        }
        if (parameters != null) {
            len = parameters.size();
            if (len > index) {
                return parameters.getProgramElement(index);
            }
            index -= len;
        }

        if (getBlock != null) {
            if (index == 0)
                return getBlock;
            index--;
        }
        if (setBlock != null) {
            if (index == 0)
                return setBlock;
            index--;
        }
            throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * @see recoder.csharp.NonTerminalProgramElement#getChildCount()
     */
    public int getChildCount() {
        return super.getChildCount()
            + (getBlock != null ? 1 : 0)
            + (setBlock != null ? 1 : 0);
    }

    /**
     * @see recoder.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
     */
    public int getChildPositionCode(ProgramElement child) {
        if (child == getBlock)
            return 1;
        if (child == setBlock)
            return 2;
        return super.getChildPositionCode(child) + 3;
    }

    /**
     * @see recoder.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
     */
    public boolean replaceChild(ProgramElement p, ProgramElement q) {

        if (super.replaceChild(p, q))
            return true;
        if (getBlock == p) {

            getBlock = (GetAccessor) q;
            if (q != null) {
                ((StatementBlock) q).setStatementContainer(this);
            }
            return true;
        }
        if (setBlock == p) {

            setBlock = (SetAccessor) q;
            if (q != null) {
                ((StatementBlock) q).setStatementContainer(this);
            }
            return true;
        }

        return false;
    }

    /**
     * @see recoder.csharp.NonTerminalProgramElement#makeParentRoleValid()
     */
    public void makeParentRoleValid() {
        super.makeParentRoleValid();
        try {
            getBlock.setAccessorContainer(this);
            setBlock.setAccessorContainer(this);
        } catch (NullPointerException e) {
        }
    }

    /**
     * Don't ever think about setting the Body. This method does nothing here.
     * @see recoder.csharp.declaration.MethodDeclaration#setBody(StatementBlock)
     */
    public void setBody(StatementBlock body) {
    }

    public GetAccessor getGetAccessor() {
        return getBlock;
    }
    public SetAccessor getSetAccessor() {
        return setBlock;
    }
    public void setGetAccessor(GetAccessor s) {
        getBlock = s;
    }
    public void setSetAccessor(SetAccessor s) {
        setBlock = s;
    }
    public boolean hasGetAccessor() {
        return (getBlock != null);
    }

    public boolean hasSetAccessor() {
        return (setBlock != null);
    }
    /**
     * @see recoder.csharp.StatementContainer#getStatementAt(int)
     */
    public Accessor getAccessorAt(int index) {
        if (getBlock != null) {
            if (index == 0)
                return getBlock;
            index--;
        }
        if (setBlock != null) {
            if (index == 0)
                return setBlock;
            index--;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * @see recoder.csharp.StatementContainer#getStatementCount()
     */
    public int getAccessorCount() {
        int result = 0;
        if (getBlock != null)
            result++;
        if (setBlock != null)
            result++;
        return result;
    }

    /**
     * @see recoder.csharp.SourceElement#accept(SourceVisitor)
     */
    public void accept(SourceVisitor v) {
        v.visitIndexerDeclaration(this);
    }

}
