package rosterDB;

import gui.MainWindow;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class RosterTableModel extends DefaultTableModel {

	public RosterTableModel() {
		super ();
	}
	
	@Override
	public int getRowCount() {

		if (MainWindow.rosterDB == null)
			return 0;
		
		if (MainWindow.rosterDB.getRosters() != null)
			return MainWindow.rosterDB.getRosters().size();
		return 0;
	}
	
	@Override
	public int getColumnCount() {
		
		return (int) (3 + MainWindow.setupData.getEventLength () +4);
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	@Override
	public String getColumnName (int column) {
		if (column == 0)
			return "Name";
		if (column == 1)
			return "Vorname";
		if (column == 3 + MainWindow.setupData.getEventLength () -1 +1)
			return "Teilnehmer";
		if (column == 3 + MainWindow.setupData.getEventLength () -1 +2)
			return "Aktiv";
		if (column == 3 + MainWindow.setupData.getEventLength () -1 +3)
			return "B\u00FCro";
		if (column == 3 + MainWindow.setupData.getEventLength () -1 +4)
			return "Urlaub";
		return MainWindow.setupData.getDateAsString(column-2);
	}
	
	@Override
	public Object getValueAt(int row, int column) {
		
		if (MainWindow.rosterDB.getRosters() == null) {
			return "";
		}
		
		if (column == 0) {
			return MainWindow.rosterDB.getRosters().get(row).getFamilyName();
		}
		if (column == 1) {
			return MainWindow.rosterDB.getRosters().get(row).getGivenName();
		}
		// Teilnehmer
		if (column == 3 + MainWindow.setupData.getEventLength () -1 +1)
			return MainWindow.rosterDB.getRosters().get(row).getAvailabilityCount(RosterAvailability.ROSTER_PARTICIPATES);
		// Aktiv
		if (column == 3 + MainWindow.setupData.getEventLength () -1 +2) {
			int av = MainWindow.rosterDB.getRosters().get(row).getAvailabilityCount(RosterAvailability.ROSTER_AVAILABLE) +
						MainWindow.rosterDB.getRosters().get(row).getAvailabilityCount(RosterAvailability.ROSTER_OFFICE);
			return av;
		}
		// Büro
		if (column == 3 + MainWindow.setupData.getEventLength () -1 +3)
			return MainWindow.rosterDB.getRosters().get(row).getAvailabilityCount(RosterAvailability.ROSTER_OFFICE);
		// Urlaub
		if (column == 3 + MainWindow.setupData.getEventLength () -1 +4)
			return MainWindow.rosterDB.getRosters().get(row).getAvailabilityCount(RosterAvailability.ROSTER_VACATION);
		
		return MainWindow.rosterDB.getRosters().get(row).checkAvailability(MainWindow.setupData.getDateAt(column-2));
	}

	@Override 
	public void setValueAt (Object val, int row, int column) {
		if ((column < 2) || (column > 2+ MainWindow.setupData.getEventLength ()))
			return;
		MainWindow.rosterDB.getRosters().get(row).setAvailability (MainWindow.setupData.getDateAt(column-2), val);
		fireTableDataChanged();
	}

	public void insertElementAt(Roster newRoster, int i) {
		MainWindow.rosterDB.getRosters().add(i, newRoster);		
		fireTableDataChanged();
	}
	

}
