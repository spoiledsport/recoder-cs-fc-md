// This file is part of the RECODER library and protected by the LGPL.

package recodercs.service;

import recodercs.service.ChangeHistory;
import recodercs.csharp.*;
import recodercs.list.*;
import recodercs.util.*;

/**
   Record of the syntactical changes that occured after the
   last validation of the model.
   @author AL
   @since 0.5
 */
public class ChangeHistoryEvent extends java.util.EventObject {

    private TreeChangeList changeList;

    ChangeHistoryEvent(ChangeHistory source, 
		       TreeChangeList changeList) {
	super(source);
	this.changeList = changeList;
    }

    /**
       Returns the series of changes.
     */
    public TreeChangeList getChanges() {
	return changeList;
    }

    public String toString() {
	StringBuffer res = new StringBuffer();
	for (int i = 0; i < changeList.size(); i += 1) {
	    res.append(changeList.getTreeChange(i).toString());
	    res.append("\n");
	}
	return res.toString();
    }
}
