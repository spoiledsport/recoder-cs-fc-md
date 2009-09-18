package recoder.list;
import recoder.abstraction.ProgramModelElement;
import recoder.ModelElement;
import recoder.NamedModelElement;
// This file is part of the RECODER library and protected by the LGPL.

/**
   List implementation based on the doubling array technique for ProgramModelElement
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class ProgramModelElementArrayList
     extends AbstractArrayList
  implements ProgramModelElementMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public ProgramModelElementArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public ProgramModelElementArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public ProgramModelElementArrayList(ProgramModelElement[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public ProgramModelElementArrayList(ProgramModelElement a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected ProgramModelElementArrayList(ProgramModelElementArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new ProgramModelElementArrayList(this);
    }

    public final void set(int index, ProgramModelElement element) {
	super.set(index, element);
    }

    public final void insert(int index, ProgramModelElement element) {
	super.insert(index, element);
    }

    public final void insert(int index, ProgramModelElementList list) {
	super.insert(index, list);
    }

    public final void add(ProgramModelElement element) {
	super.add(element);
    }

    public final void add(ProgramModelElementList list) {
	super.add(list);
    }    

    public final ProgramModelElement getProgramModelElement(int index) {
	return (ProgramModelElement)super.get(index);
    }

    public final ProgramModelElement[] toProgramModelElementArray() {
	ProgramModelElement[] result = new ProgramModelElement[size()];
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


    public final NamedModelElement getNamedModelElement(int index) {
	return (NamedModelElement)get(index);
    }

    public final NamedModelElement[] toNamedModelElementArray() {
	NamedModelElement[] result = new NamedModelElement[size()];
	copyInto(result);
	return result;
    }

}
