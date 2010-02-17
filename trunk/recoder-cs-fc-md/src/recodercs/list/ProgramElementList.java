package recodercs.list;
import recodercs.csharp.ProgramElement;

/**
  A list for objects of type ProgramElement. This interface does not define
  mutators and is extended by ProgramElementMutableList.
  @see ProgramElementMutableList  
*/
public interface ProgramElementList extends ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    ProgramElementList EMPTY_LIST = new ProgramElementArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    ProgramElement getProgramElement(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    ProgramElement[] toProgramElementArray();
    
}
