package rosterDB;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

@Root(name="RosterDB")
public class RosterDB implements PropertyChangeListener {

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
	
	public static RosterDB read (String fileName) throws Exception{
		Serializer serializer = new Persister();
		File file = new File(fileName);
		RosterDB rosterDB = serializer.read(RosterDB.class, file);
		return rosterDB;
	}


	public void add(String familyName, String givenName, String phone, Boolean isAspirant) {
		
		rosters.add(new Roster( getNextRosterID(), familyName, givenName, "", phone, isAspirant));
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
		return nr;
	}

}
