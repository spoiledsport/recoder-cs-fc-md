package recodercs.list;
import recodercs.kit.Transformation;

/**
  A list for objects of type Transformation. This interface does not define
  mutators and is extended by TransformationMutableList.
  @see TransformationMutableList  
*/
public interface TransformationList extends ObjectList {

    /**
     * An empty list.
     */
    TransformationList EMPTY_LIST = new TransformationArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    Transformation getTransformation(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    Transformation[] toTransformationArray();
    
}
