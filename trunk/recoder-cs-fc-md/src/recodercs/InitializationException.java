// This file is part of the RECODER library and protected by the LGPL.

package recodercs;

/**
 Initialization exception.
 @author <TT>AutoDoc</TT>
 */
public class InitializationException extends RuntimeException {

    Throwable cause;
    
    /**
     Initialization exception.
     */
    public InitializationException() {}

    /**
     Initialization exception.
     @param s a string.
     */
    public InitializationException(String s) {
        super(s);
    }
    
    public InitializationException(Throwable c) {
    	cause = c;
    }
	/**
	 * Returns the cause.
	 * @return Throwable
	 */
	public Throwable getCause() {
		return cause;
	}

}
