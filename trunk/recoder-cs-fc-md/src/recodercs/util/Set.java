// This file is part of the RECODER library and protected by the LGPL.

package recodercs.util;

import java.util.*;

/**
  @author AL
*/
public interface Set {

    int size();
    boolean isEmpty();
    Enumeration elements();
    boolean contains(Object key);
    Object get(Object key);
    Set EMPTY_SET = new NaturalHashSet();
    // should be a special implementation that cannot be casted and modified
}
