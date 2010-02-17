// This file is part of the RECODER library and protected by the LGPL.

package recodercs.list;

/**
   List implementation based on the doubling array technique.
   @since 0.01
   @serial
   @author AL
*/

public class ObjectArrayList extends AbstractArrayList
  implements ObjectMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public ObjectArrayList() {
	super();
    }

    /**
       Creates a list with one element.
       @since 0.01
    */
    public ObjectArrayList(Object o) {
	super(o);
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public ObjectArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    protected ObjectArrayList(ObjectArrayList proto) {
	super(proto);
    }

    
    public Object deepClone() {
	return new ObjectArrayList(this);
    }

    public final Object getObject(int index) {
	return get(index);
    }
    
    public final void set(int index, Object element) {
	super.set(index, element);
    }

    public final void insert(int index, Object element) {
	super.insert(index, element);
    }

    public final void insert(int index, ObjectList list) {
	super.insert(index, list);
    }

    public final void add(Object element) {
	super.add(element);
    }

    public final void add(ObjectList list) {
	super.add(list);
    }

    public final Object[] toObjectArray() {
	Object[] result = new Object[size()];
	copyInto(result);
	return result;
    }    
}
