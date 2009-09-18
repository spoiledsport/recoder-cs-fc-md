package sourcerer.view;

import java.text.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MemoryDisplay extends JLabel {
   
    private DecimalFormat format = new DecimalFormat("####.###");
    private String header = "Memory Used";
    private Timer timer;
    private final static int DELAY = 3000;
    
    public MemoryDisplay() {
	timer = new Timer(DELAY, new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
		    update();
		}
	    });
	addAncestorListener(new AncestorListener() {
		public void ancestorRemoved(AncestorEvent e) {
		    timer.stop();
		}
		public void ancestorMoved(AncestorEvent event) {}
		public void ancestorAdded(AncestorEvent e) {
		    timer.start();
		}
	    });
	update();
    }
    
    void update() {
	double free = Runtime.getRuntime().totalMemory();
	setText(header + ": " + format.format(free / (2 << 20)) + "MB");
    }
    
}
