package recoder.list;
import recoder.csharp.declaration.TypeDeclaration;
// This file is part of the RECODER library and protected by the LGPL.

/**
  A list for objects of type TypeDeclaration. This interface does not define
  mutators and is extended by TypeDeclarationMutableList.
  @see TypeDeclarationMutableList  
*/
public interface TypeDeclarationList extends ClassTypeList, TypeList, MemberList, ProgramModelElementList, NamedModelElementList, ProgramElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    TypeDeclarationList EMPTY_LIST = new TypeDeclarationArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    TypeDeclaration getTypeDeclaration(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    TypeDeclaration[] toTypeDeclarationArray();
    
}
