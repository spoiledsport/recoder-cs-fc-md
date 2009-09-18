package sourcerer.view;

import sourcerer.*;
import sourcerer.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import recoder.*;
import recoder.abstraction.*;
import recoder.list.*;
import recoder.convenience.*;
import recoder.csharp.*;
import recoder.util.*;

public class HistoryView extends ListSelector {

    protected int maxEntryCount = 100;

    public HistoryView(SelectionModel model) {
	super(model, "Last Visits", ProgramElementList.EMPTY_LIST, "%c %n %u %p", false, false);
	selectorList.setModel(new DefaultListModel());
	selectionModel.addChangeListener(changeListener);	
    }

    protected ChangeListener changeListener = new ChangeListener() {
	    public void stateChanged(ChangeEvent e) {
		ModelElement p = selectionModel.getSelectedElement();
		if (p == null || p == selectorList.getSelectedValue()) {
		    return;
		}
		DefaultListModel m = (DefaultListModel)selectorList.getModel();
		int s = m.getSize();
		if (s == 0 || m.getElementAt(s - 1) != p) {
		    while (s > maxEntryCount) {
			m.remove(--s);
		    }
		    m.add(0, p);
		    selectorList.setSelectedIndex(0);
		}
	    }
	};


    public void back() {
	int i = selectorList.getSelectedIndex();
	if (i == -1 || i == selectorList.getModel().getSize() - 1) {
	    return;
	}
	selectorList.setSelectedIndex(i + 1);	
	selectionModel.setSelectedElement((ModelElement)selectorList.getSelectedValue());	
    }

    public void forward() {
	int i = selectorList.getSelectedIndex();
	if (i == -1 || i == 0) {
	    return;
	}
	selectorList.setSelectedIndex(i - 1);	
	selectionModel.setSelectedElement((ModelElement)selectorList.getSelectedValue());	
    }

    public void setModel(SelectionModel model) {
	if (selectionModel != model) {
	    if (selectionModel != null) {
		selectionModel.removeChangeListener(changeListener);
	    }
	    selectionModel = model;
	    if (model != null) {
		model.addChangeListener(changeListener);
	    }
	}       
    }

    public void modelUpdated(boolean selectionRemoved) {
	DefaultListModel m = (DefaultListModel)selectorList.getModel();
	m.clear();
    }

}


