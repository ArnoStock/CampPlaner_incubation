package tripDB;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTextArea;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import riverDB.River;
import rosterDB.Roster;

@Root(name="TripDB")
public class TripDB {

	int checkErrors = 0;
	int checkWarnings = 0;
	
	public TripDB () {
		
		super ();
		trips = new ArrayList<Trip>();
		isDataChanged = false;
		
	}
	
	@ElementList(name="Trips")
    private List <Trip> trips;
	
	private boolean isDataChanged;

	public void write(String fileName) throws Exception {

//		Strategy strategy = new AnnotationStrategy();
//	    Serializer serializer = new Persister(strategy);
	    Serializer serializer = new Persister();
	    File file = new File(fileName);
	    serializer.write(this, file);
	    isDataChanged = false;
		
	}
	
	public static TripDB read (String fileName) throws Exception{
		Serializer serializer = new Persister();
		File file = new File(fileName);
		TripDB tripDB = serializer.read(TripDB.class, file);
		return tripDB;
	}


	public Trip add(Date date) {
		
		Trip t = new Trip( date );
		trips.add(t);
		isDataChanged = true;
		return t;
	}
	
	public void delete (Trip t) {
		
		if (t == null)
			return;
		trips.remove(t);
		isDataChanged = true;
	}

	public boolean isDataChanged() {
		return isDataChanged;
	}

	public List<Trip> getAllTrips() {

		return trips;

	}

	public List<Trip> getAllTrips(Date d) {
		DateFormat df = new SimpleDateFormat("dd.MM.YYYY");
		String refDate = df.format(d);
		
		List<Trip> tl = new ArrayList<Trip>();
		
		for (Trip t: trips) {
			
			String tDate = df.format(t.getTripDate());
			if (refDate.equals(tDate))
				tl.add(t);
		}
		return tl;
	}
	
	public Integer getRosterCount (Date d) {
		
		Integer rc = 0;
		
		for (Trip t: getAllTrips(d)) {
			rc += t.getRosterCount();
		}
		
		return rc;
		
	}
	
	public Integer getRosterAssignmentCount (Date d, Roster r) {
		
		Integer rc = 0;
		
		for (Trip t: getAllTrips(d)) {
			
			for (Roster tr: t.getRosterList()) {
				if (( tr.getFamilyName().equals(r.getFamilyName()) ) &&
					( tr.getGivenName().equals(r.getGivenName()))){
					rc += 1;
				}
			}
		}
		
		return rc;
		
	}

	
	public Integer[] getParticipantsCount (Date d) {

		Integer pc[] = {0, 0, 0, 0, 0, 0};
		for (Trip t: getAllTrips(d)) {
			River r = t.getRiver();
			if (r != null) {
				int ww = t.getRiver().getWwTopLevel()-1;
				pc[ww] = pc[ww]+t.getGroupSize();
			}
		}
		
		return pc;
	}
	
	public boolean checkDay (Date d, JTextArea ta) {
		
		ta.setText("Prüfung:\n");
		
		checkErrors = 0;
		checkWarnings = 0;
		
		for (Trip t: getAllTrips(d)) {
			
			int issues = 0;
			
			ta.append("\nGruppe " + t.getGroupNumber()+ "\n");

			// check, if all trips have a roster assigned
			if (t.getRosterList().size() == 0) {
				ta.append("Fehler: Gruppe " + t.getGroupNumber() + " hat keinen Fahrtenleiter!\n");
				checkErrors++; issues++;
			}
			else if (t.getRosterList().size() > 1) {
				ta.append("Warnung: Gruppe hat " + t.getRosterList().size() + " Fahrtenleiter\n");
				checkWarnings++; issues++;
			}
			
			if (issues == 0) {
				ta.append("Ok\n");
			}
		}
		return (checkErrors == 0) && (checkWarnings == 0);
	}

	public int getCheckErrors() {
		return checkErrors;
	}

	public int getCheckWarnings() {
		return checkWarnings;
	}
	
}
