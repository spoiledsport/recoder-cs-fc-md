package recoder.csharp.declaration;

import recoder.abstraction.ClassType;
import recoder.abstraction.Property;
import recoder.abstraction.Type;
import recoder.convenience.Naming;
import recoder.csharp.Expression;
import recoder.csharp.Identifier;
import recoder.csharp.MemberName;
import recoder.csharp.MemberNameContainer;
import recoder.csharp.NonTerminalProgramElement;
import recoder.csharp.ProgramElement;
import recoder.csharp.SourceElement;
import recoder.csharp.SourceVisitor;
import recoder.list.ModifierMutableList;
import recoder.service.ProgramModelInfo;
import recoder.util.Debug;

/**
 * @author kis
 *
 * Property specification
 */
public class PropertySpecification extends FieldSpecification 
implements Property, MemberNameContainer {

	/**
	 * The name of the property
	 */
	protected MemberName mname;

//	protected PropertyDeclaration parent;

	public PropertySpecification(MemberName name) {
		super();
		this.mname = name;
		makeParentRoleValid();
	}
	
	/**
	 * Constructor PropertySpecification.
	 * @param propertySpecification
	 */
	protected PropertySpecification(PropertySpecification proto) {
		super(proto);
		makeParentRoleValid();
	}

	/**
	 * Constructor PropertySpecification.
	 */
	public PropertySpecification() {
		super();
	}




	/**
	 * @see recoder.abstraction.Property#isWriteOnly()
	 */
	public boolean isWriteOnly() {
		PropertyDeclaration parent = (PropertyDeclaration) getParent();
		return (parent.getGetAccessor()==null);
	}

	/**
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitPropertySpecification(this);
	}

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new PropertySpecification(this);
	}

	/**
	 * @see recoder.abstraction.Member#getContainingClassType()
	 */
	public ClassType getContainingClassType() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		return service.getContainingClassType(this);
		
	}

	/**
	 * @see recoder.abstraction.ProgramModelElement#getFullName()
	 */
	public String getFullName() {
		return Naming.getFullName(this);
	}

	/**
	 * @see recoder.abstraction.Variable#isReadOnly()
	 */
	public boolean isReadOnly() {
		PropertyDeclaration parent = (PropertyDeclaration) getParent();
		return (parent.getSetAccessor()==null);		
	}


	/**
	 * @see recoder.abstraction.Field#isVolatile()
	 */
	public boolean isVolatile() {
		return false;
	}

	/**
	 * @see recoder.csharp.declaration.FieldSpecification#setParent(FieldDeclaration)
	 */
	public void setParent(PropertyDeclaration parent) {
		super.setParent(parent);
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#makeParentRoleValid()
	 */
	public void makeParentRoleValid() {
		mname.setMemberNameContainer(this);
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		return (mname != null) ? 1 : 0;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
		if (mname != null && index == 0) {
				return mname; 
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
	 */
	public int getChildPositionCode(ProgramElement child) {
		if (mname != null && mname == child) {
			return 0;
		}
		else return -1;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
	 */
	public boolean replaceChild(ProgramElement p, ProgramElement q) {
		if (p == null) 
			throw new NullPointerException();
		
		if (p == mname) {
			mname = (MemberName) q;
			mname.setMemberNameContainer(this);
			return true;
		}
		
		return false;
	}

	/**
	 * @see recoder.csharp.declaration.VariableSpecification#getParent()
	 */
	public VariableDeclaration getParent() {
		return parent;
	}

	/**
	 * @see recoder.csharp.NamedProgramElement#getIdentifier()
	 */
	public Identifier getIdentifier() {
		return (mname != null) ? mname.getIdentifier() : null;
	}

	/**
	 * @see recoder.csharp.NamedProgramElement#setIdentifier(Identifier)
	 */
	public void setIdentifier(Identifier id) {
		if (mname != null) {
			mname.setIdentifier(id);
		}
		else {
			mname = getFactory().createMemberName(id);
		}	
	}

	/**
	 * @see recoder.abstraction.Variable#getType()
	 */
	public Type getType() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		return service.getType(this);
		
	}

	/**
	 * @see recoder.csharp.MemberNameContainer#getMemberName()
	 */
	public MemberName getMemberName() {
		return mname;
	}

	/**
	 * @see recoder.csharp.MemberNameContainer#setMemberName(MemberName)
	 */
	public void setMemberName(MemberName mname) {
		this.mname = mname;
		makeParentRoleValid();
	}


	/**
	 * @see recoder.NamedModelElement#getName()
	 */
	public String getName() {
		return mname.getName();
	}

	/**
	 * @see recoder.csharp.SourceElement#getFirstElement()
	 */
	public SourceElement getFirstElement() {
		if (mname != null) {
			return mname;
		}
		return this;
	}

	/**
	 * @see recoder.csharp.SourceElement#getLastElement()
	 */
	public SourceElement getLastElement() {
		if (mname != null) {
			return mname;
		}
		return this;
	}

}
