// This file is part of the RECODER library and protected by the LGPL.

package recodercs.service;

/**
   Exception thrown by the change history in case of illegal change
   reports.
 */
public class IllegalChangeReportException extends RuntimeException {

    /**
       Creates a new illegal change report exception.
     */
    public IllegalChangeReportException() {}

    /**
       Creates a new illegal change report exception.
       @param msg a string.
     */
    public IllegalChangeReportException(String msg) {
        super(msg);
    }
}
