// This file is part of the RECODER library and protected by the LGPL.

package recodercs.list;

import java.io.*;
import java.util.Stack;

/**
   List implementation based on the doubling array technique.
   @since 0.01
   @author AL
*/

public abstract class AbstractArrayList 
  implements ObjectList, Cloneable, Serializable {

    /**
       The element array.
       @since 0.01
       @serialData The array is flattened during serialization.
    */
    private transient Object[] array;

    /**
       The number of the elements in the list.
       Partial Invariant: (array == null) implies (count == 0)
       @since 0.01
       @serial The array is flattened during serialization.
    */
    private int count;

    /**
       Reallocation muliplicator: when an array is filled, a bigger
       one is allocated, while the increase depends on the former size
       and the growth factor > 1.
     */
    final static float GROWTH_FACTOR = 1.5f;

    /**
       Initial minimum capacity as soon as the first allocation is necessary.
       Also indicates the minimum amount to grow each time this is necessary.
     */
    final static int MIN_CAPACITY_DELTA = 2;

    /**
       Creates an empty list.
       @since 0.01
    */
    protected AbstractArrayList() {
	array = null;
    }

    /**
       Creates a list with one element.
       @since 0.01
    */
    protected AbstractArrayList(Object o) {
	this(1);
	array[0] = o;
	count = 1;
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    protected AbstractArrayList(int initialCapacity) {
	if (initialCapacity > 0) {	    
	    array = new Object[initialCapacity];
	}
    }

    /**
       Create a new array list as a deep clone of the given one,
       if it is a list of ProgramElements. Otherwise, create the list as
       a shallow copy.
       @since 0.01
    */
    protected AbstractArrayList(AbstractArrayList a) {
	this(a.count);
	count = a.count;
	if (count > 0) {
	    if (a instanceof ProgramElementList) {
		for (int i = 0; i < count; i += 1) {
		    array[i] = ((recodercs.csharp.ProgramElement)a.array[i]).deepClone();
		}
	    } else {
		System.arraycopy(a.array, 0, array, 0, count);
	    }
	}
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    protected AbstractArrayList(Object[] a) {
	this(a.length);
	count = a.length;
	System.arraycopy(a, 0, array, 0, count);
    }
    
    public final int size() {
	return count;
    }

    public final boolean isEmpty() {
       return count == 0;
    }

    public final int getCapacity() {
	return (array == null) ? 0 : array.length;
    }

    /**
       Sets the capacity of this list.
    */
    final void setCapacity(int capacity) {
	// no parameter validation!
	Object[] newarray = new Object[capacity];
	if (count > 0) {
	    System.arraycopy(array, 0, newarray, 0, count);
	}
	array = newarray;
    }   

    public void ensureCapacity(int capacity) {
	if (capacity > getCapacity()) {
	    setCapacity(capacity);
	}
    }

    public void trim() {
	if (count == 0) {
	    array = null;
	} else {
	    if (array.length > count) {		
		setCapacity(Math.max(count, MIN_CAPACITY_DELTA));
	    }
	}
    }

    /**
       Equality of lists.
       @return <CODE>true</CODE> if the object is a list with
       equal elements in the same order, <CODE>false</CODE> otherwise.
       @since 0.01
    */
    public boolean equals(Object o) {
	if (!(o instanceof ObjectList)) {
	    return false;
	}
	ObjectList list = (ObjectList)o;
	if (count != list.size()) {
	    return false;
	}
	if (count == 0) { // empty lists of different types are equals?!?
	    return true;
	}
 	for (int i = 0, s = list.size(); i < s; i += 1) {
	    Object o1 = array[i];
            Object o2 = list.getObject(i);
            if (o1 != o2) {
                if (o1 == null || !o1.equals(o2)) {
                    return false;
                }
            }
	}
	return true;
    }

    /**
       Returns the hash code of this list.
       @return  the hash code of this list.
    */
    public int hashCode() {
	int c = 0;
	for (int i = 0; i < count; i += 1) {
	    c = 31 * c + array[i].hashCode();
	}
	return c;
    }

    /**
       Returns a string representation of this list.
       @return a string containing all elements.
    */
    public String toString() {
	StringBuffer res = new StringBuffer();
	res.append("[");
	if (count > 0) {
	    for (int i = 0; i < count; i += 1) {
		res.append(array[i]);
		if (i < count - 1) {
		    res.append(", ");
		}
	    }
	}
	res.append("]");
	return res.toString();
    }

    /**
       Serialization support.
    */
    // Warning: untested
    private void writeObject(ObjectOutputStream stream) throws IOException {
	stream.defaultWriteObject();
	stream.writeInt(count);
	stream.writeInt(getCapacity());
	for (int i = 0; i < count; i += 1) {
	    stream.writeObject(array[i]);
	}
    }

    /**
       Deserialization support.
    */
    // Warning: untested
    private void readObject(ObjectInputStream stream)
	  throws IOException, ClassNotFoundException {
	stream.defaultReadObject();
	count = stream.readInt();
	int capacity = stream.readInt();
	if (capacity < count) {
	    capacity = count; // fail-safe; we could throw an exception, too
	}
	if (capacity > 0) {
	    array = new Object[capacity];
	}
	for (int i = 0; i < count; i += 1) {
	    array[i] = stream.readObject();
	}
    }

    /**
       Overwrites an element at the given position.
       @param index the destination index (should be in range).
       @param element the element to set.
    */
    protected void set(int index, Object element) {
	if (index >= count) {
	    throw new ArrayIndexOutOfBoundsException(index);
	}
	array[index] = element;
    }

    /**
       Returns the element at the given position.
       @param index an index within the list.
       @return the element at the given index.
    */
    protected final Object get(int index) {
	if (index >= count)  {
	    throw new ArrayIndexOutOfBoundsException(index);
	}
	return array[index];
    }

    public int indexOf(Object element, int from, int upto) {
	if (from < 0 || from >= count) {
	    throw new ArrayIndexOutOfBoundsException(from);
	}
	if (upto < -1 || upto > count) {
	    throw new ArrayIndexOutOfBoundsException(upto);
	}
	if (upto >= from) {
	    for (int i = from; i < upto; i += 1) {
		if (element.equals(array[i])) {
		    return i;
		}
	    }
	} else {
	    for (int i = from; i > upto; i -= 1) {
		if (element.equals(array[i])) {
		    return i;
		}
	    }
	}
	return -1;
    }

    public final int indexOf(Object element) {
	for (int i = 0; i < count; i += 1) {
	    if (element.equals(array[i])) {
		return i;
	    }
	}
	return -1;
    }

    public void clear() {	    
	while (count > 0) {
	    count -= 1;
	    array[count] = null;
	}
    }   

    public void removeRange(int from) {
	if (from < 0 || from >= count) {
	    throw new ArrayIndexOutOfBoundsException(from);
	}
	do {
	    count -= 1;
	    array[count] = null;
	} while (count > from);
    }
   
    public void removeRange(int from, int upto) {
	if (from < 0 || from > count) {
	    throw new ArrayIndexOutOfBoundsException(from);
	}
	if (upto < -1 || upto > count) {
	    throw new ArrayIndexOutOfBoundsException(upto);
	}
	if (from == upto) {
	    return;
	} else if (from > upto) {
	    upto ^= from ^= upto ^= from; // quickswap ;)
	}
	if (upto < count) {
	    System.arraycopy(array, upto, array, from, count - upto);
	}
	upto = count - (upto - from);
	do {
	    count -= 1;
	    array[count] = null;
	} while (count > upto);
    }

    public void remove(int index) {
	int last = index + 1;
	if (last > count) {
	    throw new ArrayIndexOutOfBoundsException(last);
	}
	if (last < count) {
	    System.arraycopy(array, last, array, index, count - last);
	}
	count -= 1;
	array[count] = null;
    }

    /**
       Makes room for the given number of elements at the given
       location.
    */
    private void makeRoom(int index, int n) {
	int last = index + n;
	if (array == null) {
	    setCapacity(Math.max(last + 1, MIN_CAPACITY_DELTA));
	} else if (index >= count) {
	    if (last >= array.length) {
		int cap = last + 1;
		cap = Math.max(cap, (int)(GROWTH_FACTOR * array.length));
		cap = Math.max(cap, array.length + MIN_CAPACITY_DELTA);
		setCapacity(cap);
		// +1 additional buffer (trim() to count = 0...)
	    }
	} else {
	    if (count + n >= array.length) {
		int cap = count + n + 1;
		cap = Math.max(cap, (int)(GROWTH_FACTOR * array.length));
		cap = Math.max(cap, array.length + MIN_CAPACITY_DELTA);
		setCapacity(cap);
	    }
	    System.arraycopy(array, index, array, last, count - index);
	}
    }

    protected void insert(int index, Object element) {
	if (index < 0 || index > count) {
	    throw new ArrayIndexOutOfBoundsException(index);
	}
	makeRoom(index, 1);
	count += 1;
	array[index] = element;
    }

    protected void insert(int index, ObjectList list) {
	if (index < 0 || index > count) {
	    throw new ArrayIndexOutOfBoundsException(index);
	}
	if (list == this) {
	    list = new ObjectArrayList(this);
	}
	int s = list.size();
	makeRoom(index, s);
	count += s;
	for (int i = 0; i < s; i += 1, index += 1) {
	    array[index] = list.getObject(i);
	}
    }
    
    protected void add(Object element) {
	makeRoom(count, 1);
	array[count] = element;
	count += 1;		
    }

    protected void add(ObjectList list) {
       int s = list.size();
       makeRoom(count, s); // this is safe for the case "list == this"
       for (int i = 0; i < s; i += 1, count += 1) {
	   array[count] = list.getObject(i);
       }
    }

    protected void copyInto(Object[] a) {
	for (int i = count - 1; i >= 0; i -= 1) {
	    a[i] = array[i];
	}
    }
}


