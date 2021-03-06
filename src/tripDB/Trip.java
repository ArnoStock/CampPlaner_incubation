package tripDB;

import gui.MainWindow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import riverDB.River;
import rosterDB.Roster;

@Root(name="Trip")
public class Trip implements Comparable<Trip>{
	
	public Trip (Date tripDate) {
		super ();
		this.rosters = new ArrayList<Integer> ();
		this.river = null;
		this.tripDate = tripDate;
		this.groupNumber = 0;
		this.totalGroupSize = 2;
		this.isEducation= false; 
		this.isKidsTour = false;
		this.driverCount = 2;
		this.comment = "";

		SimpleDateFormat sdfWithDefaultYear = new SimpleDateFormat("HH:mm");
		try {
			tripStartTime = sdfWithDefaultYear.parse("10:00");
		} catch (ParseException e) {
			tripStartTime = new Date();
		}
		
	}
	
	public Trip () {
		super ();
		this.rosters = new ArrayList<Integer> ();
		this.river = null;
		this.tripDate = null;
		this.groupNumber = 0;
		this.totalGroupSize = 2;
		this.isEducation= false; 
		this.isKidsTour = false;
		this.driverCount = 2;
		this.comment = "";

		SimpleDateFormat sdfWithDefaultYear = new SimpleDateFormat("HH:mm");
		try {
			tripStartTime = sdfWithDefaultYear.parse("10:00");
		} catch (ParseException e) {
			tripStartTime = new Date();
		}
	}


	public Trip(Date tripDate, Trip tc) {

		super ();
		this.rosters = new ArrayList<Integer> ();
		this.river = tc.getRiver();
		this.tripDate = tripDate;
		this.groupNumber = 0;
		this.totalGroupSize = tc.getTotalGroupSize();
		this.isEducation= tc.getIsEducation();
		this.isKidsTour = tc.getIsKidsTour();
		this.driverCount = tc.getDriverCount();
		this.comment = tc.getComment();
		tripStartTime = tc.getTripStartTime();
	}


	@ElementList(name="Rosters")
	List<Integer> rosters;

	@Element(name="River", required=false)
	River river;

	// time and date is separated for handling reasons
	@Attribute
	Date tripDate;
	
	@Attribute
	Date tripStartTime;

	@Attribute
	Integer groupNumber;
	

	@Attribute
	Integer totalGroupSize;
	
	@Attribute
	Integer driverCount;
	
	@Attribute
	String comment;

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment.replaceAll("&#xD;","\n");
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment.replaceAll("\n", "&#xD;");
		MainWindow.tripDB.tripDataChanged();
	}

	/**
	 * @return the driverCount
	 */
	public Integer getDriverCount() {
		return driverCount;
	}

	/**
	 * @param driverCount the driverCount to set
	 */
	public void setDriverCount(Integer driverCount) {
		this.driverCount = driverCount;
		MainWindow.tripDB.tripDataChanged();
	}

	@Attribute
	boolean isEducation;

	// driverCount is not included in the totalGroupSize
	@Attribute (required = false)
	boolean isKidsTour;

	public River getRiver () {
		return river;
	}
	
	public int getRosterCount () {
		return rosters.size();
	}
	
	public String getRosterName (int i) {
		Roster r = MainWindow.rosterDB.getRosterByID (rosters.get(i));
		if (r.getIsAspirant())
			return "HFL->" + r.getGivenName() + " " + r.getFamilyName() + "<";
		return r.getGivenName() + " " + r.getFamilyName();
	}
	
	public String getRosterPhoneNumber (int i) {
		Roster r = MainWindow.rosterDB.getRosterByID (rosters.get(i));
		return r.getPhoneNumber();
	}
	
	public void setRiver (River river) {
		this.river = river;
		comment = river.getCommentUnchanged();
		totalGroupSize = river.getDefaultGroupSize();
		MainWindow.tripDB.tripDataChanged();
	}
	
	public void addRoster (Roster roster) {

		if (roster == null)
			return;
		
		if (rosters.contains(roster.getRosterID())) {
			return;
		}
		rosters.add(roster.getRosterID());
	}

	public void removeRoster (Roster roster) {
		
		if (roster == null)
			return;
		
		if (rosters.contains(roster.getRosterID())) {
			rosters.remove ((Object)roster.getRosterID());
		}
		
	}

	public Date getTripDate() {
		return tripDate;
	}
	
	/**
	 * @return the tripStartTime
	 */
	public Date getTripStartTime() {
		return tripStartTime;
	}

	/**
	 * @param tripStartTime the tripStartTime to set
	 */
	public void setTripStartTime(Date tripStartTime) {
		this.tripStartTime = tripStartTime;
		MainWindow.tripDB.tripDataChanged();
	}

	/**
	 * @return the totalGroupSize
	 */
	public Integer getTotalGroupSize() {
		return totalGroupSize;
	}

	/**
	 * @param totalGroupSize the totalGroupSize to set
	 */
	public void setTotalGroupSize(Integer totalGroupSize) {
		this.totalGroupSize = totalGroupSize;
		MainWindow.tripDB.tripDataChanged();
	}

	public Integer getTripLength() {

		if (river == null)
			return 0;
		return river.getTripLength();
	
	}

	public String getTripFrom() {

		if (river == null)
			return "<?>";
		return river.getTripFrom();
	}
	
	public String getTripTo() {

		if (river == null)
			return "<?>";
		return river.getTripTo();
	}

	public String getWwLevel() {
		if (river == null)
			return "<?>";
		return river.getWwLevel();
	}

	public Integer getDistanceToStart() {
		if (river == null)
			return 0;
		return river.getDistanceToStart();
	}

	public boolean getIsEducation() {
		return isEducation;
	}

	public void setIsEducation(boolean isEducation) {
		this.isEducation= isEducation ;
		MainWindow.tripDB.tripDataChanged();
	}
	
	public boolean getIsKidsTour() {
		return isKidsTour;
	}

	public void setIsKidsTour(boolean isKidsTour) {
		this.isKidsTour = isKidsTour;
		MainWindow.tripDB.tripDataChanged();
	}

	public List<Roster> getRosterList() {
		ArrayList<Roster> tripRosters = new ArrayList<Roster> ();
		for (Integer i: rosters) {
			tripRosters.add(MainWindow.rosterDB.getRosterByID(i));
		}
		return tripRosters;
	}

	public Integer getGroupNumber() {
		return groupNumber;
	}
	
	/**
	 * @param groupNumber the groupNumber to set
	 */
	public void setGroupNumber(Integer groupNumber) {
		this.groupNumber = groupNumber;
		MainWindow.tripDB.tripDataChanged();
	}

	public Integer getGroupSize() {
		return totalGroupSize - rosters.size();
	}
	
	public boolean isRosterAssigned (Integer id) {
		return rosters.contains(id);
	}

	@Override
	public int compareTo(Trip t) {
		
		return getGroupNumber() - t.getGroupNumber();
	}


}
