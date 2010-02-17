// This file is part of the RECODER library and protected by the LGPL

package recodercs.kit;

/** this class implements basic functions for type handling.
    @author Dirk Heuzeroth
*/

public class NameClashException extends Exception {
    NameClashException() { super(); }
    NameClashException(String msg) { super(msg); }
}
