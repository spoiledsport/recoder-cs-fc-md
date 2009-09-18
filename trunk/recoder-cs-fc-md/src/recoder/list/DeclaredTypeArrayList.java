package recoder.list;
import recoder.abstraction.DeclaredType;
import recoder.abstraction.Member;
import recoder.abstraction.ProgramModelElement;
import recoder.NamedModelElement;
import recoder.ModelElement;
import recoder.abstraction.Type;
// This file is part of the RECODER library and protected by the LGPL.

/**
   List implementation based on the doubling array technique for DeclaredType
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class DeclaredTypeArrayList
     extends AbstractArrayList
  implements DeclaredTypeMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public DeclaredTypeArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public DeclaredTypeArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public DeclaredTypeArrayList(DeclaredType[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public DeclaredTypeArrayList(DeclaredType a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected DeclaredTypeArrayList(DeclaredTypeArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new DeclaredTypeArrayList(this);
    }

    public final void set(int index, DeclaredType element) {
	super.set(index, element);
    }

    public final void insert(int index, DeclaredType element) {
	super.insert(index, element);
    }

    public final void insert(int index, DeclaredTypeList list) {
	super.insert(index, list);
    }

    public final void add(DeclaredType element) {
	super.add(element);
    }

    public final void add(DeclaredTypeList list) {
	super.add(list);
    }    

    public final DeclaredType getDeclaredType(int index) {
	return (DeclaredType)super.get(index);
    }

    public final DeclaredType[] toDeclaredTypeArray() {
	DeclaredType[] result = new DeclaredType[size()];
	copyInto(result);
	return result;
    }


    public final Member getMember(int index) {
	return (Member)get(index);
    }

    public final Member[] toMemberArray() {
	Member[] result = new Member[size()];
	copyInto(result);
	return result;
    }


    public final ProgramModelElement getProgramModelElement(int index) {
	return (ProgramModelElement)get(index);
    }

    public final ProgramModelElement[] toProgramModelElementArray() {
	ProgramModelElement[] result = new ProgramModelElement[size()];
	copyInto(result);
	return result;
    }


    public final NamedModelElement getNamedModelElement(int index) {
	return (NamedModelElement)get(index);
    }

    public final NamedModelElement[] toNamedModelElementArray() {
	NamedModelElement[] result = new NamedModelElement[size()];
	copyInto(result);
	return result;
    }


    public final ModelElement getModelElement(int index) {
	return (ModelElement)get(index);
    }

    public final ModelElement[] toModelElementArray() {
	ModelElement[] result = new ModelElement[size()];
	copyInto(result);
	return result;
    }


    public final Object getObject(int index) {
	return (Object)get(index);
    }

    public final Object[] toObjectArray() {
	Object[] result = new Object[size()];
	copyInto(result);
	return result;
    }


    public final Type getType(int index) {
	return (Type)get(index);
    }

    public final Type[] toTypeArray() {
	Type[] result = new Type[size()];
	copyInto(result);
	return result;
    }

}
