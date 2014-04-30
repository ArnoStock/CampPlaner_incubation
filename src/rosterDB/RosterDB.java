package rosterDB;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.Ostermiller.util.CSVParser;
import com.Ostermiller.util.LabeledCSVParser;

@Root(name="RosterDB")
public class RosterDB implements PropertyChangeListener {

	public static final int READ_MODE_ADD = 0;
	public static final int READ_MODE_ALL = 1;
	
	@ElementList(name="Rosters")
    private List <Roster> rosters;

	private boolean isDataChanged;
	private final PropertyChangeSupport rosterChangeSupport = new PropertyChangeSupport(this);

	public RosterDB() {
		super ();
    	rosters = new ArrayList<Roster>();
    	isDataChanged = false;
	}
	
	public List<Roster> getRosters () {
		
		return rosters;
	}
	
	public Roster getRosterByID (int id) {
		
		for (Roster r: rosters)
			if (r.getRosterID() == id)
				return r;
		return null;
		
	}

	public void write(String fileName) throws Exception {
		
	    Serializer serializer = new Persister();
	    File file = new File(fileName);
	    serializer.write(this, file);
	    isDataChanged = false;
		
	}
	
	public void write(ZipOutputStream zipOut) throws Exception {

		// save XML document to ZIP file
		ZipEntry entry = new ZipEntry("Rosters.xml");
		zipOut.putNextEntry(entry);
		
		Serializer serializer = new Persister();
	    serializer.write(this, zipOut);

		zipOut.closeEntry();
	    isDataChanged = false;
		
	}

	
	public static RosterDB read (String fileName) throws Exception{
		Serializer serializer = new Persister();
		File file = new File(fileName);
		RosterDB rosterDB = serializer.read(RosterDB.class, file);
		return rosterDB;
	}

	public static RosterDB read (ZipFile zipIn) throws Exception {
		
		ZipEntry rosterZipObj = zipIn.getEntry("Rosters.xml");
		InputStream zipInStream = zipIn.getInputStream(rosterZipObj);
		
		Serializer serializer = new Persister();
		RosterDB rosterDB = serializer.read(RosterDB.class, zipInStream);

		zipInStream.close();
		
		return rosterDB;
	}

	public void add(String familyName, String givenName, String phone, Boolean isAspirant) {
		
		rosters.add(new Roster( getNextRosterID(), familyName, givenName, phone, isAspirant));
		isDataChanged = true;
		
	}

	private int getNextRosterID() {

		int m = 0;
		for (Roster r: rosters) {
			if (r.getRosterID() > m) {
				m = r.getRosterID();
			}
		}
		return m+1;
	}

	public boolean isDataChanged() {
		return isDataChanged;
	}
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    	
        this.rosterChangeSupport.addPropertyChangeListener(listener);
		for (Roster r: rosters) {
			r.addPropertyChangeListener(this);
		}
        
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.rosterChangeSupport.removePropertyChangeListener(listener);
		for (Roster r: rosters) {
			r.removePropertyChangeListener(this);
		}
    }

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		isDataChanged = true;
		rosterChangeSupport.firePropertyChange(evt);
		
	}

	public Roster newRoster() {

		Roster nr = new Roster (getNextRosterID());
		nr.addPropertyChangeListener(this);
		rosters.add (nr);
		rosterChangeSupport.firePropertyChange("addedRoster", true, false);
		isDataChanged = true;
		return nr;
	}
	
	public void deleteRoster (Roster r) {
		rosters.remove(r);
		isDataChanged = true;
		rosterChangeSupport.firePropertyChange("removedRoster", true, false);
	}

	public static RosterDB readCSV (String fName) {
		
		RosterDB rosterDB = new RosterDB ();
	
		
		LabeledCSVParser lcsvp = null;
		try {
			lcsvp = new LabeledCSVParser(
				    new CSVParser(
				    		new FileReader( fName )		            
				    )
				);
			
			try {
				while(lcsvp.getLine() != null){

					System.out.println(
					        "Parse Nachname: " + lcsvp.getValueByLabel("Name") +
					        " Vorname: " + lcsvp.getValueByLabel("Vorname") +
					        " Handy: " + lcsvp.getValueByLabel("Handy")
					    );

					
					rosterDB.add (	lcsvp.getValueByLabel("Name"), lcsvp.getValueByLabel("Vorname"), 
									lcsvp.getValueByLabel("Handy"), Boolean.getBoolean(lcsvp.getValueByLabel("Aspirant")));

				}
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			 
		return rosterDB;
	}

	public static boolean writeCSV(RosterDB rosterDB, String absolutePath) {
		// create output file
        FileWriter outputStream = null;

	    try {
	    	outputStream = new FileWriter(absolutePath);
	    	outputStream.write("Name,Vorname,Handy,Aspirant\n");
	    	for (Roster r: rosterDB.getRosters()) {
		    	outputStream.write(	r.getFamilyName() + "," + r.getGivenName() + "," + 
		    						r.getPhoneNumber() + "," + r.getIsAspirant() + "\n");
	    	}
	    	
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	    	if (outputStream != null) {
	    		try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    }
		
		return true;
	}
	
	public void setIsDataChanged(boolean b) {
		isDataChanged = b;
		rosterChangeSupport.firePropertyChange("loadedRosters", true, false);
	}

	private boolean rosterExists (Roster r) {
		for (Roster mr: getRosters()) {
			if ((r.getFamilyName().equals(mr.getFamilyName())) &&
				(r.getGivenName().equals(mr.getGivenName())))
				return true;
		}
		return false;
	}
	
	public void addCSV(String absolutePath, int readMode) {
		
		String s = "";
		
		RosterDB newDB = readCSV (absolutePath);
		for (Roster r: newDB.getRosters()) {
			if (rosterExists (r) && (readMode == READ_MODE_ADD)) {
				s += "Ignoriere doppelten Fahrtenleiter " + r.getGivenName() + " " + r.getFamilyName() + "\n";
			}
			else {
				add (r.getFamilyName(), r.getGivenName(), r.getPhoneNumber(), r.getIsAspirant());
				s += "Neuer Fahrtenleiter " + r.getGivenName() + " " + r.getFamilyName() + "\n";
			}
		}
		setIsDataChanged (true);
		
  		JOptionPane.showMessageDialog(null,
				s, "Fahrtenleiter importiert",
				JOptionPane.OK_OPTION);

	}

}
