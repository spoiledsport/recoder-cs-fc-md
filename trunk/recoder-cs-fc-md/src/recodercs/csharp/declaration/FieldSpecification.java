// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration;

import recodercs.abstraction.ClassType;
import recodercs.abstraction.Field;
import recodercs.*;
import recodercs.abstraction.*;
import recodercs.convenience.Naming;
import recodercs.csharp.*;
import recodercs.csharp.reference.*;
import recodercs.list.*;
import recodercs.util.Debug;

public class FieldSpecification extends VariableSpecification implements Field {

	/**
	 Field specification.
	 */

	public FieldSpecification() {}

	/**
	 Field specification.
	 @param name an identifier.
	 */

	public FieldSpecification(Identifier name) {
		super(name);
	}

	/**
	 Field specification.
	 @param name an identifier.
	 @param init an expression.
	 */

	public FieldSpecification(Identifier name, Expression init) {
		super(name, init);
	}

	/**
	 Field specification.
	 @param proto a field specification.
	 */

	protected FieldSpecification(FieldSpecification proto) {
		super(proto);
	}

	/**
	 Deep clone.
	 @return the object.
	 */

	public Object deepClone() {
		return new FieldSpecification(this);
	}

	/**
	 Set parent.
	 @param parent must be a field declaration.
	 */

	public void setParent(VariableDeclaration parent) {
		setParent((FieldDeclaration) parent);
	}

	/**
	 Set parent.
	 @param parent a field declaration.
	 */

	public void setParent(FieldDeclaration parent) {
		super.setParent(parent);
	}

	/**
	 * Test whether the declaration is private.
	 */

	public boolean isPrivate() {
		return parent.isPrivate();
	}

	/**
	 * Test whether the declaration is protected.
	 */

	public boolean isProtected() {
		return parent.isProtected();
	}

	/**
	 * Test whether the declaration is public.
	 */

	public boolean isPublic() {
		return parent.isPublic();
	}

	/**
	 * Test whether the declaration is static.
	 */

	public boolean isStatic() {
		return parent.isStatic();
	}

	protected static void updateModel() {
		factory.getServiceConfiguration().getChangeHistory().updateModel();
	}

	public ClassType getContainingClassType() {
		if (service == null) {
			Debug.log("Zero service while " + Debug.makeStackTrace());
			updateModel();
		}
		return service.getContainingClassType(this);
	}

	public String getFullName() {
		return Naming.getFullName(this);
	}

	public void accept(SourceVisitor v) {
		v.visitFieldSpecification(this);
	}

	/**
	 * @see recodercs.abstraction.Field#isVolatile()
	 */
	public boolean isVolatile() {
		return ((FieldDeclaration) parent).isVolatile();
	}

	/**
	 * @see recodercs.abstraction.Member#isInternal()
	 */
	public boolean isInternal() {
		return parent.isInternal();
	}

	/**
	 * @see recodercs.abstraction.Member#isNew()
	 */
	public boolean isNew() {
		return parent.isNew();
	}

	/**
	 * @see recodercs.abstraction.Field#isVolatile()
	 */
	public boolean isReadOnly() {
		return ((FieldDeclaration) parent).isReadOnly();
	}

}
