package riverDB;


import gui.MainWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class RiverDBPanel extends JPanel implements ListSelectionListener, ActionListener {

	private DefaultListModel<River> riverListModel;
	private JList<River> riverList;
	
	private JTextField riverNameField;
	private JTextField fromNameField;
	private JTextField toNameField;
	private JTextField distanceField;
	private JTextField lengthField;
	private JTextField wwLevelField;
	private JTextField maxWaterLevelField;
	private JTextField minWaterLevelField;
	private JTextField defaultGroupSizeField;
	
	private JButton okButton;
	private JButton cancelButton;
	private JButton copyButton;
	private JButton deleteButton;
	private JButton editButton;
	private JButton newButton;
	private JLabel maxWaterLevelUnitField;
	private JLabel minWaterLevelUnitField;
	private JComboBox<String> unitOfWaterLevelCombo;
	private JComboBox<Integer> wwTopLevelCombo;
	
	public RiverDBPanel() {
		super ();
		// setup the riverDB GUI
		setBackground(Color.BLUE);
	    setLayout(new BorderLayout());
		
		riverListModel = new DefaultListModel<River>();
		riverList = new JList<River>(riverListModel);
		riverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		riverList.setSelectedIndex(0);
		riverList.addListSelectionListener(this);
		JScrollPane listScroller = new JScrollPane(riverList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		
		// add pane containing elements for data editing
		JPanel editPane = new JPanel (new GridBagLayout ());
		JScrollPane editScroller = new JScrollPane(editPane);
		GridBagConstraints cl = new GridBagConstraints();
		GridBagConstraints cd = new GridBagConstraints();
		cl.anchor = GridBagConstraints.WEST;
		cl.gridx = 0; cl.gridy = 0; cl.insets = new Insets (1,5,1,1);
		cl.fill = GridBagConstraints.NONE;
		cd.anchor = GridBagConstraints.WEST;
		cd.gridx = 1; cd.gridy = 0; cd.insets = new Insets (1,1,1,5); 
		cd.fill = GridBagConstraints.HORIZONTAL;
		
		// river name
		editPane.add(new JLabel ("Name: "), cl);
		riverNameField = new JTextField ("Test");
		riverNameField.setPreferredSize(new Dimension (250,20));
		editPane.add (riverNameField, cd);
		cl.gridy += 1; cd.gridy += 1;
		
		// start
		editPane.add(new JLabel ("Einstieg: "), cl);
		fromNameField = new JTextField ("Test");
		editPane.add (fromNameField, cd);
		cl.gridy += 1; cd.gridy += 1;

		// destination
		editPane.add(new JLabel ("Ausstieg: "), cl);
		toNameField = new JTextField ("Test");
		editPane.add (toNameField, cd);
		cl.gridy += 1; cd.gridy += 1;

		// trip length
		editPane.add(new JLabel ("L\u00E4nge [km]: "), cl);
		lengthField = new JTextField ("15");
		editPane.add (lengthField, cd);		
		cl.gridy += 1; cd.gridy += 1;

		// default group size
		editPane.add(new JLabel ("Gruppengr\u00F6\u00dfe: "), cl);
		defaultGroupSizeField = new JTextField ("6");
		editPane.add (defaultGroupSizeField, cd);		
		cl.gridy += 1; cd.gridy += 1;
		
		// distance to start
		editPane.add(new JLabel ("Anfahrt [km]: "), cl);
		distanceField = new JTextField ("10");
		editPane.add (distanceField, cd);		
		cl.gridy += 1; cd.gridy += 1;

		// WW level
		editPane.add(new JLabel ("WW Klasse: "), cl);
		wwLevelField = new JTextField ("2");
		editPane.add (wwLevelField, cd);
		cl.gridy += 1; cd.gridy += 1;

		// WW top-level
		editPane.add(new JLabel ("WW St\u00E4rke: "), cl);
		Integer wwLevels[] = {1, 2, 3, 4, 5, 6};
		wwTopLevelCombo = new JComboBox<Integer> (wwLevels);
		wwTopLevelCombo.setSelectedIndex(2);
		JPanel wwP = new JPanel (new FlowLayout(FlowLayout.LEADING));
		wwP.add (wwTopLevelCombo);
		editPane.add (wwP, cd);
		cl.gridy += 1; cd.gridy += 1;

		// max water level
		JPanel maxWaterLevelPanel = new JPanel (new FlowLayout (FlowLayout.LEADING));
		editPane.add(new JLabel ("Pegel max: "), cl);
		maxWaterLevelField = new JTextField ("200");
		maxWaterLevelPanel.add (maxWaterLevelField, cd);
		maxWaterLevelUnitField = new JLabel ("cbm/s");
		maxWaterLevelPanel.add (maxWaterLevelUnitField, cl);
		maxWaterLevelField.setPreferredSize(new Dimension ( 60, (int) maxWaterLevelField.getMinimumSize().getHeight() ));
		editPane.add(maxWaterLevelPanel, cd);
		cl.gridy += 1; cd.gridy += 1;
		
		// min water level
		JPanel minWaterLevelPanel = new JPanel (new FlowLayout (FlowLayout.LEADING));
		editPane.add(new JLabel ("Pegel min: "), cl);
		minWaterLevelField = new JTextField ("20");
		minWaterLevelPanel.add (minWaterLevelField);
		minWaterLevelUnitField = new JLabel ("cm");
		minWaterLevelPanel.add (minWaterLevelUnitField);
		minWaterLevelField.setPreferredSize(new Dimension ( 60, (int) minWaterLevelField.getMinimumSize().getHeight() ));
		editPane.add(minWaterLevelPanel, cd);
		cl.gridy += 1; cd.gridy += 1;

		editPane.add(new JLabel ("Pegel Einheit: "), cl);
		String waterLevelUnits[] = {"cm","cbm/s"};
		unitOfWaterLevelCombo = new JComboBox<String> (waterLevelUnits);
		unitOfWaterLevelCombo.addActionListener(this);
		wwP = new JPanel (new FlowLayout(FlowLayout.LEADING));
		wwP.add(unitOfWaterLevelCombo);
		editPane.add (wwP, cd);
		cl.gridy += 1; cd.gridy += 1;

		setEditFieldsEditable (false);
		
		// Buttons
		newButton = new JButton ("Neu");
		newButton.addActionListener(this);
		editButton = new JButton ("Bearbeiten");
		editButton.addActionListener(this);
		copyButton = new JButton ("Kopieren");
		copyButton.addActionListener(this);
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
		buttonPanelNorth.add(copyButton);
		buttonPanelNorth.add(deleteButton);
		buttonPanelSouth.add(editButton);
		buttonPanelSouth.add(cancelButton);
		buttonPanelSouth.add(okButton, BorderLayout.EAST);
		buttonPanel.add(buttonPanelNorth, BorderLayout.NORTH);
		buttonPanel.add(buttonPanelSouth, BorderLayout.CENTER);
		
		//Create a split pane with the two scroll panes in it.
		JPanel editPaneContainer = new JPanel (new BorderLayout ());
		editPaneContainer.add(editScroller, BorderLayout.NORTH);
		editPaneContainer.add(buttonPanel, BorderLayout.CENTER);
		
		add (listScroller, BorderLayout.CENTER);
		add (editPaneContainer, BorderLayout.EAST);
		
        refreshEditData ();
	}

	@Override
	public void valueChanged(ListSelectionEvent selEvt) {
		// TODO Auto-generated method stub
		if (selEvt.getValueIsAdjusting())
			return;

		if (selEvt.getSource().equals(riverList))
			refreshEditData ();
	}
	
	private void refreshEditData () {
		
		JList<River> jList = (JList<River>) riverList;
		River selRiver = (River) jList.getSelectedValue();
		if (selRiver == null)
			return;
		riverNameField.setText(selRiver.getRiverName());
		fromNameField.setText(selRiver.getTripFrom());
		toNameField.setText(selRiver.getTripTo());
		lengthField.setText (Integer.toString(selRiver.getTripLength()));
		defaultGroupSizeField.setText(Integer.toString(selRiver.getDefaultGroupSize()));
		distanceField.setText (Integer.toString(selRiver.getDistanceToStart ()));
		wwLevelField.setText(selRiver.getWwLevel());
		wwTopLevelCombo.setSelectedIndex(selRiver.getWwTopLevel()-1);
		minWaterLevelField.setText(Integer.toString(selRiver.getMinWaterLevel()));
		maxWaterLevelField.setText(Integer.toString(selRiver.getMaxWaterLevel()));
		unitOfWaterLevelCombo.setSelectedItem(selRiver.getUnitOfWaterLevel());
		minWaterLevelUnitField.setText (selRiver.getUnitOfWaterLevel());
		maxWaterLevelUnitField.setText (selRiver.getUnitOfWaterLevel());
	}

	private void storeEditData () {
		
		JList<River> jList = (JList<River>) riverList;
		River selRiver = (River) jList.getSelectedValue();
		selRiver.setRiverName (riverNameField.getText());
		selRiver.setTripFrom (fromNameField.getText());
		selRiver.setTripTo(toNameField.getText());
		selRiver.setTripLength(Integer.parseInt(lengthField.getText ()));
		selRiver.setDefaultGroupSize(Integer.parseInt(defaultGroupSizeField.getText ()));
		selRiver.setDistanceToStart(Integer.parseInt(distanceField.getText ()));
		selRiver.setWwLevel(wwLevelField.getText());
		selRiver.setWwTopLevel(wwTopLevelCombo.getSelectedIndex()+1);
		selRiver.setMinWaterLevel(Integer.parseInt(minWaterLevelField.getText()));
		selRiver.setMaxWaterLevel(Integer.parseInt(maxWaterLevelField.getText()));
		selRiver.setUnitOfWaterLevel(unitOfWaterLevelCombo.getSelectedItem().toString());
		
		// sort list
		MainWindow.riverDB.sort();
		// refresh JList
		refreshList();
	}
	
	private void copySelectedRiver() {

		for (River r:((JList<River>) riverList).getSelectedValuesList()) {
			riverListModel.insertElementAt(MainWindow.riverDB.cloneRiver (r), riverList.getSelectedIndex()+1);
		}
	}

	private void deleteSelectedRiver() {

		for (River r:((JList<River>) riverList).getSelectedValuesList()) {
			
			riverListModel.removeElement(r);
			MainWindow.riverDB.removeRiver (r);
		}
	}

	
	private void setEditFieldsEditable (Boolean ed) {
		
		riverNameField.setEditable (ed);
		fromNameField.setEditable(ed);
		toNameField.setEditable(ed);
		lengthField.setEditable(ed);
		defaultGroupSizeField.setEditable(ed);
		distanceField.setEditable(ed);
		wwLevelField.setEditable (ed);
		wwTopLevelCombo.setEnabled(ed);
		minWaterLevelField.setEditable(ed);
		maxWaterLevelField.setEditable(ed);
		unitOfWaterLevelCombo.setEnabled(ed);
	}
	
	// 0: copy - delete - edit enabled
	// 1: ok - cancel enabled
	private void setButtonMode (Integer bm) {
		
		if (bm == 0) {
			
			okButton.setEnabled(false);
			cancelButton.setEnabled(false);
			copyButton.setEnabled(true);
			deleteButton.setEnabled (true);
			editButton.setEnabled(true);
			newButton.setEnabled(true);
		}
		
		if (bm == 1) {

			okButton.setEnabled(true);
			cancelButton.setEnabled(true);
			copyButton.setEnabled(false);
			deleteButton.setEnabled (false);
			editButton.setEnabled(false);
			newButton.setEnabled(false);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		
		if (evt.getSource().equals(editButton)) {
			
			setEditFieldsEditable(true);		
			riverList.setEnabled(false);
			setButtonMode(1);
		}
		
		if (evt.getSource().equals(newButton)) {
			riverListModel.insertElementAt(MainWindow.riverDB.newRiver (), riverList.getSelectedIndex()+1);
			riverList.setSelectedIndex(riverList.getSelectedIndex()+1);
			setEditFieldsEditable(true);		
			riverList.setEnabled(false);
			setButtonMode(1);
		}
		
		if (evt.getSource().equals(copyButton)) {
			copySelectedRiver ();
		}

		if (evt.getSource().equals(deleteButton)) {
			deleteSelectedRiver ();
		}
		
		if (evt.getSource().equals(okButton)) {
			
			setEditFieldsEditable(false);
			riverList.setEnabled(true);
			setButtonMode(0);
			// store changed data to object
			storeEditData ();
		}

		if (evt.getSource().equals(cancelButton)) {
			
			setEditFieldsEditable(false);		
			riverList.setEnabled(true);
			setButtonMode(0);
	        refreshEditData ();
		}

		if (evt.getSource().equals(unitOfWaterLevelCombo)) {
			minWaterLevelUnitField.setText (unitOfWaterLevelCombo.getSelectedItem().toString());
			maxWaterLevelUnitField.setText (unitOfWaterLevelCombo.getSelectedItem().toString());
		}
	}
	
	public void refreshList () {
		if ((riverListModel == null) || (MainWindow.riverDB == null))
			return;
		riverListModel.removeAllElements();
		for (River r: MainWindow.riverDB.getAllRivers()) {
    		riverListModel.addElement( r);
	    }
	}

}
