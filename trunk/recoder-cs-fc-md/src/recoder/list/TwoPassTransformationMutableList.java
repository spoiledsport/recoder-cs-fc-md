package recoder.list;
import recoder.kit.TwoPassTransformation;
/**
  A mutable list for objects of type TwoPassTransformation.
 */

public interface TwoPassTransformationMutableList extends TwoPassTransformationList {

    /**
      Ensures a given capacity. 
      */
    void ensureCapacity(int capacity);

    /**
      Remove all elements in the list. Behaves like remove(0, size()).
    */
    void clear();

    /**
      Replace the content of a given index. The element may not be
      <tt>null</tt>.
      @param index the index of the element to be replaced.
      @param element the element to be set as content of the given index.
      @exception IndexOutOfBoundsException if index < 0 or index >= size()
    */
    void set(int index, TwoPassTransformation element);

    /**
      Removes an element at the given index. The following elements
      are shifted downwards one index.
      @param index the index of the element to be removed.
      @exception IndexOutOfBoundsException if index < 0 or index > size() - 1
    */
    void remove(int index);

    /**
      Removes a sublist between the given indices.
      The following elements are shifted downwards as needed.
      As for indexOf, from may be &gt; upto.
      @param from the index of the first element to be removed.
      @param upto the index of the last element that is not removed.
      @exception IndexOutOfBoundsException if <tt>from</tt> is not in
      range [0, size()] or <tt>upto</tt> is not in range [-1, size()].
    */
    void removeRange(int from, int upto);

    /**
      Removes the end of the list beginning at the given index.
      @param from the index of the first element to be removed.
      @exception IndexOutOfBoundsException if <tt>from</tt> is not in
      range [0, size()-1].
    */
    void removeRange(int from);

    /**
      Insert a new element to the given index. The following elements are
      shifted upwards one index.
      @param index the index where the element should be inserted
      @param element the element to be inserted
      @exception IndexOutOfBoundsException if index < 0 or index > size()
     */	
    void insert(int index, TwoPassTransformation element);

    /**
      Insert a list of elements to the given index. The following elements are
      shifted upwards as needed.
      @param index the index where the list should be inserted
      @param list the list to be inserted
      @exception IndexOutOfBoundsException if index < 0 or index > size()
      @exception NullPointerException if list == null.
     */	
    void insert(int index, TwoPassTransformationList list);

    /**
      Append an element to the list. Behaves like insert(size(), element).
      @param element the element to be appended.
     */	
    void add(TwoPassTransformation element);

    /**
      Append a second list. Behaves like insert(size(), list).
      @param list the list to be appended.
     */
    void add(TwoPassTransformationList list);

    /**
       Deeply clones this list and its contents.
     */
    Object deepClone();

}
