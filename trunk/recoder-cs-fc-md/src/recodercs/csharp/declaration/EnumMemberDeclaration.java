package recodercs.csharp.declaration;

import recodercs.csharp.Expression;
import recodercs.csharp.ExpressionContainer;
import recodercs.csharp.Identifier;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.reference.TypeReference;
import recodercs.list.FieldSpecificationArrayList;
import recodercs.list.FieldSpecificationMutableList;
import recodercs.list.ModifierMutableList;
import recodercs.util.Debug;

/**
 * @author kis
 *
 * Declaration of enum members...
 * 
 * 
 */
public class EnumMemberDeclaration extends FieldDeclaration {

	/**
	 * Constructor for EnumMemberDeclaration.
	 */
	public EnumMemberDeclaration() {
		super();
	}

	/**
	 * Constructor for EnumMemberDeclaration.
	 */
	public EnumMemberDeclaration(EnumMemberSpecification m) {
		super();
		FieldSpecificationMutableList l = new FieldSpecificationArrayList();
		l.add(m);
		setFieldSpecifications(l);
		makeParentRoleValid();
	}

	/**
	 * Constructor for EnumMemberDeclaration.
	 */
	public EnumMemberDeclaration(Identifier id) {
		super();
		EnumMemberSpecification m = new EnumMemberSpecification(id);
		FieldSpecificationMutableList l = new FieldSpecificationArrayList();
		l.add(m);
		setFieldSpecifications(l);
		makeParentRoleValid();
	}


	/**
	 * Constructor for EnumMemberDeclaration.
	 * @param mods
	 * @param init
	 */
	public EnumMemberDeclaration(
		ModifierMutableList mods,
		Expression init) {
			super();
			setModifiers(mods);
			setExpression(init);
		makeParentRoleValid();
	}


	/**
	 * Constructor for EnumMemberDeclaration.
	 * @param proto
	 */
	public EnumMemberDeclaration(EnumMemberDeclaration proto) {
		super(proto);
	}
	
	public void setExpression(Expression init) {
		Debug.asserta(getFieldSpecifications().size() == 1);
		getFieldSpecifications().getFieldSpecification(0).setInitializer(init);
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitEnumMemberDeclaration(this);
	}

	public EnumMemberSpecification getEnumMember() {
		Debug.asserta(getFieldSpecifications());
		Debug.asserta(getFieldSpecifications().size()==1);
		Debug.asserta(getFieldSpecifications().getFieldSpecification(0) instanceof EnumMemberSpecification);
		return	(EnumMemberSpecification) getFieldSpecifications().getFieldSpecification(0);
	}

	/**
	 * @see recodercs.csharp.declaration.FieldDeclaration#setFieldSpecifications(FieldSpecificationMutableList)
	 */
	public void setFieldSpecifications(FieldSpecificationMutableList l) {
		// These are additional conditions for enum members.
		Debug.asserta(l);
		Debug.asserta(l.size()==1);
		Debug.asserta(l.getFieldSpecification(0) instanceof EnumMemberSpecification);
		
		super.setFieldSpecifications(l);
	}

}
