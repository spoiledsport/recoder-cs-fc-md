package recoder.list;
import recoder.csharp.declaration.MethodDeclaration;
import recoder.abstraction.Member;
import recoder.abstraction.ProgramModelElement;
import recoder.NamedModelElement;
import recoder.csharp.declaration.MemberDeclaration;
import recoder.csharp.ProgramElement;
import recoder.ModelElement;
import recoder.abstraction.Method;
// This file is part of the RECODER library and protected by the LGPL.

/**
   List implementation based on the doubling array technique for MethodDeclaration
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class MethodDeclarationArrayList
     extends AbstractArrayList
  implements MethodDeclarationMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public MethodDeclarationArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public MethodDeclarationArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public MethodDeclarationArrayList(MethodDeclaration[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public MethodDeclarationArrayList(MethodDeclaration a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected MethodDeclarationArrayList(MethodDeclarationArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new MethodDeclarationArrayList(this);
    }

    public final void set(int index, MethodDeclaration element) {
	super.set(index, element);
    }

    public final void insert(int index, MethodDeclaration element) {
	super.insert(index, element);
    }

    public final void insert(int index, MethodDeclarationList list) {
	super.insert(index, list);
    }

    public final void add(MethodDeclaration element) {
	super.add(element);
    }

    public final void add(MethodDeclarationList list) {
	super.add(list);
    }    

    public final MethodDeclaration getMethodDeclaration(int index) {
	return (MethodDeclaration)super.get(index);
    }

    public final MethodDeclaration[] toMethodDeclarationArray() {
	MethodDeclaration[] result = new MethodDeclaration[size()];
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


    public final MemberDeclaration getMemberDeclaration(int index) {
	return (MemberDeclaration)get(index);
    }

    public final MemberDeclaration[] toMemberDeclarationArray() {
	MemberDeclaration[] result = new MemberDeclaration[size()];
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


    public final Method getMethod(int index) {
	return (Method)get(index);
    }

    public final Method[] toMethodArray() {
	Method[] result = new Method[size()];
	copyInto(result);
	return result;
    }

}
