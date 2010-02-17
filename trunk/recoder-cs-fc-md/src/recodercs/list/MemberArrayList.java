package recodercs.list;
import recodercs.ModelElement;
import recodercs.NamedModelElement;
import recodercs.abstraction.Member;
import recodercs.abstraction.ProgramModelElement;

/**
   List implementation based on the doubling array technique for Member
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class MemberArrayList
     extends AbstractArrayList
  implements MemberMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public MemberArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public MemberArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public MemberArrayList(Member[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public MemberArrayList(Member a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected MemberArrayList(MemberArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new MemberArrayList(this);
    }

    public final void set(int index, Member element) {
	super.set(index, element);
    }

    public final void insert(int index, Member element) {
	super.insert(index, element);
    }

    public final void insert(int index, MemberList list) {
	super.insert(index, list);
    }

    public final void add(Member element) {
	super.add(element);
    }

    public final void add(MemberList list) {
	super.add(list);
    }    

    public final Member getMember(int index) {
	return (Member)super.get(index);
    }

    public final Member[] toMemberArray() {
	Member[] result = new Member[size()];
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


    public final ProgramModelElement getProgramModelElement(int index) {
	return (ProgramModelElement)get(index);
    }

    public final ProgramModelElement[] toProgramModelElementArray() {
	ProgramModelElement[] result = new ProgramModelElement[size()];
	copyInto(result);
	return result;
    }

}
