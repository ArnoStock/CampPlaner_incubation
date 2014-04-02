package rosterDB;

import gui.SetupData;

import java.util.List;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class RosterTableModel extends DefaultTableModel {

	List<Roster> rosters;
	SetupData setupData;
	
	
	public RosterTableModel(List<Roster> rosters, SetupData setupData) {
		super ();
		this.rosters = rosters;
		this.setupData = setupData;
	}
	
	@Override
	public int getRowCount() {

		if (rosters != null)
			return rosters.size();
		return 0;
	}
	
	@Override
	public int getColumnCount() {
		
		return (int) (2 + setupData.getEventLength ());
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
		return setupData.getDateAsString(column-2);
	}
	
	@Override
	public Object getValueAt(int row, int column) {
		
		if (rosters == null) {
			return "";
		}
		
		if (column == 0) {
			return rosters.get(row).getFamilyName();
		}
		if (column == 1) {
			return rosters.get(row).getGivenName();
		}
		
		return rosters.get(row).checkAvailability(setupData.getDateAt(column-2));
	}

	@Override 
	public void setValueAt (Object val, int row, int column) {
		rosters.get(row).setAvailability (setupData.getDateAt(column-2), val);
	}

	public void insertElementAt(Roster newRoster, int i) {
		rosters.add(i, newRoster);		
	}
	

}
