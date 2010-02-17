package recodercs.list;
import recodercs.ModelElement;

/**
  A list for objects of type ModelElement. This interface does not define
  mutators and is extended by ModelElementMutableList.
  @see ModelElementMutableList  
*/
public interface ModelElementList extends ObjectList {

    /**
     * An empty list.
     */
    ModelElementList EMPTY_LIST = new ModelElementArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    ModelElement getModelElement(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    ModelElement[] toModelElementArray();
    
}
