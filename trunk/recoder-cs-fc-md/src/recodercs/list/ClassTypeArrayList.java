package recodercs.list;
import recodercs.ModelElement;
import recodercs.NamedModelElement;
import recodercs.abstraction.ClassType;
import recodercs.abstraction.Member;
import recodercs.abstraction.ProgramModelElement;
import recodercs.abstraction.Type;

/**
   List implementation based on the doubling array technique for ClassType
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class ClassTypeArrayList
     extends AbstractArrayList
  implements ClassTypeMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public ClassTypeArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public ClassTypeArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public ClassTypeArrayList(ClassType[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public ClassTypeArrayList(ClassType a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected ClassTypeArrayList(ClassTypeArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new ClassTypeArrayList(this);
    }

    public final void set(int index, ClassType element) {
	super.set(index, element);
    }

    public final void insert(int index, ClassType element) {
	super.insert(index, element);
    }

    public final void insert(int index, ClassTypeList list) {
	super.insert(index, list);
    }

    public final void add(ClassType element) {
	super.add(element);
    }

    public final void add(ClassTypeList list) {
	super.add(list);
    }    

    public final ClassType getClassType(int index) {
	return (ClassType)super.get(index);
    }

    public final ClassType[] toClassTypeArray() {
	ClassType[] result = new ClassType[size()];
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
