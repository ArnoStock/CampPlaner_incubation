package rosterDB;

import gui.SetupData;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class RosterDBPanel extends JPanel implements ActionListener, ListSelectionListener {

	private RosterTableModel rosterTableModel;
	private JTable rosterTable;
	private RosterDB rosterDB;
	
	private JTextField familyNameField;
	private JTextField givenNameField;
	private JTextField nickNameField;
	private JTextField phoneNumberField;
	
	private JButton okButton;
	private JButton cancelButton;
	private JButton deleteButton;
	private JButton editButton;
	private JButton newButton;
	private JScrollPane listScroller;
	
	private JButton absentButton;
	private JButton participatesButton;
	private JButton vacationButton;
	private JButton availableButton;
	private Checkbox isAspirantCheckBox;
	
	public RosterDBPanel(RosterDB rosterDB, SetupData setupData) {
		super ();
		// create table showing roster availability
		setBackground(Color.GREEN);
	    setLayout(new BorderLayout());
		// 
		rosterTableModel = new RosterTableModel(rosterDB.getRosters(), setupData);
		rosterTable = new JTable(rosterTableModel);
		rosterTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            
			@Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
 
                @SuppressWarnings("unused")
				Component c = super.getTableCellRendererComponent(rosterTable, value,
                        isSelected, hasFocus, row, column);
                
				if (value instanceof RosterAvailability) {
					setBackground(((RosterAvailability) value).getRosterAvailabilityBackGroundColor (isSelected ));
					setForeground(((RosterAvailability) value).getRosterAvailabilityForeGroundColor (isSelected ));
	                setHorizontalAlignment(JLabel.CENTER);
				} 
				else if (column < 2) {
						setBackground(new Color (220,220,220));
						setForeground(Color.BLACK);
		                setHorizontalAlignment(JLabel.LEADING);
				}
				else if (!isSelected) {
					setBackground(Color.WHITE);
					setForeground(Color.BLACK);
	                setHorizontalAlignment(JLabel.LEADING);
				}
				
				return this;
            }
        });
		rosterTable.setCellSelectionEnabled(true);
		rosterTable.setColumnSelectionAllowed(true);
		rosterTable.getTableHeader().setReorderingAllowed(false);
		rosterTable.getSelectionModel().addListSelectionListener( this );
		
		rosterTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 2; i < rosterTable.getColumnCount(); i++)
			rosterTable.getColumnModel().getColumn(i).setPreferredWidth(45);

		listScroller = new JScrollPane(rosterTable);
		listScroller.setPreferredSize(rosterTable.getPreferredSize());
	
		// add pane containing elements for data editing
		JPanel editPane = new JPanel (new GridBagLayout ());
		GridBagConstraints cl = new GridBagConstraints();
		GridBagConstraints cd = new GridBagConstraints();
		cl.anchor = GridBagConstraints.WEST;
		cl.gridx = 0; cl.gridy = 0; cl.insets = new Insets (2,5,1,1);
		cl.fill = GridBagConstraints.NONE;
		cd.anchor = GridBagConstraints.WEST;
		cd.gridx = 1; cd.gridy = 0; cd.insets = new Insets (1,1,1,5); 
		cd.fill = GridBagConstraints.HORIZONTAL;
		
		// family name
		editPane.add(new JLabel ("Name: "), cl);
		familyNameField = new JTextField ("<Nachname>");
		familyNameField.setPreferredSize(new Dimension ( 150, (int) familyNameField.getMinimumSize().getHeight() ));
		editPane.add (familyNameField, cd);
		cl.gridy += 1; cd.gridy += 1;

		// given name
		editPane.add(new JLabel ("Vorname: "), cl);
		givenNameField = new JTextField ("<Vorname>");
		editPane.add (givenNameField, cd);
		cl.gridy += 1; cd.gridy += 1;

		// nick name
		editPane.add(new JLabel ("Spitzname: "), cl);
		nickNameField = new JTextField ("");
		editPane.add (nickNameField, cd);
		cl.gridy += 1; cd.gridy += 1;

		// phone
		editPane.add(new JLabel ("Handy: "), cl);
		phoneNumberField = new JTextField ("+49 ");
		editPane.add (phoneNumberField, cd);
		cl.gridy += 1; cd.gridy += 1;
		
		// aspirant
		editPane.add(new JLabel ("Aspirant: "), cl);
		JPanel p = new JPanel (new FlowLayout (FlowLayout.LEADING));
		isAspirantCheckBox = new Checkbox ();
		p.add(isAspirantCheckBox);
		editPane.add (p, cd);
		cl.gridy += 1; cd.gridy += 1;

		editPane.setMinimumSize(editPane.getPreferredSize());

		setEditFieldsEditable (false);
		
		// Buttons
		newButton = new JButton ("Neu");
		newButton.addActionListener(this);
		editButton = new JButton ("Bearbeiten");
		editButton.addActionListener(this);
		deleteButton = new JButton ("L\u00F6schen");
		deleteButton.addActionListener(this);
		okButton = new JButton ("Ok");
		okButton.addActionListener(this);
		cancelButton = new JButton ("Abbruch");
		cancelButton.addActionListener(this);
		setButtonMode (0);
		JPanel buttonPanel = new JPanel (new BorderLayout ());
		JPanel buttonPanelNorth = new JPanel ();
		JPanel buttonPanelSouth = new JPanel ();
		buttonPanelNorth.add(newButton);
		buttonPanelNorth.add(deleteButton);
		buttonPanelSouth.add(editButton);
		buttonPanelSouth.add(cancelButton);
		buttonPanelSouth.add(okButton, BorderLayout.EAST);
		buttonPanel.add(buttonPanelNorth, BorderLayout.NORTH);
		buttonPanel.add(buttonPanelSouth, BorderLayout.CENTER);
	
		JPanel outerShellPanel = new JPanel();
		JPanel statusPanel = new JPanel ( new GridBagLayout ());
		cl = new GridBagConstraints();
		GridBagConstraints cb = new GridBagConstraints();
		cl.anchor = GridBagConstraints.WEST;
		cl.gridx = 0; cl.gridy = 0; cl.insets = new Insets (2,5,1,1);
		cl.fill = GridBagConstraints.HORIZONTAL;
		cb.anchor = GridBagConstraints.WEST;
		cb.gridx = 1; cb.gridy = 0; cb.insets = new Insets (2,1,1,5); 
		cb.fill = GridBagConstraints.HORIZONTAL;

		JLabel absentLabel = new JLabel (RosterAvailability.getAvailabilityString(RosterAvailability.ROSTER_ABSENT));
		absentLabel.setBackground(RosterAvailability.getRosterAvailabilityBackGroundColor(RosterAvailability.ROSTER_ABSENT, false));
		absentLabel.setOpaque(true);
		absentLabel.setPreferredSize(new Dimension (25,25));
		statusPanel.add (absentLabel, cl);
		absentButton = new JButton ("Abwesend");
		absentButton.addActionListener(this);
		statusPanel.add (absentButton, cb);
		cl.gridy += 1; cb.gridy += 1;

		JLabel participatesLabel = new JLabel (RosterAvailability.getAvailabilityString(RosterAvailability.ROSTER_PARTICIPATES));
		participatesLabel.setBackground(RosterAvailability.getRosterAvailabilityBackGroundColor(RosterAvailability.ROSTER_PARTICIPATES, false));
		participatesLabel.setOpaque(true);
		participatesLabel.setPreferredSize(new Dimension (25,25));
		statusPanel.add (participatesLabel, cl);
		participatesButton = new JButton ("Teilnehmer");
		participatesButton.addActionListener(this);
		statusPanel.add (participatesButton, cb);
		cl.gridy += 1; cb.gridy += 1;

		JLabel vacationLabel = new JLabel (RosterAvailability.getAvailabilityString(RosterAvailability.ROSTER_VACATION));
		vacationLabel.setBackground(RosterAvailability.getRosterAvailabilityBackGroundColor(RosterAvailability.ROSTER_VACATION, false));
		vacationLabel.setOpaque(true);
		vacationLabel.setPreferredSize(new Dimension (25,25));
		statusPanel.add (vacationLabel, cl);
		vacationButton = new JButton ("Urlaub");
		vacationButton.addActionListener(this);
		statusPanel.add (vacationButton, cb);
		cl.gridy += 1; cb.gridy += 1;

		JLabel availableLabel = new JLabel (RosterAvailability.getAvailabilityString(RosterAvailability.ROSTER_AVAILABLE));
		availableLabel.setBackground(RosterAvailability.getRosterAvailabilityBackGroundColor(RosterAvailability.ROSTER_AVAILABLE, false));
		availableLabel.setOpaque(true);
		availableLabel.setPreferredSize(new Dimension (25,25));
		statusPanel.add (availableLabel, cl);
		availableButton = new JButton ("Aktiv");
		availableButton.addActionListener(this);
		statusPanel.add (availableButton,cb);
		outerShellPanel.add (statusPanel);
		
		//Create a split pane with the two scroll panes in it.
		JPanel editPaneContainer = new JPanel (new BorderLayout ());
		editPaneContainer.add(editPane, BorderLayout.NORTH);
		editPaneContainer.add(buttonPanel, BorderLayout.CENTER);
		editPaneContainer.add(outerShellPanel, BorderLayout.SOUTH);
		
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
        		listScroller, editPaneContainer);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(500);

        //Provide a preferred size for the split pane.
        splitPane.setPreferredSize(new Dimension(600, 300));
        add (splitPane);
        
		setRosterDB(rosterDB);

		if (rosterTable.getComponentCount() > 0)
			rosterTable.setRowSelectionInterval(0, 0);
		
	}
	
	private void refreshEditDataFrom(Roster selRoster) {
		
		familyNameField.setText(selRoster.getFamilyName());
		givenNameField.setText(selRoster.getGivenName());
		nickNameField.setText(selRoster.getNickName());
		phoneNumberField.setText(selRoster.getPhoneNumber());
		isAspirantCheckBox.setState(selRoster.getIsAspirant());
	}

	private void refreshEditData() {

		int selRow = rosterTable.getSelectedRow();
		if (selRow >= 0) {
			refreshEditDataFrom (rosterDB.getRosters().get(selRow));
		}
	}
	
	private void storeEditData () {
		
		int selRow = rosterTable.getSelectedRow();
		if (selRow >= 0) {
			Roster selRoster = rosterDB.getRosters().get(selRow);
			selRoster.setFamilyName (familyNameField.getText());
			selRoster.setGivenName (givenNameField.getText());
			selRoster.setNickName(nickNameField.getText());
			selRoster.setPhoneNumber(phoneNumberField.getText());
			selRoster.setIsAspirant(isAspirantCheckBox.getState());
		}
	}

	
	// 0: copy - delete - edit enabled
	// 1: ok - cancel enabled
	private void setButtonMode(Integer bm) {

		if (bm == 0) {
			
			okButton.setEnabled(false);
			cancelButton.setEnabled(false);
			deleteButton.setEnabled (true);
			editButton.setEnabled(true);
			newButton.setEnabled(true);
		}
		
		if (bm == 1) {

			okButton.setEnabled(true);
			cancelButton.setEnabled(true);
			deleteButton.setEnabled (false);
			editButton.setEnabled(false);
			newButton.setEnabled(false);
		}
		
	}

	private void setEditFieldsEditable(boolean ed) {
		
		familyNameField.setEditable (ed);
		givenNameField.setEditable(ed);
		nickNameField.setEditable(ed);
		phoneNumberField.setEditable(ed);
		isAspirantCheckBox.setEnabled(ed);
	}

	private void setRosterDB(RosterDB rosterDB) {
		this.rosterDB = rosterDB;
		listScroller.setPreferredSize(rosterTable.getPreferredSize());
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		
		if (evt.getSource().equals(newButton)) {
			
			refreshEditDataFrom(rosterDB.newRoster ());

			setEditFieldsEditable(true);		
			rosterTable.setEnabled(false);
			setButtonMode(1);
		}

		if (evt.getSource().equals(editButton)) {
			
			setEditFieldsEditable(true);		
			rosterTable.setEnabled(false);
			setButtonMode(1);
		}
		
		if (evt.getSource().equals(okButton)) {
			
			setEditFieldsEditable(false);		
			rosterTable.setEnabled(true);
			setButtonMode(0);
			// store changed data to object
			storeEditData ();
		}

		if (evt.getSource().equals(cancelButton)) {
			
			setEditFieldsEditable(false);		
			rosterTable.setEnabled(true);
			setButtonMode(0);
	        refreshEditData ();
		}
		
		if (evt.getSource().equals(availableButton)) {
			setSelectedCellContents (RosterAvailability.ROSTER_AVAILABLE);
		}
		if (evt.getSource().equals(vacationButton)) {
			setSelectedCellContents (RosterAvailability.ROSTER_VACATION);
		}
		if (evt.getSource().equals(participatesButton)) {
			setSelectedCellContents (RosterAvailability.ROSTER_PARTICIPATES);
		}
		if (evt.getSource().equals(absentButton)) {
			setSelectedCellContents (RosterAvailability.ROSTER_ABSENT);
		}

	}
	
	private void setSelectedCellContents (int availabilityCode) {
		
		for (int column: rosterTable.getSelectedColumns()) {
			for (int row: rosterTable.getSelectedRows()) {
				rosterTableModel.setValueAt(availabilityCode, row, column);
			}
		}
		rosterTable.invalidate();
		rosterTable.repaint();
	}

	@Override
	public void valueChanged(ListSelectionEvent evt) {
		refreshEditData();
	}

}