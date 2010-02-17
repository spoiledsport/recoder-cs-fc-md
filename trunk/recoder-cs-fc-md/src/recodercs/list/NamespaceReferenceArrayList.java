package recodercs.list;
import recodercs.ModelElement;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.reference.NamespaceReference;

/**
   List implementation based on the doubling array technique for NamespaceReference
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class NamespaceReferenceArrayList
     extends AbstractArrayList
  implements NamespaceReferenceMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public NamespaceReferenceArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public NamespaceReferenceArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public NamespaceReferenceArrayList(NamespaceReference[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public NamespaceReferenceArrayList(NamespaceReference a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected NamespaceReferenceArrayList(NamespaceReferenceArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new NamespaceReferenceArrayList(this);
    }

    public final void set(int index, NamespaceReference element) {
	super.set(index, element);
    }

    public final void insert(int index, NamespaceReference element) {
	super.insert(index, element);
    }

    public final void insert(int index, NamespaceReferenceList list) {
	super.insert(index, list);
    }

    public final void add(NamespaceReference element) {
	super.add(element);
    }

    public final void add(NamespaceReferenceList list) {
	super.add(list);
    }    

    public final NamespaceReference getPackageReference(int index) {
	return (NamespaceReference)super.get(index);
    }

    public final NamespaceReference[] toPackageReferenceArray() {
	NamespaceReference[] result = new NamespaceReference[size()];
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


    public final ProgramElement getProgramElement(int index) {
	return (ProgramElement)get(index);
    }

    public final ProgramElement[] toProgramElementArray() {
	ProgramElement[] result = new ProgramElement[size()];
	copyInto(result);
	return result;
    }

}
