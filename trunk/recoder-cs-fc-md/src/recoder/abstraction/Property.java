package recoder.abstraction;

/**
 * Property.java
 * @author orosz
 *
 * 
 */
public interface Property extends Field {
	
	/**
	 * Checks if the property is write only.
	 * @return true if the property can only be set, false otherwise.
	 */
	public boolean isWriteOnly();
	
}
