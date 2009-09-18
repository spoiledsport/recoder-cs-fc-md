package recoder.abstraction;

/**
 * DeclaredType.java
 * @author orosz
 *
 * A new abstract interface for all declared types (namespace and class members).
 * This abstraction was needed, since in C# these types are not only class types,
 * but can be enums and delegates too. These types must be handled differently, since
 * they don't have for example supertypes, and stuff like that.
 */
public interface DeclaredType extends Type, Member {
	
	/**
     Checks if this member is abstract. An interface will report
     <CODE>true</CODE>.
     @return <CODE>true</CODE> if this member is abstract,
     <CODE>false</CODE> otherwise.
      @see #isInterface()
     */
    boolean isAbstract();

    /**
       Returns the namespace this element is defined in. Namespaces
       have no recursive scope and report themselves.
       @return the package of this element.
     */
    Namespace getNamespace();
    
    /**
       Returns the enclosing package or class type, or method.
       A package will report <tt>null</tt>, a methods its enclosing
       class.
       @return the container of this element.
     */
    DeclaredTypeContainer getContainer();
	
}
