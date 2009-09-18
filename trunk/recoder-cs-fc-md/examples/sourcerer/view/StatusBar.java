package sourcerer.view;

import sourcerer.*;
import sourcerer.util.*;

import recoder.*;
import recoder.abstraction.*;
import recoder.bytecode.*;
import recoder.csharp.*;
import recoder.csharp.declaration.*;
import recoder.convenience.*;
import recoder.service.*;
import recoder.util.*;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

public class StatusBar
	extends JPanel
	implements SelectionView, ProgressListener {

	JProgressBar progressBar;
	MemoryDisplay memory;
	JLabel selected;
	JPanel cardPanel;
	CardLayout cardLayout;
	SelectionModel model;
	CrossReferenceServiceConfiguration config;

	final static javax.swing.border.Border BORDER =
		BorderFactory.createCompoundBorder(
			BorderFactory.createEmptyBorder(2, 2, 2, 2),
			BorderFactory.createCompoundBorder(
				SwingUtils.createThinLoweredBevelBorder(),
				BorderFactory.createEmptyBorder(0, 2, 0, 2)));

	public StatusBar(
		CrossReferenceServiceConfiguration config,
		SelectionModel model) {
		super(new GridBagLayout());
		this.config = config;
		GridBagConstraints gbc = new GridBagConstraints();
		selected = new JLabel();
		selected.setForeground(getForeground());
		selected.setBorder(BORDER);
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setString("");
		progressBar.setFont(progressBar.getFont().deriveFont(9f));
		progressBar.setBorder(BORDER);
		cardPanel = new JPanel(cardLayout = new CardLayout());
		cardPanel.add(selected, "Element");
		cardPanel.add(progressBar, "Progress");

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 3;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		add(cardPanel, gbc);
		memory = new MemoryDisplay();
		memory.setForeground(getForeground());
		memory.setBorder(BORDER);
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		add(memory, gbc);
		setModel(model);
	}

	public JLabel getSelectionLabel() {
		return selected;
	}

	public SelectionModel getModel() {
		return model;
	}

	public void setModel(SelectionModel model) {
		if (this.model != model) {
			if (this.model != null) {
				this.model.removeChangeListener(changeListener);
			}
			if (model == null) {
				model = new DefaultSelectionModel();
			}
			model.addChangeListener(changeListener);
			this.model = model;
			updateView();
		}
	}

	public void modelUpdated(boolean selectionRemoved) {
		updateView();
	}

	protected final ChangeListener changeListener = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			updateView();
		}
	};

	void updateView() {
		if (model == null) {
			selected.setText("");
		} else {
			ModelElement m = model.getSelectedElement();
			if (m == null) {
				selected.setText("No element selected");
				return;
			}
			StringBuffer text = new StringBuffer();
			if ((m instanceof Member) || (m instanceof Variable)) {
				if (m instanceof Member) {
					Member mb = (Member) m;
					if (mb.isPublic()) {
						text.append("public ");
					} else if (mb.isProtected()) {
						text.append("protected ");
					} else if (mb.isPrivate()) {
						text.append("private ");
					}
					if (mb.isStatic()) {
						text.append("static ");
					} else if (mb instanceof Method) {
						if (((Method) mb).isAbstract()) {
							text.append("abstract ");
						}
					} else if (mb instanceof ClassType) {
						if (((ClassType) mb).isAbstract()) {
							text.append("abstract ");
						}
					}
					if (mb.isSealed()) {
						text.append("final ");
					}
					if (mb instanceof Method) {
						if (((Method) mb).isExtern()) {
							text.append("extern ");
						}
					}
				}
				if ((m instanceof Variable) && !(m instanceof Field)) {
					if (((Variable) m).isReadOnly()) {
						text.append("readonly ");
					}
				}
			}
			String name = m.getClass().getName();
			text.append(name.substring(name.lastIndexOf('.') + 1));
			if (m instanceof CompilationUnit) {
				m = ((CompilationUnit) m).getPrimaryTypeDeclaration();
			}
			if (m instanceof NamedModelElement) {
				text.append(" \"");
				text.append(
					RecoderUtils.getNonTrivialName((NamedModelElement) m));
				text.append("\"");
			} else if (m instanceof Identifier) {
				text.append(" \"");
				text.append(((Identifier) m).getText());
				text.append("\"");
			}

			String constantValue = null;
			Expression expr = null;
			if (m instanceof VariableSpecification) {
				expr = ((VariableSpecification) m).getInitializer();
			} else if (m instanceof Expression) {
				expr = (Expression) m;
			}
			if (expr != null) {
				ConstantEvaluator.EvaluationResult evr =
					new ConstantEvaluator.EvaluationResult();
				if (config
					.getConstantEvaluator()
					.isCompileTimeConstant(expr, evr)) {
					constantValue = evr.toString();
				}
			}
			if (constantValue != null) {
				text.append(" (constant value ");
				text.append(constantValue);
				text.append(")");
			}

			selected.setText(text.toString());

		}
	}

	public void workProgressed(ProgressEvent pe) {
		int max = pe.getWorkToDoCount();
		int val = pe.getWorkDoneCount();
		String msg = "";
		if (val == max) {
			val = 0;
			cardLayout.show(cardPanel, "Element");
		} else {
			cardLayout.show(cardPanel, "Progress");
			Object state = pe.getState();
			if (state != null) {
				msg = state.toString();
			}
			if (val > max) {
				max = val;
				// val = 0;
			}
		}
		progressBar.setValue(val);
		progressBar.setMaximum(max);
		progressBar.setString(msg);
	}

}
