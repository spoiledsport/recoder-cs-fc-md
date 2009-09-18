package recoder.csharp.declaration;

import recoder.csharp.CSharpProgramElement;
import recoder.csharp.NonTerminalProgramElement;
import recoder.csharp.ProgramElement;
import recoder.csharp.SourceVisitor;
import recoder.csharp.Statement;
import recoder.csharp.StatementBlock;
import recoder.csharp.StatementContainer;
import recoder.csharp.attributes.AttributableElement;
import recoder.csharp.attributes.AttributeSection;
import recoder.csharp.attributes.AttributeSectionList;
import recoder.csharp.attributes.AttributeSectionMutableList;

/**
 * @author kis
 * This class is a supertype for the special accessor types used in 
 * property and event declarations.
 */
public abstract class Accessor
	extends CSharpProgramElement
	implements StatementContainer, AttributableElement {

	AttributeSectionMutableList attrSections;

	StatementBlock statement;

	AccessorContainer parent;

	/**
	 * Constructor for Accessor.
	 */
	public Accessor() {
		super();
	}

	/**
	 * Constructor for Accessor.
	 * @param proto
	 */
	protected Accessor(Accessor proto) {
		super(proto);
		if (proto.attrSections != null) {
			attrSections = (AttributeSectionMutableList) proto.attrSections.deepClone();
		}
		if (proto.statement != null) {
			this.statement = (StatementBlock) proto.statement.deepClone();
		} 
		makeParentRoleValid();
	}

	/**
	 * @see recoder.csharp.StatementContainer#getStatementCount()
	 */
	public int getStatementCount() {
		return (statement != null) ? 1 : 0 ;
	}

	/**
	 * @see recoder.csharp.StatementContainer#getStatementAt(int)
	 */
	public Statement getStatementAt(int index) {
		if (index == 0 && statement != null) {
			return statement;
		} 
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		int result = 0;
		if (statement != null) result ++;
		if (attrSections != null)
			result += attrSections.size();
		return result;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
		int count, len;
		if (attrSections != null) {
			len = attrSections.size();
			if (len > index) {
				return attrSections.getAttributeSection(index);
			}
			index -= len;
		}
		if (index == 0 && statement != null) {
			return statement;
		} 
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getIndexOfChild(ProgramElement)
	 */
	public int getIndexOfChild(ProgramElement child) {
		if (child == statement) {
			return 0;
		}
		return -1;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
	 */
	public int getChildPositionCode(ProgramElement child) {
		// role 0: statement
		// role 15 : attribute
		if (attrSections != null) {
			int index = attrSections.indexOf(child);
			if (index >= 0) {
				return (index << 4) | 15;
			}
		}	
		if (child == statement) {
			return 0;
		}
		return -1;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getIndexOfChild(int)
	 */
	public int getIndexOfChild(int positionCode) {
		if (positionCode == 0 && statement != null) {
			return 0;
		}
		if (positionCode > 0) {
			return positionCode >> 4;
		}
		return -1;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getRoleOfChild(int)
	 */
	public int getRoleOfChild(int positionCode) {
		if (positionCode == 0 && statement != null) {
			return 0;
		}
		if (positionCode > 0) {
			return positionCode & 15;
		}
		
		return -1;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#makeParentRoleValid()
	 */
	public void makeParentRoleValid() {
		if (attrSections != null) {
			for (int i = attrSections.size() - 1; i >= 0; i--) {
				attrSections.getAttributeSection(i).setParent(this);
			}
		}	
		if (statement != null) {
			statement.setStatementContainer(this);
		}
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#makeAllParentRolesValid()
	 */
	public void makeAllParentRolesValid() {
		makeParentRoleValid();
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
	 */
	public boolean replaceChild(ProgramElement p, ProgramElement q) {
		if (p == null) {
			throw new NullPointerException();
		}
		int count = (attrSections == null) ? 0 : attrSections.size();
		for (int i = 0; i < count; i++) {
			if (attrSections.getAttributeSection(i) == p) {
				if (q == null) {
					attrSections.remove(i);
				} else {
					AttributeSection r = (AttributeSection) q;
					attrSections.set(i, r);
					r.setParent(this);
				}
				return true;
			}
		}
		if (p == statement) {
			statement = (StatementBlock) q;
			if (statement != null) {
				statement.setStatementContainer(this);
			}
			return true;
		}
		return false;
	}

	/**
	 * @see recoder.csharp.ProgramElement#getASTParent()
	 */
	public NonTerminalProgramElement getASTParent() {
		return parent;
	}

	/**
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public abstract void accept(SourceVisitor v);

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public abstract Object deepClone();
	
	public void setStatementBlock(StatementBlock block) {
		statement = block;
		makeParentRoleValid();
	}
	
	public void setAccessorContainer(AccessorContainer c) {
		parent = c;
	}
	
	/**
	 * @see recoder.csharp.attributes.AttributableElement#getAttributeSectionAt(int)
	 */
	public AttributeSection getAttributeSectionAt(int index) {
		int len;
		if (attrSections != null) {
			len = attrSections.size();
			if (len > index) {
				return attrSections.getAttributeSection(index);
			}
			index -= len;
		}
	
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recoder.csharp.attributes.AttributableElement#getAttributeSectionCount()
	 */
	public int getAttributeSectionCount() {
		int result = 0;
		if (attrSections != null)
			result += attrSections.size();
		return result;
	}

	/**
	 * @see recoder.csharp.attributes.AttributableElement#setAttributeSections(AttributeSectionMutableList)
	 */
	public void setAttributeSections(AttributeSectionMutableList attrs) {
		attrSections = attrs;
		makeParentRoleValid();
	}

    
	

	/**
	 * @see recoder.csharp.attributes.AttributableElement#getAttributeSections()
	 */
	public AttributeSectionList getAttributeSections() {
		return attrSections;
	}

}
