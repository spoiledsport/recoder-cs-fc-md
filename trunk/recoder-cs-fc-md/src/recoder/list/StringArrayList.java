package recoder.list;
// This file is part of the RECODER library and protected by the LGPL.

/**
   List implementation based on the doubling array technique for String
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class StringArrayList
     extends AbstractArrayList
  implements StringMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public StringArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public StringArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public StringArrayList(String[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public StringArrayList(String a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected StringArrayList(StringArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new StringArrayList(this);
    }

    public final void set(int index, String element) {
	super.set(index, element);
    }

    public final void insert(int index, String element) {
	super.insert(index, element);
    }

    public final void insert(int index, StringList list) {
	super.insert(index, list);
    }

    public final void add(String element) {
	super.add(element);
    }

    public final void add(StringList list) {
	super.add(list);
    }    

    public final String getString(int index) {
	return (String)super.get(index);
    }

    public final String[] toStringArray() {
	String[] result = new String[size()];
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
