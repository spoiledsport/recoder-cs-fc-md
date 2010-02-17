package recodercs.list;
import recodercs.ModelElement;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.reference.MemberReference;
import recodercs.csharp.reference.TypeReference;

/**
   List implementation based on the doubling array technique for TypeReference
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class TypeReferenceArrayList
     extends AbstractArrayList
  implements TypeReferenceMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public TypeReferenceArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public TypeReferenceArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public TypeReferenceArrayList(TypeReference[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public TypeReferenceArrayList(TypeReference a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected TypeReferenceArrayList(TypeReferenceArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new TypeReferenceArrayList(this);
    }

    public final void set(int index, TypeReference element) {
	super.set(index, element);
    }

    public final void insert(int index, TypeReference element) {
	super.insert(index, element);
    }

    public final void insert(int index, TypeReferenceList list) {
	super.insert(index, list);
    }

    public final void add(TypeReference element) {
	super.add(element);
    }

    public final void add(TypeReferenceList list) {
	super.add(list);
    }    

    public final TypeReference getTypeReference(int index) {
	return (TypeReference)super.get(index);
    }

    public final TypeReference[] toTypeReferenceArray() {
	TypeReference[] result = new TypeReference[size()];
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


    public final MemberReference getMemberReference(int index) {
	return (MemberReference)get(index);
    }

    public final MemberReference[] toMemberReferenceArray() {
	MemberReference[] result = new MemberReference[size()];
	copyInto(result);
	return result;
    }

}
