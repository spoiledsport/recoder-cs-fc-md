package recodercs.list;
import recodercs.abstraction.Type;

/**
  A list for objects of type Type. This interface does not define
  mutators and is extended by TypeMutableList.
  @see TypeMutableList  
*/
public interface TypeList extends ProgramModelElementList, NamedModelElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    TypeList EMPTY_LIST = new TypeArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    Type getType(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    Type[] toTypeArray();
    
}
