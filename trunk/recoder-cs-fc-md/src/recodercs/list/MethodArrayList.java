package recodercs.list;
import recodercs.ModelElement;
import recodercs.NamedModelElement;
import recodercs.abstraction.Member;
import recodercs.abstraction.Method;
import recodercs.abstraction.ProgramModelElement;

/**
   List implementation based on the doubling array technique for Method
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class MethodArrayList
     extends AbstractArrayList
  implements MethodMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public MethodArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public MethodArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public MethodArrayList(Method[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public MethodArrayList(Method a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected MethodArrayList(MethodArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new MethodArrayList(this);
    }

    public final void set(int index, Method element) {
	super.set(index, element);
    }

    public final void insert(int index, Method element) {
	super.insert(index, element);
    }

    public final void insert(int index, MethodList list) {
	super.insert(index, list);
    }

    public final void add(Method element) {
	super.add(element);
    }

    public final void add(MethodList list) {
	super.add(list);
    }    

    public final Method getMethod(int index) {
	return (Method)super.get(index);
    }

    public final Method[] toMethodArray() {
	Method[] result = new Method[size()];
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


    public final Member getMember(int index) {
	return (Member)get(index);
    }

    public final Member[] toMemberArray() {
	Member[] result = new Member[size()];
	copyInto(result);
	return result;
    }

}
