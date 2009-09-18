package recoder.list;
import recoder.NamedModelElement;
import recoder.ModelElement;
// This file is part of the RECODER library and protected by the LGPL.

/**
   List implementation based on the doubling array technique for NamedModelElement
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class NamedModelElementArrayList
     extends AbstractArrayList
  implements NamedModelElementMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public NamedModelElementArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public NamedModelElementArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public NamedModelElementArrayList(NamedModelElement[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public NamedModelElementArrayList(NamedModelElement a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected NamedModelElementArrayList(NamedModelElementArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new NamedModelElementArrayList(this);
    }

    public final void set(int index, NamedModelElement element) {
	super.set(index, element);
    }

    public final void insert(int index, NamedModelElement element) {
	super.insert(index, element);
    }

    public final void insert(int index, NamedModelElementList list) {
	super.insert(index, list);
    }

    public final void add(NamedModelElement element) {
	super.add(element);
    }

    public final void add(NamedModelElementList list) {
	super.add(list);
    }    

    public final NamedModelElement getNamedModelElement(int index) {
	return (NamedModelElement)super.get(index);
    }

    public final NamedModelElement[] toNamedModelElementArray() {
	NamedModelElement[] result = new NamedModelElement[size()];
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


    public final ModelElement getModelElement(int index) {
	return (ModelElement)get(index);
    }

    public final ModelElement[] toModelElementArray() {
	ModelElement[] result = new ModelElement[size()];
	copyInto(result);
	return result;
    }

}
