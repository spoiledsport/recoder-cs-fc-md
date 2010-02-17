package recodercs.list;
import recodercs.kit.Transformation;
import recodercs.kit.TwoPassTransformation;

/**
   List implementation based on the doubling array technique for TwoPassTransformation
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class TwoPassTransformationArrayList
     extends AbstractArrayList
  implements TwoPassTransformationMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public TwoPassTransformationArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public TwoPassTransformationArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public TwoPassTransformationArrayList(TwoPassTransformation[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public TwoPassTransformationArrayList(TwoPassTransformation a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected TwoPassTransformationArrayList(TwoPassTransformationArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new TwoPassTransformationArrayList(this);
    }

    public final void set(int index, TwoPassTransformation element) {
	super.set(index, element);
    }

    public final void insert(int index, TwoPassTransformation element) {
	super.insert(index, element);
    }

    public final void insert(int index, TwoPassTransformationList list) {
	super.insert(index, list);
    }

    public final void add(TwoPassTransformation element) {
	super.add(element);
    }

    public final void add(TwoPassTransformationList list) {
	super.add(list);
    }    

    public final TwoPassTransformation getTwoPassTransformation(int index) {
	return (TwoPassTransformation)super.get(index);
    }

    public final TwoPassTransformation[] toTwoPassTransformationArray() {
	TwoPassTransformation[] result = new TwoPassTransformation[size()];
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


    public final Transformation getTransformation(int index) {
	return (Transformation)get(index);
    }

    public final Transformation[] toTransformationArray() {
	Transformation[] result = new Transformation[size()];
	copyInto(result);
	return result;
    }

}
