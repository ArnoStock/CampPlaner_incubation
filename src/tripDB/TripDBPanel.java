package tripDB;

import gui.MainWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import riverDB.River;
import rosterDB.Roster;

import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

@SuppressWarnings("serial")
public class TripDBPanel extends JPanel implements ChangeListener, ActionListener, PropertyChangeListener, ListSelectionListener, DocumentListener {

	private JLabel tfWW[] = {null, null, null, null, null};
	private JLabel tfTotal;
	private JLabel tfRoster;
	private int availableRosters;
	
	private JList<TripComponent> tripList;
	private DefaultListModel<TripComponent> tripListModel;
	private DefaultListModel<RosterComponent> rosterListModel;
	private Calendar targetDay;
    private JDatePicker targetDayPicker;
	private JList<RiverComponent> riverList;
	private JList<RosterComponent> rosterList;
	
	private JButton btnAdd;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JButton btnUp;
	private JButton btnDown;
	private JTabbedPane tabpanePlanningSteps;
	private JPanel leftPanel;
	private JPanel headerPanel;
	private DefaultListModel<RiverComponent> riverListModel;
	
    private JLabel detailsGroupNumber;
    private JLabel detailsRiverInfo;
    private JLabel detailsTripFrom;
    private JLabel detailsTripTo;
    private JLabel detailsWwLevel;
    private JLabel detailsDistanceToStart;
    private JCheckBox detailsIsEducation;
	private JComboBox<Integer> detailsGroupSize;
	private JComboBox<Integer> detailsDriverCount;
	private JList<RosterComponent> detailsRosterList;
	private DefaultListModel<RosterComponent> detailsRosterListModel;
	private JButton detailsRosterRemoveBtn;
	private JTextArea detailsCommentText;
	private JSpinner detailsStartTimeSpinner;
	private JButton nextDayButton;
	private JButton prevDayButton;
	
	private JPanel panelStartTime;
	private PrintOutPanel panelPreview;
	    
	public TripDBPanel() {
		super (new BorderLayout ());
		
		targetDay = Calendar.getInstance();
		targetDay.setTime(new Date ());
		targetDay.add(Calendar.DATE, 1);
		Calendar startDay = Calendar.getInstance();
		startDay.setTime(MainWindow.setupData.getEventStartDate());
		if (targetDay.before(startDay))
			targetDay = startDay;

		// create table showing roster availability
		setBackground(Color.BLUE);

	    headerPanel = new JPanel ( new BorderLayout ());

//	    prevDayButton = new JButton ("<");
//	    nextDayButton = new JButton (">");
	    prevDayButton = new JButton (MainWindow.getImageIcon("toolbarButtonGraphics/navigation/Back16.gif"));
	    nextDayButton = new JButton (MainWindow.getImageIcon("toolbarButtonGraphics/navigation/Forward16.gif"));
	    prevDayButton.addActionListener(this);
	    nextDayButton.addActionListener(this);
	    
	    
	    JPanel datePickerPanel = new JPanel();
		new JDateComponentFactory();
		targetDayPicker = JDateComponentFactory.createJDatePicker();
		targetDayPicker.setTextEditable(true);
		targetDayPicker.setShowYearButtons(true);
		targetDayPicker.getModel().addChangeListener(this);
		targetDayPicker.getModel().setDate(	targetDay.get(Calendar.YEAR), 
											targetDay.get(Calendar.MONTH), 
											targetDay.get(Calendar.DATE));
		targetDayPicker.getModel().setSelected(true);

		datePickerPanel.add(prevDayButton);
		datePickerPanel.add ((Component)targetDayPicker);
		datePickerPanel.add(nextDayButton);
		datePickerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		headerPanel.add(datePickerPanel, BorderLayout.WEST); 
		
		JPanel wwLevelPanel = new JPanel ();
		tfWW[0] = addStatisticsField(wwLevelPanel, "WW-I:");
		tfWW[1] = addStatisticsField(wwLevelPanel, "WW-II:");
		tfWW[2] = addStatisticsField(wwLevelPanel, "WW-III:");
		tfWW[3] = addStatisticsField(wwLevelPanel, "WW-IV:");
		tfWW[4] = addStatisticsField(wwLevelPanel, "WW-V:");
		tfTotal = addStatisticsField(wwLevelPanel, "Total:");
	    headerPanel.add (wwLevelPanel, BorderLayout.CENTER);

	    JPanel rosterPanel = new JPanel ();
	    tfRoster = addStatisticsField(rosterPanel, "FL:");
	    headerPanel.add (rosterPanel, BorderLayout.EAST);
	    
	    headerPanel.setBackground(new Color (220, 220, 220));

	    tripListModel = new DefaultListModel<TripComponent>();  
	    // create trip list in left panel
	    tripList = new JList<TripComponent> (tripListModel);
	    tripList.setCellRenderer(new TripListRenderer());
	    tripList.addListSelectionListener(this);
	    tripList.setTransferHandler(new TripListTransferHandler(this));
       // Create JTabbedPane object
        tabpanePlanningSteps = new JTabbedPane
           (JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT );
        // create panel to select river information
        JPanel panelRivers = new JPanel ( new BorderLayout ());
        panelRivers.setBackground(Color.BLUE);
        tabpanePlanningSteps.addTab("Fl\u00FCsse", panelRivers);
	    riverListModel = new DefaultListModel<RiverComponent>();  
	    // create trip list in left panel
	    riverList = new JList<RiverComponent> (riverListModel);
	    riverList.setCellRenderer(new RiverListRenderer());
	    panelRivers.add (new JScrollPane(riverList), BorderLayout.CENTER);
	    riverList.setDragEnabled(true);
	    riverList.setTransferHandler(new RiverListTransferHandler(this));
        
        // create panel to select roster information
        JPanel panelRosters = new JPanel ( new BorderLayout ());
        panelRosters.setBackground(Color.YELLOW);
        tabpanePlanningSteps.addTab("Fahrtenleiter", panelRosters);
	    rosterListModel = new DefaultListModel<RosterComponent>();  
	    // create trip list in left panel
	    rosterList = new JList<RosterComponent> (rosterListModel);
	    rosterList.setCellRenderer(new RosterListRenderer(targetDay));
	    updateRosterList ();
	    panelRosters.add (new JScrollPane(rosterList), BorderLayout.CENTER);
	    rosterList.setDragEnabled(true);
	    rosterList.setTransferHandler(new RosterListTransferHandler(this));
	    
	    // create panel with trip details
		JPanel panelDetails = new JPanel ( new BorderLayout ());
        panelDetails.setBackground(new Color (220, 220, 220));
        tabpanePlanningSteps.addTab("Details", panelDetails);
	    JPanel detailsGridPanel = new JPanel (new GridLayout (10,1));
	    detailsGroupNumber = new JLabel ("Gruppe ");
	    detailsGridPanel.add (detailsGroupNumber);
	    detailsRiverInfo = new JLabel ("Name km \u02AC");
	    detailsGridPanel.add (detailsRiverInfo);
	    detailsTripFrom = new JLabel ("Von: ");
	    detailsGridPanel.add (detailsTripFrom);
	    detailsTripTo = new JLabel ("Nach: ");
	    detailsGridPanel.add (detailsTripTo);
	    detailsWwLevel = new JLabel ("WW Stufe: ");
	    detailsGridPanel.add (detailsWwLevel);
	    detailsDistanceToStart = new JLabel ("Anfahrt: 0km");
	    detailsGridPanel.add (detailsDistanceToStart);
	    detailsIsEducation = new JCheckBox ("Schulung");
	    detailsIsEducation.addActionListener(this);
	    detailsGridPanel.add (detailsIsEducation);
	    
	    JPanel grPanel = new JPanel (new FlowLayout (FlowLayout.LEADING));
	    grPanel.add (new JLabel ("Gruppengr\u00f6\u00dfe:"));
	    Integer groupSize[] = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
	    detailsGroupSize = new JComboBox<Integer>(groupSize);
	    grPanel.add (detailsGroupSize);
	    detailsGroupSize.addActionListener(this);
	    detailsGridPanel.add (grPanel);
	    
	    JPanel drPanel = new JPanel (new FlowLayout (FlowLayout.LEADING));
	    drPanel.add(new JLabel ("Fahrer:"));
	    Integer drivers[] = {1, 2, 3};
	    detailsDriverCount = new JComboBox<Integer>(drivers);
	    detailsDriverCount.addActionListener(this);
	    drPanel.add (detailsDriverCount);
	    detailsGridPanel.add (drPanel);
	    
        //Add the start time spinner.
	    JPanel timePanel = new JPanel (new FlowLayout(FlowLayout.LEADING));
	    timePanel.add(new JLabel ("Startzeit: "));
	    SimpleDateFormat sdfWithDefaultYear = new SimpleDateFormat("HH:mm");
	    Date initDate;
		try {
			initDate = sdfWithDefaultYear.parse("10:00");
		} catch (ParseException e) {
			initDate = new Date();
		}
	    SpinnerModel dateModel = new SpinnerDateModel(initDate,
                                     null,
                                     null,
                                     Calendar.MINUTE);//ignored for user input
        detailsStartTimeSpinner = new JSpinner (dateModel);
        detailsStartTimeSpinner.setEditor(new JSpinner.DateEditor(detailsStartTimeSpinner, "HH:mm"));
        detailsStartTimeSpinner.addChangeListener(this);
        timePanel.add(detailsStartTimeSpinner);
	    detailsGridPanel.add (timePanel);
	    
	    panelDetails.add (detailsGridPanel, BorderLayout.NORTH);
	    
	    JPanel detailsBottomPanel = new JPanel (new BorderLayout());
	    
	    JPanel detailsRosterPanel = new JPanel (new BorderLayout());
	    detailsRosterPanel.add(new JLabel ("Fahrtenleiter"), BorderLayout.NORTH);
	    detailsRosterListModel = new DefaultListModel<RosterComponent> ();
	    detailsRosterList = new JList<RosterComponent> (detailsRosterListModel);
	    detailsRosterList.setPreferredSize(new Dimension (200, 20));
	    detailsRosterList.setVisibleRowCount(1);
	    detailsRosterList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
	    detailsRosterPanel.add (detailsRosterList, BorderLayout.CENTER);
	    detailsRosterRemoveBtn = new JButton ("Entfernen");
	    detailsRosterRemoveBtn.addActionListener(this);
	    JPanel rbPanel = new JPanel (new FlowLayout (FlowLayout.LEADING));
	    rbPanel.add (detailsRosterRemoveBtn);
	    detailsRosterRemoveBtn.setEnabled(false);
	    detailsRosterPanel.add (rbPanel, BorderLayout.SOUTH);
 	    
	    detailsBottomPanel.add (detailsRosterPanel, BorderLayout.NORTH);
	    
	    JPanel detailsCommentPanel = new JPanel (new BorderLayout());
	    detailsCommentPanel.add(new JLabel ("Bemerkung"), BorderLayout.NORTH);
	    detailsCommentText = new JTextArea ();
	    detailsCommentText.getDocument().addDocumentListener(this);
	    detailsCommentPanel.add(detailsCommentText, BorderLayout.CENTER);
	    
	    detailsBottomPanel.add (detailsCommentPanel, BorderLayout.CENTER);
	    
	    panelDetails.add (detailsBottomPanel, BorderLayout.CENTER);

	    // create panel with trip start times
        panelStartTime = new JPanel ( new FlowLayout (FlowLayout.LEADING));
        panelStartTime.setBackground(new Color (220, 220, 220));
        JScrollPane startTimesScrollPane = new JScrollPane (panelStartTime);
        tabpanePlanningSteps.addTab("Zeiten", startTimesScrollPane);

	    // create panel with print previews
        panelPreview = new PrintOutPanel (targetDay);
        panelPreview.setBackground(new Color (220, 220, 220));
        tabpanePlanningSteps.addTab("Drucken", panelPreview);
	    
	    /*
	     * Add center panel with buttons
	     */
	    JPanel btnPanel = new JPanel ( );
	    btnPanel.setLayout( new GridLayout (7,1) );
	    btnAdd = new JButton ("+ Hinzu");
	    btnPanel.add(btnAdd);
	    Dimension minSize = new Dimension(5, 10);
	    Dimension prefSize = new Dimension(5, 10);
	    Dimension maxSize = new Dimension(5, 20);
	    btnUpdate = new JButton ("\u23CE Ersetzen");
	    btnPanel.add(btnUpdate);
	    btnPanel.add(new Box.Filler(minSize, prefSize, maxSize));
	    btnUp = new JButton ("\u2206 Auf");
	    btnUp.setEnabled(false);
	    btnPanel.add (btnUp);
	    btnDown = new JButton ("\u2207  Ab");
	    btnDown.setEnabled(false);
	    btnPanel.add (btnDown);
	    btnPanel.add(new Box.Filler(minSize, prefSize, maxSize));
	    btnDelete = new JButton ("\u2297 L\u00F6schen");
	    btnDelete.setEnabled(false);
	    btnPanel.add(btnDelete);
	    JPanel btnAreaPanel = new JPanel (new GridBagLayout ());
	    
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
	    btnAreaPanel.add(btnPanel, c);
	    
	    btnAdd.addActionListener(this);
	    btnAdd.setActionCommand("Add");
	    btnUpdate.addActionListener(this);
	    btnUpdate.setActionCommand("Update");
	    btnDelete.addActionListener(this);
	    btnDelete.setActionCommand("Delete");
	    btnUp.addActionListener(this);
	    btnUp.setActionCommand("Up");
	    btnDown.addActionListener(this);
	    btnDown.setActionCommand("Down");
	    
		// left panel for trip list and buttons
		leftPanel = new JPanel (new BorderLayout ());
		leftPanel.add(new JScrollPane(tripList), BorderLayout.CENTER);
		leftPanel.add(btnAreaPanel, BorderLayout.EAST);
		
		// split pane in the center of the layout
	    JSplitPane sp = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT, leftPanel, tabpanePlanningSteps);
	    add (headerPanel, BorderLayout.NORTH);

	    c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
	    add (sp, BorderLayout.CENTER);

	    //Provide minimum sizes for the two components in the split pane
	    leftPanel.setMinimumSize(new Dimension(350, 200));
	    tabpanePlanningSteps.setMinimumSize(new Dimension(200, 200));
	    tabpanePlanningSteps.addChangeListener(this);

	}

	private JLabel addStatisticsField (JPanel pp, String n){

		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		JPanel p = new JPanel ();
		p.setBorder(loweredbevel);
		p.add (new JLabel (n));
	    JLabel l = new JLabel ("0");
	    p.add (l);
	    pp.add (p);
	    return l;

	}
	
	private void updateRosterList() {
		availableRosters = 0;
		if ((rosterListModel == null) || (MainWindow.rosterDB == null))
			return;
		rosterListModel.removeAllElements();
		for (Roster r: MainWindow.rosterDB.getRosters()) {
	    	if (r.isAvailableAt (targetDay.getTime())) {
	    		rosterListModel.addElement( new RosterComponent(r));
	    		availableRosters++;
	    	}
	    }
		tfRoster.setText(MainWindow.tripDB.getRosterCount(targetDay.getTime()) + "/" + availableRosters);
	}


	@Override
	public void stateChanged(ChangeEvent evt) {

		if (evt.getSource().equals(targetDayPicker.getModel())) {

			targetDay.set(	targetDayPicker.getModel().getYear(), 
							targetDayPicker.getModel().getMonth(), 
							targetDayPicker.getModel().getDay());
			updateRosterList();
			updateTripList();
		}
		
		if (evt.getSource().equals(detailsStartTimeSpinner)) {
			TripComponent t = tripList.getSelectedValue();
			if (t == null)
				return;
			Date d = (Date)detailsStartTimeSpinner.getValue();
			
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			int m = c.get(Calendar.MINUTE);
			if (m % 15 != 0) {
				if (m%15 > 8) {
					m -= m%15;
				}
				else {
					m += 15 - m%15;
				}
				c.set(Calendar.MINUTE, m);
			}
			
			t.getTrip().setTripStartTime(c.getTime());
			detailsStartTimeSpinner.setValue (c.getTime());
			return;			
		}
		
		if (evt.getSource().equals(tabpanePlanningSteps)) {
			if (tabpanePlanningSteps.getSelectedIndex() == 3)
				updateTimePanel();
			if (tabpanePlanningSteps.getSelectedIndex() == 4)
				panelPreview.checkTrips();
		}
	
	}

	private void updateGroupIndex () {
		
		for (int i = 0; i < tripListModel.getSize(); i++) {
			tripListModel.get(i).getTrip().setGroupNumber(i+1);
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource().equals(detailsIsEducation)) {
			TripComponent t = tripList.getSelectedValue();
			if (t == null)
				return;
			t.getTrip().setIsEducation(detailsIsEducation.isSelected());
			return;
		}
		
		if (evt.getSource().equals(detailsGroupSize)) {
			TripComponent t = tripList.getSelectedValue();
			if (t == null)
				return;
			t.getTrip().setTotalGroupSize(detailsGroupSize.getSelectedIndex()+2);
			updateParticipantsPanel ();
			return;
		}

		if (evt.getSource().equals(detailsDriverCount)) {
			TripComponent t = tripList.getSelectedValue();
			if (t == null)
				return;
			t.getTrip().setDriverCount(detailsDriverCount.getSelectedIndex()+1);
			return;
		}
		
		if (evt.getSource().equals(detailsRosterRemoveBtn)) {
			TripComponent t = tripList.getSelectedValue();
			if (t == null)
				return;
			RosterComponent rc = detailsRosterList.getSelectedValue();
			if (rc == null)
				return;
			t.getTrip().removeRoster(rc.getRoster());
			detailsRosterListModel.removeElement(rc);
			detailsRosterRemoveBtn.setEnabled(!detailsRosterListModel.isEmpty());
			tripList.invalidate();
			tripList.repaint();
			tfRoster.setText(MainWindow.tripDB.getRosterCount(targetDay.getTime()) + "/" + availableRosters);
			return;
		}
		
		if (evt.getSource().equals(prevDayButton)) {
			targetDayPicker.getModel().addDay(-1);
		}

		if (evt.getSource().equals(nextDayButton)) {
			targetDayPicker.getModel().addDay(1);
		}
		
		if (evt.getActionCommand().contentEquals("Add")) {
			
			Trip t = MainWindow.tripDB.add ( targetDay.getTime() );
			if (tabpanePlanningSteps.getSelectedIndex() == 0) {
				if (!riverList.isSelectionEmpty())
					t.setRiver(riverList.getSelectedValue().getRiver());
			}
			else {
				if (!rosterList.isSelectionEmpty())
					t.addRoster(rosterList.getSelectedValue().getRoster());
			}
			tripListModel.addElement (new TripComponent (t));
			tfRoster.setText(MainWindow.tripDB.getRosterCount(targetDay.getTime()) + "/" + availableRosters);
			updateParticipantsPanel ();
			updateGroupIndex();
			updateTimePanel ();
		}
		
		if (evt.getActionCommand().contentEquals("Update")) {
			
			List<TripComponent> selectedTrips = tripList.getSelectedValuesList();
			if (!selectedTrips.isEmpty()){
				
				if (tabpanePlanningSteps.getSelectedIndex() == 0) {
					if (!riverList.isSelectionEmpty()) {

						for (TripComponent tc: selectedTrips)
							tc.getTrip().setRiver(riverList.getSelectedValue().getRiver());
						tripList.invalidate();
						tripList.repaint();
					}
				}
				else {
					if (!rosterList.isSelectionEmpty()) {
						
						for (TripComponent tc: selectedTrips)
							tc.getTrip().addRoster(rosterList.getSelectedValue().getRoster());
						tripList.invalidate();
						tripList.repaint();
						rosterList.repaint();
					}
				}
				
				
			}
			tfRoster.setText(MainWindow.tripDB.getRosterCount(targetDay.getTime()) + "/" + availableRosters);
			updateParticipantsPanel ();
			updateDetailsPane();
			updateTimePanel ();
			
		}

		if (evt.getActionCommand().contentEquals("Up")) {
			if (!tripList.isSelectionEmpty()) {
				int i = tripList.getSelectedIndex();
				if (i >= 1) {
					TripComponent tc = tripListModel.remove(i);
					tripListModel.add(i-1, tc);
					tripList.setSelectedIndex(i-1);
					tripList.invalidate();
					tripList.repaint();
					updateGroupIndex();
					updateDetailsPane();
					updateTimePanel ();
				}
			}
		}
		
		if (evt.getActionCommand().contentEquals("Down")) {
			if (!tripList.isSelectionEmpty()) {
				int i = tripList.getSelectedIndex();
				if (i+1 < tripListModel.getSize()) {
					TripComponent tc = tripListModel.remove(i);
					tripListModel.add(i+1, tc);
					tripList.setSelectedIndex(i+1);
					tripList.invalidate();
					tripList.repaint();
					updateGroupIndex();
					updateDetailsPane();
					updateTimePanel ();
				}
			}
		}
		
		if (evt.getActionCommand().contentEquals("Delete")) {
			
			List<TripComponent> selectedTrips = tripList.getSelectedValuesList();
			if (!selectedTrips.isEmpty()){
				
		   		if ( JOptionPane.showConfirmDialog(null,
		    			"Sollen die ausgew\u00e4hlten Gruppen gel\u00f6scht werden?",
						"Gruppen l\u00f6schen?", 
						JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION ) {
							return;
				}
				
				for (TripComponent tc: selectedTrips) {
					
					MainWindow.tripDB.delete(tc.getTrip());
					tripListModel.removeElement(tc);
					
				}
				tfRoster.setText(MainWindow.tripDB.getRosterCount(targetDay.getTime()) + "/" + availableRosters);
				updateParticipantsPanel ();
				tripList.invalidate();
				tripList.repaint();
				updateGroupIndex();
				updateTimePanel ();
			}
		}
		
	}

	public void updateTripList () {

		if (tripListModel == null)
			return;
		
		tripListModel.removeAllElements();
		for (Trip t : MainWindow.tripDB.getAllTrips (targetDay.getTime()))
			tripListModel.addElement (new TripComponent (t));
		tfRoster.setText(MainWindow.tripDB.getRosterCount(targetDay.getTime()) + "/" + availableRosters);
		updateParticipantsPanel ();
		panelPreview.updateTripList ();
		
	}

	public void updateRiverList () {

		if (riverListModel == null)
			return;
		
		riverListModel.removeAllElements();
		for (River r : MainWindow.riverDB.getAllRivers ())
			riverListModel.addElement (new RiverComponent (r));
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		refreshList();
	}

	public void refreshList () {

		updateRiverList();
		updateRosterList();
		updateDetailsPane();
		updateTripList();
		tfRoster.setText(MainWindow.tripDB.getRosterCount(targetDay.getTime()) + "/" + availableRosters);
		
	}

	private void updateDetailsPane () {

		TripComponent t = tripList.getSelectedValue();
		if (t == null)
			return;
		Trip tr = t.getTrip();
		detailsGroupNumber.setText("Gruppe " + (tripList.getMinSelectionIndex()+1));
		detailsRiverInfo.setText(t.getRiverName() + "  " + tr.getTripLength() +"km  \u02AC" + t.getWwTopLevel());
	    detailsTripFrom.setText("Von: " + tr.getTripFrom());
	    detailsTripTo.setText("Bis: " + tr.getTripTo());
	    detailsWwLevel.setText("WW Stufe: " +tr.getWwLevel() );
	    detailsDistanceToStart.setText("Anfahrt: " + tr.getDistanceToStart() +"km");
	    detailsIsEducation.setSelected(tr.getIsEducation());
	    detailsGroupSize.setSelectedIndex(tr.getTotalGroupSize()-2);
	    detailsDriverCount.setSelectedIndex(tr.getDriverCount()-1);
	    detailsStartTimeSpinner.setValue(tr.getTripStartTime());
	    detailsRosterListModel.clear();
	    for (Roster r: tr.getRosterList()) {
	    	detailsRosterListModel.addElement(new RosterComponent(r));
	    }
	    detailsRosterRemoveBtn.setEnabled(!detailsRosterListModel.isEmpty());
	    detailsCommentText.setText(tr.getComment());
	}
	
	@Override
	public void valueChanged(ListSelectionEvent l) {

		// serve the trip list selection changes to fill the "Details" pane contents
//		if (l.getValueIsAdjusting() == false) {
			updateDetailsPane();
			btnUp.setEnabled(!tripList.isSelectionEmpty());
			btnDown.setEnabled(!tripList.isSelectionEmpty());
			btnDelete.setEnabled(!tripList.isSelectionEmpty());
//		}
		
	}

	private void feedbackTripComment () {
		TripComponent t = tripList.getSelectedValue();
		if (t == null)
			return;
		t.getTrip().setComment(detailsCommentText.getText());
		return;
		
	}
	
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		feedbackTripComment ();	
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		feedbackTripComment ();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		feedbackTripComment ();
	}

	public void dropRiver(River river, javax.swing.TransferHandler.DropLocation dropLocation) {
		TripComponent tc = tripList.getSelectedValue();
		if (tc == null) {
			tc = new TripComponent (MainWindow.tripDB.add ( targetDay.getTime() ));
			tripListModel.addElement (tc);
		}
		tc.getTrip().setRiver(river);
		updateGroupIndex();
		updateParticipantsPanel ();
		tripList.invalidate();
		tripList.repaint();
	}
	
	public void dropRoster(Integer rosterID, javax.swing.TransferHandler.DropLocation dropLocation) {
		TripComponent tc = tripList.getSelectedValue();
		if (tc == null) {
			tc = new TripComponent (MainWindow.tripDB.add ( targetDay.getTime() ));
			tripListModel.addElement (tc);
		}
		tc.getTrip().addRoster(MainWindow.rosterDB.getRosterByID(rosterID));
		updateGroupIndex();
		tfRoster.setText(MainWindow.tripDB.getRosterCount(targetDay.getTime()) + "/" + availableRosters);
		tripList.invalidate();
		tripList.repaint();
		rosterList.repaint();
	}

	public void updateTimePanel () {
		
		panelStartTime.removeAll();
		JPanel tripComponentPanel = new JPanel (new GridBagLayout ());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		for (Object o: tripListModel.toArray()) {
			TripComponent tc = (TripComponent) o;
			tripComponentPanel.add (new TripTimeLabelComponent (tc.getTrip()), c);
			c.gridx = 1;
			tripComponentPanel.add (new TripTimeIndicatorComponent (tc.getTrip()),c);
			c.gridy += 1;
			c.gridx = 0;
		}
		panelStartTime.add(tripComponentPanel);
	}
	
	public void updateParticipantsPanel () {
		
		Integer c[] = MainWindow.tripDB.getParticipantsCount (targetDay.getTime());
		Integer t = 0;
		for (int i = 0; i < 5; i++) {
			t += c[i];
			tfWW[i].setText(((Integer)c[i]).toString());
		}
		tfTotal.setText(t.toString());
		
	}
	
}
