package recodercs.list;
import recodercs.csharp.reference.MethodReference;

/**
  A list for objects of type MethodReference. This interface does not define
  mutators and is extended by MethodReferenceMutableList.
  @see MethodReferenceMutableList  
*/
public interface MethodReferenceList extends MemberReferenceList, StatementList, ExpressionList, ProgramElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    MethodReferenceList EMPTY_LIST = new MethodReferenceArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    MethodReference getMethodReference(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    MethodReference[] toMethodReferenceArray();
    
}
