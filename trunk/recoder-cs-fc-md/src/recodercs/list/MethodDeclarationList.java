package recodercs.list;
import recodercs.csharp.declaration.MethodDeclaration;

/**
  A list for objects of type MethodDeclaration. This interface does not define
  mutators and is extended by MethodDeclarationMutableList.
  @see MethodDeclarationMutableList  
*/
public interface MethodDeclarationList extends MethodList, MemberList, ProgramModelElementList, NamedModelElementList, MemberDeclarationList, ProgramElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    MethodDeclarationList EMPTY_LIST = new MethodDeclarationArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    MethodDeclaration getMethodDeclaration(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    MethodDeclaration[] toMethodDeclarationArray();
    
}
