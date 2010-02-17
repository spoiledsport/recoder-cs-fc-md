package recodercs.list;
import recodercs.ModelElement;
import recodercs.csharp.Expression;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.reference.FieldReference;
import recodercs.csharp.reference.MemberReference;
import recodercs.csharp.reference.VariableReference;

/**
   List implementation based on the doubling array technique for FieldReference
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class FieldReferenceArrayList
     extends AbstractArrayList
  implements FieldReferenceMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public FieldReferenceArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public FieldReferenceArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public FieldReferenceArrayList(FieldReference[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public FieldReferenceArrayList(FieldReference a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected FieldReferenceArrayList(FieldReferenceArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new FieldReferenceArrayList(this);
    }

    public final void set(int index, FieldReference element) {
	super.set(index, element);
    }

    public final void insert(int index, FieldReference element) {
	super.insert(index, element);
    }

    public final void insert(int index, FieldReferenceList list) {
	super.insert(index, list);
    }

    public final void add(FieldReference element) {
	super.add(element);
    }

    public final void add(FieldReferenceList list) {
	super.add(list);
    }    

    public final FieldReference getFieldReference(int index) {
	return (FieldReference)super.get(index);
    }

    public final FieldReference[] toFieldReferenceArray() {
	FieldReference[] result = new FieldReference[size()];
	copyInto(result);
	return result;
    }


    public final MemberReference getMemberReference(int index) {
	return (MemberReference)get(index);
    }

    public final MemberReference[] toMemberReferenceArray() {
	MemberReference[] result = new MemberReference[size()];
	copyInto(result);
	return result;
    }


    public final Expression getExpression(int index) {
	return (Expression)get(index);
    }

    public final Expression[] toExpressionArray() {
	Expression[] result = new Expression[size()];
	copyInto(result);
	return result;
    }


    public final ProgramElement getProgramElement(int index) {
	return (ProgramElement)get(index);
    }

    public final ProgramElement[] toProgramElementArray() {
	ProgramElement[] result = new ProgramElement[size()];
	copyInto(result);
	return result;
    }


    public final ModelElement getModelElement(int index) {
	return (ModelElement)get(index);
    }

    public final ModelElement[] toModelElementArray() {
	ModelElement[] result = new ModelElement[size()];
	copyInto(result);
	return result;
    }


    public final Object getObject(int index) {
	return (Object)get(index);
    }

    public final Object[] toObjectArray() {
	Object[] result = new Object[size()];
	copyInto(result);
	return result;
    }


    public final VariableReference getVariableReference(int index) {
	return (VariableReference)get(index);
    }

    public final VariableReference[] toVariableReferenceArray() {
	VariableReference[] result = new VariableReference[size()];
	copyInto(result);
	return result;
    }

}
