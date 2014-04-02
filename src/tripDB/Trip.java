package tripDB;

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
public class Trip {
	
	public Trip (Date tripDate) {
		super ();
		this.rosters = new ArrayList<Roster> ();
		this.river = null;
		this.tripDate = tripDate;
		this.groupNumber = 0;
		this.totalGroupSize = 2;
		this.isEducation= false; 
		this.driverCount = 1;
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
		this.rosters = new ArrayList<Roster> ();
		this.river = null;
		this.tripDate = null;
		this.groupNumber = 0;
		this.totalGroupSize = 2;
		this.isEducation= false; 
		this.driverCount = 1;
		this.comment = "";

		SimpleDateFormat sdfWithDefaultYear = new SimpleDateFormat("HH:mm");
		try {
			tripStartTime = sdfWithDefaultYear.parse("10:00");
		} catch (ParseException e) {
			tripStartTime = new Date();
		}
	}


	@ElementList(name="Rosters")
	List<Roster> rosters ;

	@Element(name="River")
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
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
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
	}


	@Attribute
	boolean isEducation;

	public River getRiver () {
		return river;
	}
	
	public int getRosterCount () {
		return rosters.size();
	}
	
	public String getRosterName (int i) {
		return rosters.get(i).getGivenName() + " " + rosters.get(i).getFamilyName();
	}
	
	public void setRiver (River river) {
		this.river = river;
		totalGroupSize = river.getDefaultGroupSize();
	}
	
	public void addRoster (Roster roster) {

		if (roster == null)
			return;
		
		if (rosters.contains(roster)) {
			return;
		}
		rosters.add(roster);
	}

	public void removeRoster (Roster roster) {
		
		if (roster == null)
			return;
		
		if (rosters.contains(roster)) {
			rosters.remove(roster);
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
	}

	public List<Roster> getRosterList() {
		return rosters;
	}

	public Integer getGroupNumber() {
		return groupNumber;
	}
	
	/**
	 * @param groupNumber the groupNumber to set
	 */
	public void setGroupNumber(Integer groupNumber) {
		this.groupNumber = groupNumber;
	}

	public Integer getGroupSize() {
		return totalGroupSize - rosters.size();
	}


}
