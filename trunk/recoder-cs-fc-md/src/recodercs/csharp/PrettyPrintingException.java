// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp;

import java.io.IOException;

/**
   This runtime exception wraps an IOException thrown by the
   pretty printer's writer.
 */
public class PrettyPrintingException extends RuntimeException {

    private IOException ioe;

    public PrettyPrintingException(IOException ioe) {
	this.ioe = ioe;
    }

    public IOException getWrappedException() {
	return ioe;
    }

    public String toString() {
	return ioe.toString();
    }
}
