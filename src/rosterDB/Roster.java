package rosterDB;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@SuppressWarnings("serial")
@Root(name="Roster")
public class Roster extends Component {

    private final PropertyChangeSupport rosterChangeSupport = new PropertyChangeSupport(this);

    public Roster() {
		givenName = "Vorname";
		familyName = "Hausname";
		nickName = "";
		phoneNumber = "+49";
		isAspirant = false;
		rosterAvailability = new ArrayList<RosterAvailability>();
		this.rosterID = 0;
	}

    public Roster(int rosterID) {
		givenName = "Vorname";
		familyName = "Hausname";
		nickName = "";
		phoneNumber = "+49";
		isAspirant = false;
		rosterAvailability = new ArrayList<RosterAvailability>();
		this.rosterID = rosterID;
	}

	public Roster(int rosterID, String familyName, String givenName, String nickName, String phoneNumber, Boolean isAspirant) {
		this.givenName = givenName;
		this.familyName = familyName;
		this.nickName = nickName;
		this.phoneNumber = phoneNumber;
		this.isAspirant = isAspirant;
		rosterAvailability = new ArrayList<RosterAvailability>();
		this.rosterID = rosterID;
	}

	@Attribute
    private int rosterID;

	@Attribute
    private String givenName;

    @Attribute
    private String familyName;
    
    @Attribute
    private String nickName;

    @Attribute
    private String phoneNumber;
    
    @Attribute (required=false)
    private Boolean isAspirant;
    
    @ElementList(name="Available")
    private ArrayList<RosterAvailability> rosterAvailability;
	
	public RosterAvailability checkAvailability (Date d) {
	
		DateFormat df = new SimpleDateFormat("dd.MM.YYYY");
		String refDate = df.format(d);

		for (RosterAvailability r: rosterAvailability) {
	
			String rDate = df.format(r.getDate());
			if (refDate.equals(rDate))
				return r;
		}
		return null;
		
	}
	
	public boolean isAvailableAt(Date targetDay) {

		RosterAvailability ra = checkAvailability (targetDay);
		if (ra == null)
			return false;
		
		if (ra.getAvailabilityCode().equals(RosterAvailability.ROSTER_AVAILABLE))
			return true;
		
		return false;
	}
	

	/**
	 * @return the givenName
	 */
	public String getGivenName() {
		return givenName;
	}

	/**
	 * @param givenName the givenName to set
	 */
	public void setGivenName(String givenName) {
		String oldGivenName = this.givenName;
		this.givenName = givenName;
		rosterChangeSupport.firePropertyChange("givenName", oldGivenName, givenName);
	}

	/**
	 * @return the familyName
	 */
	public String getFamilyName() {
		return familyName;
	}

	/**
	 * @param familyName the familyName to set
	 */
	public void setFamilyName(String familyName) {
		String oldFamilyName = this.familyName;
		this.familyName = familyName;
		rosterChangeSupport.firePropertyChange("familyName", oldFamilyName, familyName);
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		String oldNickName = this.nickName;
		this.nickName = nickName;
		rosterChangeSupport.firePropertyChange("nickName", oldNickName, nickName);
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		String oldPhoneNumber = this.phoneNumber;
		this.phoneNumber = phoneNumber;
		rosterChangeSupport.firePropertyChange("phoneNumber", oldPhoneNumber, phoneNumber);
	}

    /**
	 * @return the isAspirant
	 */
	public Boolean getIsAspirant() {
		return isAspirant;
	}

	/**
	 * @param isAspirant the isAspirant to set
	 */
	public void setIsAspirant(Boolean isAspirant) {

		Boolean oldIsAspirant = this.isAspirant;
		this.isAspirant = isAspirant;
		rosterChangeSupport.firePropertyChange("isAspirant", oldIsAspirant, isAspirant);
		
	}

	/**
	 * @return the rosterAvailability
	 */
	public List<RosterAvailability> getRosterAvailability() {
		return rosterAvailability;
	}

	/**
	 * @param rosterAvailability the rosterAvailability to set
	 */
/*	public void setRosterAvailability(ArrayList<RosterAvailability> rosterAvailability) {
		this.rosterAvailability = rosterAvailability;
		rosterChangeSupport.firePropertyChange("givenName", oldGivenName, givenName);
	}*/
	
	@Override
	public String toString () {
		return givenName + " " + familyName + " (" + nickName + ") ";
	}
	
	public int getRosterAvailabilityCode (Date date) {
		
		// check roster's availability code for the date. Default is non available
		for (RosterAvailability ra : rosterAvailability) {
			if (ra.getDate().compareTo(date) == 0)
				return ra.getAvailabilityCode();
		}
		
		return RosterAvailability.ROSTER_ABSENT;
	}

	public void setAvailability(Date dateAt, Object val) {

		// check, if date is already existing
		RosterAvailability r = checkAvailability (dateAt);
		if (r == null) {
			rosterAvailability.add(new RosterAvailability (dateAt.getTime(), (int)val));
			rosterChangeSupport.firePropertyChange("rosterAvailability", null, val);
		}
		else {
			Integer oldVal = r.getAvailabilityCode();
			r.setAvailabilityCode ((int)val);
			rosterChangeSupport.firePropertyChange("rosterAvailability", oldVal, val);
		}
		
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.rosterChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.rosterChangeSupport.removePropertyChangeListener(listener);
    }

	public int getRosterID() {
		return rosterID;
	}

}

