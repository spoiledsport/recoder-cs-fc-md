package recoder.csharp.declaration;

import recoder.abstraction.ClassType;
import recoder.abstraction.Enum;
import recoder.abstraction.EnumMember;
import recoder.abstraction.Type;
import recoder.convenience.Naming;
import recoder.csharp.Expression;
import recoder.csharp.Identifier;
import recoder.util.Debug;

/**
 * EnumMemberSpecification.java
 * @author orosz
 *
 * 
 */
public class EnumMemberSpecification extends ConstantFieldSpecification
		implements EnumMember {

	/**
	 * Constructor for EnumMemberSpecification.
	 */
	public EnumMemberSpecification() {
		super();
	}

	/**
	 * Constructor for EnumMemberSpecification.
	 * @param name
	 */
	public EnumMemberSpecification(Identifier name) {
		super(name);
	}

	/**
	 * Constructor for EnumMemberSpecification.
	 * @param name
	 * @param init
	 */
	public EnumMemberSpecification(Identifier name, Expression init) {
		super(name, init);
	}

	/**
	 * Constructor for EnumMemberSpecification.
	 * @param proto
	 */
	public EnumMemberSpecification(FieldSpecification proto) {
		super(proto);
	}

	/**
	 * @see recoder.abstraction.Member#isInternal()
	 */
	public boolean isInternal() {
		return super.isInternal();
	}

	/**
	 * @see recoder.abstraction.Member#isNew()
	 */
	public boolean isNew() {
		return super.isNew();
	}

	/**
	 * @see recoder.abstraction.Member#isPrivate()
	 */
	public boolean isPrivate() {
		return false;
	}

	/**
	 * @see recoder.abstraction.Member#isProtected()
	 */
	public boolean isProtected() {
		return false;
	}

	/**
	 * @see recoder.abstraction.Member#isPublic()
	 */
	public boolean isPublic() {
		return true;
	}

	/**
	 * @see recoder.abstraction.Member#isStatic()
	 */
	public boolean isStatic() {
		return true;
	}

	/**
	 * @see recoder.abstraction.Field#isVolatile()
	 */
	public boolean isVolatile() {
		return false;
	}

	/**
	 * Enum members report their parent's type as their type.
	 * @see recoder.abstraction.Variable#getType()
	 */
	public Type getType() {
		return getEnum().getBaseType();
	}

	/**
	 * @see recoder.csharp.declaration.VariableSpecification#isSealed()
	 */
	public boolean isSealed() {
		return super.isSealed();
	}


	public Enum getEnum() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		return service.getContainingEnum(this);		
	}

	/**
	 * Enum members are not contained in a class type.
	 * @see recoder.abstraction.Member#getContainingClassType()
	 */
	public ClassType getContainingClassType() {
		System.err.println("Warning: Calling getContainingClassType on an enum member returns null!");
		return null;
	}

	public String getFullName() {
		return Naming.getFullNameEnumMember(this);
	}

}
