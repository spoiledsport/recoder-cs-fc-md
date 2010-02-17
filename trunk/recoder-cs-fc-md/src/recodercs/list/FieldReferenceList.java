package recodercs.list;
import recodercs.csharp.reference.FieldReference;

/**
  A list for objects of type FieldReference. This interface does not define
  mutators and is extended by FieldReferenceMutableList.
  @see FieldReferenceMutableList  
*/
public interface FieldReferenceList extends VariableReferenceList, MemberReferenceList, ExpressionList, ProgramElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    FieldReferenceList EMPTY_LIST = new FieldReferenceArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    FieldReference getFieldReference(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    FieldReference[] toFieldReferenceArray();
    
}
