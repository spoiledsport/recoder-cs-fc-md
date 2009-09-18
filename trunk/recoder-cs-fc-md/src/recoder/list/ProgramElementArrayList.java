package recoder.list;
import recoder.csharp.ProgramElement;
import recoder.ModelElement;
// This file is part of the RECODER library and protected by the LGPL.

/**
   List implementation based on the doubling array technique for ProgramElement
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class ProgramElementArrayList
     extends AbstractArrayList
  implements ProgramElementMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public ProgramElementArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public ProgramElementArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public ProgramElementArrayList(ProgramElement[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public ProgramElementArrayList(ProgramElement a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected ProgramElementArrayList(ProgramElementArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new ProgramElementArrayList(this);
    }

    public final void set(int index, ProgramElement element) {
	super.set(index, element);
    }

    public final void insert(int index, ProgramElement element) {
	super.insert(index, element);
    }

    public final void insert(int index, ProgramElementList list) {
	super.insert(index, list);
    }

    public final void add(ProgramElement element) {
	super.add(element);
    }

    public final void add(ProgramElementList list) {
	super.add(list);
    }    

    public final ProgramElement getProgramElement(int index) {
	return (ProgramElement)super.get(index);
    }

    public final ProgramElement[] toProgramElementArray() {
	ProgramElement[] result = new ProgramElement[size()];
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
