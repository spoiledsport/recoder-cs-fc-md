// This file is part of the RECODER library and protected by the LGPL.

package recoder.list;

/**
  A list for objects. This interface does not define
  mutators.
*/

public interface ObjectList extends Cloneable {
    
    ObjectList EMPTY_LIST = new ObjectArrayList();

    /**
       Determine the number of elements in the list.
       @return the length of the list.
    */
    int size();

   /** 
    * Test list on emptyness.
    */
    boolean isEmpty();

    /**
      Retrieve an element from the given index.
      Does not return <tt>null</tt>.
      @param index the index of the element.
      @return the element at the given index.
      @exception IndexOutOfBoundsException if index &lt; 0 or 
      index &gt;= size()
      */
    Object getObject(int index);

    /**
      Search an element between indices.
      If <tt>upto</tt> &gt; <tt>from</tt>, the elements are searched in 
      forward direction, otherwise in reverse direction. For instance,
      indexOf(element, size() - 1, 0) returns the <EM>last</EM> element
      of the list. 
      <BR>
      indexOf(e) &gt;= 0 implies get(indexOf(e)).equals(e).
      @param element the element to search.
      @param from the index of the element to start searching, inclusive.
      @param upto the index to stop searching at, exclusive.
      @return the index of the element if successful, -1 otherwise.
      @exception IndexOutOfBoundsException if <tt>from</tt> is not in
      range [0, size()-1] or <tt>upto</tt> is not in range [-1, size()].
      */	
    int indexOf(Object element, int from, int upto);

    /**
      Search an element in the list. The search is in forward direction
      and behaves like indexOf(element, 0, size()-1)
      @param element the element to search.
      @return the index of the element, in [0, size()-1] if successful,
      -1 otherwise.
      @see #indexOf(Object,int,int)
    */
    int indexOf(Object element);

    /**
      Returns the current capacity of the list. Insertions into the
      list are inexpensive up to the capacity. getCapacity() &gt;= size().
      @return the number of elements that can be stored without reallocation.
     */
    int getCapacity();

    /**
      Reduces the storage needed for the list. Makes getCapacity() come
      as near to size() as possible for the given implementation.
     */
    void trim();

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    Object[] toObjectArray();

}
