package recodercs.list;
import recodercs.ModelElement;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.Statement;
import recodercs.csharp.reference.ConstructorReference;
import recodercs.csharp.reference.MemberReference;

/**
   List implementation based on the doubling array technique for ConstructorReference
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class ConstructorReferenceArrayList
     extends AbstractArrayList
  implements ConstructorReferenceMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public ConstructorReferenceArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public ConstructorReferenceArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public ConstructorReferenceArrayList(ConstructorReference[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public ConstructorReferenceArrayList(ConstructorReference a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected ConstructorReferenceArrayList(ConstructorReferenceArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new ConstructorReferenceArrayList(this);
    }

    public final void set(int index, ConstructorReference element) {
	super.set(index, element);
    }

    public final void insert(int index, ConstructorReference element) {
	super.insert(index, element);
    }

    public final void insert(int index, ConstructorReferenceList list) {
	super.insert(index, list);
    }

    public final void add(ConstructorReference element) {
	super.add(element);
    }

    public final void add(ConstructorReferenceList list) {
	super.add(list);
    }    

    public final ConstructorReference getConstructorReference(int index) {
	return (ConstructorReference)super.get(index);
    }

    public final ConstructorReference[] toConstructorReferenceArray() {
	ConstructorReference[] result = new ConstructorReference[size()];
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


    public final Statement getStatement(int index) {
	return (Statement)get(index);
    }

    public final Statement[] toStatementArray() {
	Statement[] result = new Statement[size()];
	copyInto(result);
	return result;
    }

}
