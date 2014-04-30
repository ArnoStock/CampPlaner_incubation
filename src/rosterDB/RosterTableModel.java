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
		
		return (int) (3 + MainWindow.setupData.getEventLength ());
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
		
		return MainWindow.rosterDB.getRosters().get(row).checkAvailability(MainWindow.setupData.getDateAt(column-2));
	}

	@Override 
	public void setValueAt (Object val, int row, int column) {
		MainWindow.rosterDB.getRosters().get(row).setAvailability (MainWindow.setupData.getDateAt(column-2), val);
		fireTableDataChanged();
	}

	public void insertElementAt(Roster newRoster, int i) {
		MainWindow.rosterDB.getRosters().add(i, newRoster);		
		fireTableDataChanged();
	}
	

}
