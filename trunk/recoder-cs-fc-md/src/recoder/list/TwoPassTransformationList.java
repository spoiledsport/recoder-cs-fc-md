package recoder.list;
import recoder.kit.TwoPassTransformation;
// This file is part of the RECODER library and protected by the LGPL.

/**
  A list for objects of type TwoPassTransformation. This interface does not define
  mutators and is extended by TwoPassTransformationMutableList.
  @see TwoPassTransformationMutableList  
*/
public interface TwoPassTransformationList extends TransformationList, ObjectList {

    /**
     * An empty list.
     */
    TwoPassTransformationList EMPTY_LIST = new TwoPassTransformationArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    TwoPassTransformation getTwoPassTransformation(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    TwoPassTransformation[] toTwoPassTransformationArray();
    
}
