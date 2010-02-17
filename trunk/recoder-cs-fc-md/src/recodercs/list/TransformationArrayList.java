package recodercs.list;
import recodercs.kit.Transformation;

/**
   List implementation based on the doubling array technique for Transformation
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class TransformationArrayList
     extends AbstractArrayList
  implements TransformationMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public TransformationArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public TransformationArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public TransformationArrayList(Transformation[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public TransformationArrayList(Transformation a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected TransformationArrayList(TransformationArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new TransformationArrayList(this);
    }

    public final void set(int index, Transformation element) {
	super.set(index, element);
    }

    public final void insert(int index, Transformation element) {
	super.insert(index, element);
    }

    public final void insert(int index, TransformationList list) {
	super.insert(index, list);
    }

    public final void add(Transformation element) {
	super.add(element);
    }

    public final void add(TransformationList list) {
	super.add(list);
    }    

    public final Transformation getTransformation(int index) {
	return (Transformation)super.get(index);
    }

    public final Transformation[] toTransformationArray() {
	Transformation[] result = new Transformation[size()];
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

}
