package recodercs.csharp.declaration;

import recodercs.abstraction.ClassType;
import recodercs.abstraction.Event;
import recodercs.abstraction.Type;
import recodercs.convenience.Naming;
import recodercs.csharp.Expression;
import recodercs.csharp.Identifier;
import recodercs.csharp.MemberName;
import recodercs.csharp.MemberNameContainer;
import recodercs.csharp.NonTerminalProgramElement;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.SourceElement;
import recodercs.csharp.SourceVisitor;
import recodercs.list.ModifierMutableList;
import recodercs.service.ProgramModelInfo;
import recodercs.util.Debug;

/**
 * @author kis
 *
 * Property specification
 */
public class EventSpecification extends FieldSpecification 
implements Event, MemberNameContainer {

	/**
	 * The name of the property
	 */
	protected MemberName mname;



	public EventSpecification(MemberName name) {
		super();
		this.mname = name;
		makeParentRoleValid();
	}
	
	/**
	 * Constructor PropertySpecification.
	 * @param propertySpecification
	 */
	protected EventSpecification(EventSpecification proto) {
		super(proto);
		makeParentRoleValid();
	}

	/**
	 * Constructor PropertySpecification.
	 */
	public EventSpecification() {
		super();
	}


	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitEventSpecification(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new EventSpecification(this);
	}

	/**
	 * @see recodercs.abstraction.Member#getContainingClassType()
	 */
	public ClassType getContainingClassType() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		return service.getContainingClassType(this);
		
	}

	/**
	 * @see recodercs.abstraction.ProgramModelElement#getFullName()
	 */
	public String getFullName() {
		return Naming.getFullName(this);
	}

	/**
	 * @see recodercs.abstraction.Variable#isReadOnly()
	 */
	public boolean isReadOnly() {
// An event cannot be readonly...
//		EventDeclaration parent = (EventDeclaration) getParent();
//		return (parent.getSetAccessor()==null);		
                return false;
	}


	/**
	 * @see recodercs.abstraction.Field#isVolatile()
	 */
	public boolean isVolatile() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.FieldSpecification#setParent(FieldDeclaration)
	 */
	public void setParent(PropertyDeclaration parent) {
		super.setParent(parent);
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#makeParentRoleValid()
	 */
	public void makeParentRoleValid() {
		mname.setMemberNameContainer(this);
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		return (mname != null) ? 1 : 0;
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
		if (mname != null && index == 0) {
				return mname; 
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
	 */
	public int getChildPositionCode(ProgramElement child) {
		if (mname != null && mname == child) {
			return 0;
		}
		else return -1;
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
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
	 * @see recodercs.csharp.declaration.VariableSpecification#getParent()
	 */
	public VariableDeclaration getParent() {
		return parent;
	}

	/**
	 * @see recodercs.csharp.NamedProgramElement#getIdentifier()
	 */
	public Identifier getIdentifier() {
		return (mname != null) ? mname.getIdentifier() : null;
	}

	/**
	 * @see recodercs.csharp.NamedProgramElement#setIdentifier(Identifier)
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
	 * @see recodercs.abstraction.Variable#getType()
	 */
	public Type getType() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		return service.getType(this);
		
	}

	/**
	 * @see recodercs.csharp.MemberNameContainer#getMemberName()
	 */
	public MemberName getMemberName() {
		return mname;
	}

	/**
	 * @see recodercs.csharp.MemberNameContainer#setMemberName(MemberName)
	 */
	public void setMemberName(MemberName mname) {
		this.mname = mname;
		makeParentRoleValid();
	}


	/**
	 * @see recodercs.NamedModelElement#getName()
	 */
	public String getName() {
		return mname.getName();
	}

	/**
	 * @see recodercs.csharp.SourceElement#getFirstElement()
	 */
	public SourceElement getFirstElement() {
		if (mname != null) {
			return mname;
		}
		return this;
	}

	/**
	 * @see recodercs.csharp.SourceElement#getLastElement()
	 */
	public SourceElement getLastElement() {
		if (initializer != null) {
			return initializer;
		}
		if (mname != null) {
			return mname;
		}
		return this;
	}

}
