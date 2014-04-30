package gui;

import java.awt.AWTEvent;
import java.awt.Event;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class ComputerOptionsDialog extends JDialog implements ActionListener {


	boolean modalResult;
	
	JButton btnOk;
	JButton btnCancel;
	
	SetupData setupData;
	
	public ComputerOptionsDialog(Frame mainFrame, SetupData setupData) {
		super(mainFrame, "Computeroptionen", true);
		modalResult = true;
		this.setupData= setupData;
		
        JTabbedPane tabpane = new JTabbedPane
                (JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT );
		
        JPanel filesPanel = new FilesSetupPanel(setupData);
        tabpane.add ("Dateien", filesPanel);
        JPanel printSetupPanel = new PrintElementSetupPanel (setupData);
        tabpane.add ("Formular", printSetupPanel);

		add (tabpane);
		
        JPanel btnPanel = new JPanel ();
        btnOk = new JButton("OK");
        btnOk.addActionListener(this);
        btnOk.setActionCommand("ok");
        
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setActionCommand("cancel");

        btnPanel.add(btnOk);
        btnPanel.add(btnCancel);
        add("South", btnPanel);
        
        pack();

	}

	public boolean getModalResult() {
		return modalResult;
	}
	
    public void processEvent(AWTEvent e) {
        if (e.getID() == Event.WINDOW_DESTROY) {
            dispose();
        } else {
            super.processEvent(e);
        }
    }
    
    public void setSetupData (SetupData setupData) {
    	this.setupData= setupData; 
    }
    
    public SetupData getSetupData () {
    	return setupData;
    }

	@Override
	public void setVisible(boolean state) {
		
		if (state) {
		}
		super.setVisible(state);
	}
    
	@Override
	public void actionPerformed(ActionEvent event) {
		
		if ((event.getSource().equals(btnOk)) || (event.getSource().equals(btnCancel))) {
	    	modalResult = (event.getActionCommand().equals("ok"));
	    	setVisible(false);
		}
	}

}
