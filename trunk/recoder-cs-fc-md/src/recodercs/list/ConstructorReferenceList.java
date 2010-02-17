package recodercs.list;
import recodercs.csharp.reference.ConstructorReference;

/**
  A list for objects of type ConstructorReference. This interface does not define
  mutators and is extended by ConstructorReferenceMutableList.
  @see ConstructorReferenceMutableList  
*/
public interface ConstructorReferenceList extends StatementList, MemberReferenceList, ProgramElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    ConstructorReferenceList EMPTY_LIST = new ConstructorReferenceArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    ConstructorReference getConstructorReference(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    ConstructorReference[] toConstructorReferenceArray();
    
}
