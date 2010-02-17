package recodercs.list;
import recodercs.csharp.declaration.MemberDeclaration;

/**
  A list for objects of type MemberDeclaration. This interface does not define
  mutators and is extended by MemberDeclarationMutableList.
  @see MemberDeclarationMutableList  
*/
public interface MemberDeclarationList extends ProgramElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    MemberDeclarationList EMPTY_LIST = new MemberDeclarationArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    MemberDeclaration getMemberDeclaration(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    MemberDeclaration[] toMemberDeclarationArray();
    
}
