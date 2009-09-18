package recoder.list;
import recoder.csharp.reference.UncollatedReferenceQualifier;
import recoder.ModelElement;
import recoder.csharp.ProgramElement;
// This file is part of the RECODER library and protected by the LGPL.

/**
   List implementation based on the doubling array technique for UncollatedReferenceQualifier
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class UncollatedReferenceQualifierArrayList
     extends AbstractArrayList
  implements UncollatedReferenceQualifierMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public UncollatedReferenceQualifierArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public UncollatedReferenceQualifierArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public UncollatedReferenceQualifierArrayList(UncollatedReferenceQualifier[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public UncollatedReferenceQualifierArrayList(UncollatedReferenceQualifier a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected UncollatedReferenceQualifierArrayList(UncollatedReferenceQualifierArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new UncollatedReferenceQualifierArrayList(this);
    }

    public final void set(int index, UncollatedReferenceQualifier element) {
	super.set(index, element);
    }

    public final void insert(int index, UncollatedReferenceQualifier element) {
	super.insert(index, element);
    }

    public final void insert(int index, UncollatedReferenceQualifierList list) {
	super.insert(index, list);
    }

    public final void add(UncollatedReferenceQualifier element) {
	super.add(element);
    }

    public final void add(UncollatedReferenceQualifierList list) {
	super.add(list);
    }    

    public final UncollatedReferenceQualifier getUncollatedReferenceQualifier(int index) {
	return (UncollatedReferenceQualifier)super.get(index);
    }

    public final UncollatedReferenceQualifier[] toUncollatedReferenceQualifierArray() {
	UncollatedReferenceQualifier[] result = new UncollatedReferenceQualifier[size()];
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
