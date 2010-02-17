package recodercs.list;
import recodercs.ModelElement;
import recodercs.NamedModelElement;
import recodercs.abstraction.Namespace;
import recodercs.abstraction.ProgramModelElement;

/**
   List implementation based on the doubling array technique for Namespace
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class NamespaceArrayList
     extends AbstractArrayList
  implements NamespaceMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public NamespaceArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public NamespaceArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public NamespaceArrayList(Namespace[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public NamespaceArrayList(Namespace a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected NamespaceArrayList(NamespaceArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new NamespaceArrayList(this);
    }

    public final void set(int index, Namespace element) {
	super.set(index, element);
    }

    public final void insert(int index, Namespace element) {
	super.insert(index, element);
    }

    public final void insert(int index, NamespaceList list) {
	super.insert(index, list);
    }

    public final void add(Namespace element) {
	super.add(element);
    }

    public final void add(NamespaceList list) {
	super.add(list);
    }    

    public final Namespace getNamespace(int index) {
	return (Namespace)super.get(index);
    }

    public final Namespace[] toNamespaceArray() {
	Namespace[] result = new Namespace[size()];
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


    public final ProgramModelElement getProgramModelElement(int index) {
	return (ProgramModelElement)get(index);
    }

    public final ProgramModelElement[] toProgramModelElementArray() {
	ProgramModelElement[] result = new ProgramModelElement[size()];
	copyInto(result);
	return result;
    }

}
