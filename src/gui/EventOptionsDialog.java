package gui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

@SuppressWarnings("serial")
public class EventOptionsDialog extends JDialog implements ActionListener, ChangeListener {

	boolean modalResult;
	
	JDatePicker startDatePicker;
	JDatePicker endDatePicker;
	
	JButton btnOk;
	JButton btnCancel;
	
	String region;
	Calendar eventStart;
	Calendar eventEnd;
	
	public EventOptionsDialog(Frame mainFrame) {
		super(mainFrame, "Veranstaltungsoptionen", true);
		modalResult = true;
		eventStart = Calendar.getInstance();
		eventEnd = Calendar.getInstance();
		
		JPanel textPanel = new JPanel(new FlowLayout (FlowLayout.LEADING));
		textPanel.add(new JLabel("Erster Veranstaltungstag - letzter Veranstaltungstag"));
		add ("North", textPanel);
		
        JPanel datePanel = new JPanel ();

		new JDateComponentFactory();
		startDatePicker = JDateComponentFactory.createJDatePicker();
		startDatePicker.setTextEditable(true);
		startDatePicker.setShowYearButtons(true);
		startDatePicker.getModel().addChangeListener(this);
		datePanel.add((Component) startDatePicker); 
		
		endDatePicker = JDateComponentFactory.createJDatePicker();
		endDatePicker.setTextEditable(true);
		endDatePicker.setShowYearButtons(true);
		endDatePicker.getModel().addChangeListener(this);
		datePanel.add((Component) endDatePicker);
		
		add (datePanel);
		
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
        setResizable(false);
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

	@Override
	public void setVisible(boolean state) {
		
		if (state) {
			eventStart.setTime(MainWindow.setupData.getEventStartDate());
			startDatePicker.getModel().setDate(	eventStart.get(Calendar.YEAR), 
												eventStart.get(Calendar.MONTH), 
												eventStart.get(Calendar.DATE));
			startDatePicker.getModel().setSelected(true);
			
			eventEnd.setTime(MainWindow.setupData.getEventEndDate());
			endDatePicker.getModel().setDate(	eventEnd.get(Calendar.YEAR), 
												eventEnd.get(Calendar.MONTH), 
												eventEnd.get(Calendar.DATE));
			endDatePicker.getModel().setSelected(true);
		}
		super.setVisible(state);
	}
    
	@Override
	public void actionPerformed(ActionEvent event) {
		
		if ((event.getSource().equals(btnOk)) || (event.getSource().equals(btnCancel))) {
	    	modalResult = (event.getActionCommand().equals("ok"));
	    	MainWindow.setupData.setEventStartDate(eventStart.getTime());
	    	MainWindow.setupData.setEventEndDate(eventEnd.getTime());
	    	setVisible(false);
		}
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		
		if (event.getSource().equals(startDatePicker.getModel())) {

			eventStart.set(	startDatePicker.getModel().getYear(), 
							startDatePicker.getModel().getMonth(), 
							startDatePicker.getModel().getDay());
			if (eventStart.after(eventEnd)) {
				eventEnd = (Calendar) eventStart.clone ();
				eventEnd.add(Calendar.DATE, 1);
				endDatePicker.getModel().setDate(	eventEnd.get(Calendar.YEAR), 
													eventEnd.get(Calendar.MONTH), 
													eventEnd.get(Calendar.DATE));
				endDatePicker.getModel().setSelected(true);
			}
			
		}
		
		if (event.getSource().equals(endDatePicker.getModel())) {
			eventEnd.set(	endDatePicker.getModel().getYear(), 
							endDatePicker.getModel().getMonth(), 
							endDatePicker.getModel().getDay());
			if (eventStart.after(eventEnd)) {
				eventStart = (Calendar) eventEnd.clone ();
				eventStart.add(Calendar.DATE, -1);
				startDatePicker.getModel().setDate(	eventStart.get(Calendar.YEAR), 
													eventStart.get(Calendar.MONTH), 
													eventStart.get(Calendar.DATE));
				startDatePicker.getModel().setSelected(true);
			}
		}
		
	}
}
