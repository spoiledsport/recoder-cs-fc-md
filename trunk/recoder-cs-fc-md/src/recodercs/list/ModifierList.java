package recodercs.list;
import recodercs.csharp.declaration.Modifier;

/**
  A list for objects of type Modifier. This interface does not define
  mutators and is extended by ModifierMutableList.
  @see ModifierMutableList  
*/
public interface ModifierList extends ProgramElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    ModifierList EMPTY_LIST = new ModifierArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    Modifier getModifier(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    Modifier[] toModifierArray();
    
}
