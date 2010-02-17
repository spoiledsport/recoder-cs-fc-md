package recodercs.list;
import recodercs.ModelElement;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.Using;

/**
   List implementation based on the doubling array technique for Using
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class UsingArrayList extends AbstractArrayList implements UsingMutableList {

	/**
	   Creates an empty list.
	   @since 0.01
	*/
	public UsingArrayList() {
		super();
	}

	/**
	   Creates an empty list that guarantees efficient addition of
	   up to the given amount of elements.
	   @since 0.01
	*/
	public UsingArrayList(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	   Create a list containing all elements of the given array.       
	   @since 0.01
	*/
	public UsingArrayList(Using[] a) {
		super(a);
	}

	/**
	   Creates a list with one element.
	   @since 0.03
	*/
	public UsingArrayList(Using a) {
		super(a);
	}

	/**
	   Create a new list as a deep clone of the given one.
	*/
	protected UsingArrayList(UsingArrayList proto) {
		super(proto);
	}

	public Object deepClone() {
		return new UsingArrayList(this);
	}

	public final void set(int index, Using element) {
		super.set(index, element);
	}

	public final void insert(int index, Using element) {
		super.insert(index, element);
	}

	public final void insert(int index, UsingList list) {
		super.insert(index, list);
	}

	public final void add(Using element) {
		super.add(element);
	}

	public final void add(UsingList list) {
		super.add(list);
	}

	public final Using getUsing(int index) {
		return (Using) super.get(index);
	}

	public final Using[] toUsingArray() {
		Using[] result = new Using[size()];
		copyInto(result);
		return result;
	}

	public final ModelElement getModelElement(int index) {
		return (ModelElement) get(index);
	}

	public final ModelElement[] toModelElementArray() {
		ModelElement[] result = new ModelElement[size()];
		copyInto(result);
		return result;
	}

	public final Object getObject(int index) {
		return (Object) get(index);
	}

	public final Object[] toObjectArray() {
		Object[] result = new Object[size()];
		copyInto(result);
		return result;
	}

	public final ProgramElement getProgramElement(int index) {
		return (ProgramElement) get(index);
	}

	public final ProgramElement[] toProgramElementArray() {
		ProgramElement[] result = new ProgramElement[size()];
		copyInto(result);
		return result;
	}

}
