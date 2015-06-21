package tripDB;

import gui.MainWindow;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

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

	    Serializer serializer = new Persister();
	    File file = new File(fileName);
	    serializer.write(this, file);
	    isDataChanged = false;
		
	}
	
	public void write(ZipOutputStream zipOut) throws Exception {

		// save XML document to ZIP file
		ZipEntry entry = new ZipEntry("Trips.xml");
		zipOut.putNextEntry(entry);
		
		Serializer serializer = new Persister();
	    serializer.write(this, zipOut);

	    zipOut.closeEntry();
	    isDataChanged = false;

	}

	
	public static TripDB read (String fileName) throws Exception{
		Serializer serializer = new Persister();
		File file = new File(fileName);
		TripDB tripDB = serializer.read(TripDB.class, file);
		return tripDB;
	}

	public static TripDB read (ZipFile zipIn) throws Exception {
		
		ZipEntry tripsZipObj = zipIn.getEntry("Trips.xml");
		InputStream zipInStream = zipIn.getInputStream(tripsZipObj);
		
		Serializer serializer = new Persister();
		TripDB tripDB = serializer.read(TripDB.class, zipInStream);

		zipInStream.close();
		
		return tripDB;
	}

	public Trip add(Date date) {
		
		Trip t = new Trip( date );
		trips.add(t);
		isDataChanged = true;
		return t;
	}
	
	public Trip add(Date date, Trip tc) {
		Trip t = new Trip( date, tc);
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
//System.out.println("Trip data changed!");
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
		// sort list by group-number
		Collections.sort(tl);
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
		
		ta.setText("Pr\u00fcfung:\n");
		
		checkErrors = 0;
		checkWarnings = 0;
		Boolean fatalError = false;
		
		// check roster assignment
		for (Roster r: MainWindow.rosterDB.getRosters()) {
	    	if (r.isAvailableAt (d)) {
	    		int a = getRosterAssignmentCount (d, r);
	    		if (a == 0) {
	    			ta.append("Fehler: Fahrtenleiter " + r.getGivenName() + " " +
	    						r.getFamilyName() + " hat noch keine Gruppe.\n");
	    			checkErrors++;
	    		}
	    		else if (a == 2) {
	    			ta.append("Warnung: Fahrtenleiter " + r.getGivenName() + " " +
    						r.getFamilyName() + " hat zwei Gruppen.\n");
	    			checkWarnings++;
	    		}
	    		else if (a > 2) {
	    			ta.append("Fehler: Fahrtenleiter " + r.getGivenName() + " " +
    						r.getFamilyName() + " hat mehr als zwei Gruppen.\n");
	    			checkErrors++;
	    		}
	    	}
		}

	    // check trips
	    if (getAllTrips(d).size() == 0) {
			ta.append("\nFehler: F\u00fcr diesen Tag gibt es keine Gruppen!\n");
			checkErrors++;
			fatalError = true;
		}
		else {
			for (Trip t: getAllTrips(d)) {
				
				int issues = 0;
				
				ta.append("\nGruppe " + t.getGroupNumber()+ "\n");
	
				// check, if all trips have a roster assigned
				if (t.getRosterList().size() == 0) {
					ta.append("Fehler: Gruppe " + t.getGroupNumber() + " hat keinen Fahrtenleiter!\n");
					checkErrors++; issues++;
				}
				else {
					// count rosters and aspirants
					int roster = 0;
					int aspirant = 0;
					for (Roster r: t.getRosterList()) {
						if (r.getIsAspirant())
							aspirant++;
						else roster++;
					}
					
					if (aspirant > 1) {
						ta.append("Warnung: Gruppe hat " + aspirant + " Fahrtenleiteranw\u00e4rter\n");
						checkWarnings++; issues++;
					}
					
					if (roster == 0) {
						ta.append("Fehler: Gruppe " + t.getGroupNumber() + " hat keinen Fahrtenleiter!\n");
						checkErrors++; issues++;
					}

					if (roster > 1) {
						ta.append("Warnung: Gruppe hat " + roster + " Fahrtenleiter\n");
						checkWarnings++; issues++;
					}
				}

				// check, if roster is available
				for (Roster r: t.getRosterList())
					if (!r.isAvailableAt (d)) {
						ta.append("Fehler: Fahrtenleiter " + r.getGivenName() + " " + 
									" hat keinen Dienst!\n");
						checkErrors++; issues++;
					}
				
				if (issues == 0) {
					ta.append("Ok\n");
				}
			}
		}
		ta.append("\nEnde");
			
		return !fatalError;
	}

	public int getCheckErrors() {
		return checkErrors;
	}

	public int getCheckWarnings() {
		return checkWarnings;
	}
	
	public boolean isRosterAssigned (Integer id) {
		for (Trip t: trips) {
			if (t.isRosterAssigned(id))
				return true;
		}
		return false;
	}
	
	public void tripDataChanged () {
		isDataChanged = true;
	}

}
