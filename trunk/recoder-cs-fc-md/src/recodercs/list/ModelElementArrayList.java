package recodercs.list;
import recodercs.ModelElement;

/**
   List implementation based on the doubling array technique for ModelElement
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class ModelElementArrayList
     extends AbstractArrayList
  implements ModelElementMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public ModelElementArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public ModelElementArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public ModelElementArrayList(ModelElement[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public ModelElementArrayList(ModelElement a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected ModelElementArrayList(ModelElementArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new ModelElementArrayList(this);
    }

    public final void set(int index, ModelElement element) {
	super.set(index, element);
    }

    public final void insert(int index, ModelElement element) {
	super.insert(index, element);
    }

    public final void insert(int index, ModelElementList list) {
	super.insert(index, list);
    }

    public final void add(ModelElement element) {
	super.add(element);
    }

    public final void add(ModelElementList list) {
	super.add(list);
    }    

    public final ModelElement getModelElement(int index) {
	return (ModelElement)super.get(index);
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

}
