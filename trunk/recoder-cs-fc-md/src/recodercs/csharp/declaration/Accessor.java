package recodercs.csharp.declaration;

import recodercs.csharp.CSharpProgramElement;
import recodercs.csharp.NonTerminalProgramElement;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.Statement;
import recodercs.csharp.StatementBlock;
import recodercs.csharp.StatementContainer;
import recodercs.csharp.attributes.AttributableElement;
import recodercs.csharp.attributes.AttributeSection;
import recodercs.csharp.attributes.AttributeSectionList;
import recodercs.csharp.attributes.AttributeSectionMutableList;

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
	 * @see recodercs.csharp.StatementContainer#getStatementCount()
	 */
	public int getStatementCount() {
		return (statement != null) ? 1 : 0 ;
	}

	/**
	 * @see recodercs.csharp.StatementContainer#getStatementAt(int)
	 */
	public Statement getStatementAt(int index) {
		if (index == 0 && statement != null) {
			return statement;
		} 
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		int result = 0;
		if (statement != null) result ++;
		if (attrSections != null)
			result += attrSections.size();
		return result;
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildAt(int)
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
	 * @see recodercs.csharp.NonTerminalProgramElement#getIndexOfChild(ProgramElement)
	 */
	public int getIndexOfChild(ProgramElement child) {
		if (child == statement) {
			return 0;
		}
		return -1;
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
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
	 * @see recodercs.csharp.NonTerminalProgramElement#getIndexOfChild(int)
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
	 * @see recodercs.csharp.NonTerminalProgramElement#getRoleOfChild(int)
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
	 * @see recodercs.csharp.NonTerminalProgramElement#makeParentRoleValid()
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
	 * @see recodercs.csharp.NonTerminalProgramElement#makeAllParentRolesValid()
	 */
	public void makeAllParentRolesValid() {
		makeParentRoleValid();
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
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
	 * @see recodercs.csharp.ProgramElement#getASTParent()
	 */
	public NonTerminalProgramElement getASTParent() {
		return parent;
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public abstract void accept(SourceVisitor v);

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
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
	 * @see recodercs.csharp.attributes.AttributableElement#getAttributeSectionAt(int)
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
	 * @see recodercs.csharp.attributes.AttributableElement#getAttributeSectionCount()
	 */
	public int getAttributeSectionCount() {
		int result = 0;
		if (attrSections != null)
			result += attrSections.size();
		return result;
	}

	/**
	 * @see recodercs.csharp.attributes.AttributableElement#setAttributeSections(AttributeSectionMutableList)
	 */
	public void setAttributeSections(AttributeSectionMutableList attrs) {
		attrSections = attrs;
		makeParentRoleValid();
	}

    
	

	/**
	 * @see recodercs.csharp.attributes.AttributableElement#getAttributeSections()
	 */
	public AttributeSectionList getAttributeSections() {
		return attrSections;
	}

}
