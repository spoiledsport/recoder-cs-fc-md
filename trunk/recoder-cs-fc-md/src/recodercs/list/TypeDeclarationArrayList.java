package recodercs.list;
import recodercs.ModelElement;
import recodercs.NamedModelElement;
import recodercs.abstraction.ClassType;
import recodercs.abstraction.Member;
import recodercs.abstraction.ProgramModelElement;
import recodercs.abstraction.Type;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.declaration.TypeDeclaration;

/**
   List implementation based on the doubling array technique for TypeDeclaration
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class TypeDeclarationArrayList
     extends AbstractArrayList
  implements TypeDeclarationMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public TypeDeclarationArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public TypeDeclarationArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public TypeDeclarationArrayList(TypeDeclaration[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public TypeDeclarationArrayList(TypeDeclaration a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected TypeDeclarationArrayList(TypeDeclarationArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new TypeDeclarationArrayList(this);
    }

    public final void set(int index, TypeDeclaration element) {
	super.set(index, element);
    }

    public final void insert(int index, TypeDeclaration element) {
	super.insert(index, element);
    }

    public final void insert(int index, TypeDeclarationList list) {
	super.insert(index, list);
    }

    public final void add(TypeDeclaration element) {
	super.add(element);
    }

    public final void add(TypeDeclarationList list) {
	super.add(list);
    }    

    public final TypeDeclaration getTypeDeclaration(int index) {
	return (TypeDeclaration)super.get(index);
    }

    public final TypeDeclaration[] toTypeDeclarationArray() {
	TypeDeclaration[] result = new TypeDeclaration[size()];
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


    public final ProgramElement getProgramElement(int index) {
	return (ProgramElement)get(index);
    }

    public final ProgramElement[] toProgramElementArray() {
	ProgramElement[] result = new ProgramElement[size()];
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


    public final ClassType getClassType(int index) {
	return (ClassType)get(index);
    }

    public final ClassType[] toClassTypeArray() {
	ClassType[] result = new ClassType[size()];
	copyInto(result);
	return result;
    }

}
