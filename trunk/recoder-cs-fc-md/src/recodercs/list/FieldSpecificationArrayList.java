package recodercs.list;
import recodercs.ModelElement;
import recodercs.NamedModelElement;
import recodercs.abstraction.Field;
import recodercs.abstraction.Member;
import recodercs.abstraction.ProgramModelElement;
import recodercs.abstraction.Variable;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.declaration.FieldSpecification;
import recodercs.csharp.declaration.VariableSpecification;

/**
   List implementation based on the doubling array technique for FieldSpecification
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class FieldSpecificationArrayList
     extends AbstractArrayList
  implements FieldSpecificationMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public FieldSpecificationArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public FieldSpecificationArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public FieldSpecificationArrayList(FieldSpecification[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public FieldSpecificationArrayList(FieldSpecification a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected FieldSpecificationArrayList(FieldSpecificationArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new FieldSpecificationArrayList(this);
    }

    public final void set(int index, FieldSpecification element) {
	super.set(index, element);
    }

    public final void insert(int index, FieldSpecification element) {
	super.insert(index, element);
    }

    public final void insert(int index, FieldSpecificationList list) {
	super.insert(index, list);
    }

    public final void add(FieldSpecification element) {
	super.add(element);
    }

    public final void add(FieldSpecificationList list) {
	super.add(list);
    }    

    public final FieldSpecification getFieldSpecification(int index) {
	return (FieldSpecification)super.get(index);
    }

    public final FieldSpecification[] toFieldSpecificationArray() {
	FieldSpecification[] result = new FieldSpecification[size()];
	copyInto(result);
	return result;
    }


    public final Field getField(int index) {
	return (Field)get(index);
    }

    public final Field[] toFieldArray() {
	Field[] result = new Field[size()];
	copyInto(result);
	return result;
    }


    public final Variable getVariable(int index) {
	return (Variable)get(index);
    }

    public final Variable[] toVariableArray() {
	Variable[] result = new Variable[size()];
	copyInto(result);
	return result;
    }


    public final Member getMember(int index) {
	return (Member)get(index);
    }

    public final Member[] toMemberArray() {
	Member[] result = new Member[size()];
	copyInto(result);
	return result;
    }


    public final ProgramModelElement getProgramModelElement(int index) {
	return (ProgramModelElement)get(index);
    }

    public final ProgramModelElement[] toProgramModelElementArray() {
	ProgramModelElement[] result = new ProgramModelElement[size()];
	copyInto(result);
	return result;
    }


    public final NamedModelElement getNamedModelElement(int index) {
	return (NamedModelElement)get(index);
    }

    public final NamedModelElement[] toNamedModelElementArray() {
	NamedModelElement[] result = new NamedModelElement[size()];
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


    public final VariableSpecification getVariableSpecification(int index) {
	return (VariableSpecification)get(index);
    }

    public final VariableSpecification[] toVariableSpecificationArray() {
	VariableSpecification[] result = new VariableSpecification[size()];
	copyInto(result);
	return result;
    }

}
