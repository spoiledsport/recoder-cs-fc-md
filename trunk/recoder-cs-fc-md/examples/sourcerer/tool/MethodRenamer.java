package sourcerer.tool;

import sourcerer.*;
import sourcerer.view.*;
import sourcerer.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import recoder.*;
import recoder.abstraction.*;
import recoder.list.*;
import recoder.service.*;
import recoder.kit.*;
import recoder.kit.transformation.*;
import recoder.java.*;
import recoder.java.declaration.*;
import recoder.java.reference.*;
import recoder.util.*;

public class MethodRenamer extends SourcererWizard implements SelectionView {

    MethodDeclaration method;
    String newName = "";
    RenameMethod rm;

    static final String TITLE_HEADER = "Method Renamer";

    public MethodRenamer(Main main, MethodDeclaration md) {
	super(main);
	setName(TITLE_HEADER);
	method = md;
	start(descriptionState);
    }

    State descriptionState = new State() {

	    JPanel panel = new JPanel(new BorderLayout());
	    
	    public void stateEntered(Wizard w) {
		w.getNextButton().setToolTipText("Find declarations and references to rename");
		w.getNextButton().setEnabled(false);
		w.getFinishButton().setEnabled(false);
		w.setComponent(panel);
		panel.add(message, BorderLayout.NORTH);
		message.setText("This wizard renames a method.\n" + 
				"Start the analysis, or abort.");
		JPanel p = new JPanel(new BorderLayout());
		final JTextField textField = new JTextField(newName);
		textField.setBorder(BorderFactory.createTitledBorder("Enter new name for method"));
		p.add(textField, BorderLayout.NORTH);
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    // TODO: check for valid Java ID
			    newName = textField.getText();
			    if (newName.length() > 0) {
				getNextButton().setEnabled(true);
			    }
			}
		    });
		panel.add(p);
		w.setComponent(panel);
	    }
	    
	    public State nextState() {
		return analysisState;
	    }
	};

    State analysisState = new State() {

	    ModelElementList list;
	    JPanel panel = new JPanel(new BorderLayout());
	    
	    public void stateEntered(Wizard w) {
		if (list == null) {
		    panel.add(message, BorderLayout.NORTH);
		    w.setComponent(panel);
		    message.setText("Analyzing method context...");

		    rm = new RenameMethod(xconfig, method, newName);
		    disableButtons();
		    new Thread(new Runnable() {
			    public void run() {
				ProblemReport pr = rm.analyze();
				if (pr instanceof Problem) {
				    displayFailure(pr);
				} else {
				    list = rm.getRenamedMethods();
				    displaySuccess();
				}
			    }}).start();
		} else {
		    displaySuccess();
		}
	    }

	    private void displayFailure(ProblemReport pr) {
		getBackButton().setEnabled(true);
		getNextButton().setEnabled(false);
		getCancelButton().setEnabled(true);
		message.setText("Found a problem: " + pr.toString() + "\n" +
				"Go back to change name, initiate renaming, or abort.");		
	    }
	    
	    private void displaySuccess() {
		getBackButton().setEnabled(true);
		getNextButton().setEnabled(true);
		getFinishButton().setEnabled(false);
		getCancelButton().setEnabled(true);
		SelectorList s = new SelectorList(getModel(), list, "%u %4p", true);
		s.setBorder(BorderFactory.createEtchedBorder());
		panel.add(new JScrollPane(s), BorderLayout.CENTER);
		message.setText("Found " + list.size() + " methods that have to be renamed.\n" + 
				"Go back to change name, initiate renaming, or abort.");
		getNextButton().setToolTipText("Rename everything needed");
		getBackButton().setToolTipText("Back to description");
		getNextButton().setEnabled(!list.isEmpty());
		getFinishButton().setEnabled(list.isEmpty());
		if (list.isEmpty()) {
		    getFinishButton().setToolTipText("Nothing to be done");
		}
	    }
	    public State nextState() {
		return transformationState;
	    }
	};
    

    State transformationState = new State() {
	    
	    public void stateEntered(Wizard w) {
		JPanel panel = new JPanel(new BorderLayout());
		w.setComponent(panel);
		TwoPassTransformationMutableList trans = new TwoPassTransformationArrayList(1);
		trans.add(rm);
		transform(panel, trans);
	    }

	    public State nextState() {
		return null;
	    }
	};
}

