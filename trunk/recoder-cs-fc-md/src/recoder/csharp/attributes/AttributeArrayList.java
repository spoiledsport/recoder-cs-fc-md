package recoder.csharp.attributes;
import recoder.csharp.ProgramElement;
import recoder.list.AbstractArrayList;
import recoder.ModelElement;
// This file is part of the RECODER library and protected by the LGPL.

/**
   List implementation based on the doubling array technique for DesignPattern
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class AttributeArrayList
     extends AbstractArrayList
  implements AttributeMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public AttributeArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public AttributeArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public AttributeArrayList(Attribute[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public AttributeArrayList(Attribute a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected AttributeArrayList(AttributeArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new AttributeArrayList(this);
    }

    public final void set(int index, Attribute element) {
	super.set(index, element);
    }

    public final void insert(int index, Attribute element) {
	super.insert(index, element);
    }

    public final void insert(int index, AttributeList list) {
	super.insert(index, list);
    }

    public final void add(Attribute element) {
	super.add(element);
    }

    public final void add(AttributeList list) {
	super.add(list);
    }    

    public final Attribute getAttribute(int index) {
	return (Attribute)super.get(index);
    }

    public final Attribute[] toAttributeArray() {
	Attribute[] result = new Attribute[size()];
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

	/**
	 * @see recoder.list.ProgramElementList#getProgramElement(int)
	 */
	public ProgramElement getProgramElement(int index) {
		return (ProgramElement)get(index);
	}

	/**
	 * @see recoder.list.ProgramElementList#toProgramElementArray()
	 */
	public ProgramElement[] toProgramElementArray() {
	ProgramElement[] result = new ProgramElement[size()];
	copyInto(result);
	return result;
	}

}
