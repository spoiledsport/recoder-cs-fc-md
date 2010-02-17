package recodercs.list;
// This file is part of the RECODER library and protected by the LGPL.

/**
  A list for objects of type String. This interface does not define
  mutators and is extended by StringMutableList.
  @see StringMutableList  
*/
public interface StringList extends ObjectList {

    /**
     * An empty list.
     */
    StringList EMPTY_LIST = new StringArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    String getString(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    String[] toStringArray();
    
}
