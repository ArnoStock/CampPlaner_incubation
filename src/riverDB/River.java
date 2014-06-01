package riverDB;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
 
@SuppressWarnings("serial")
@Root(name="River")
public class River extends Component implements Comparable<River> {

    private final PropertyChangeSupport riverChangeSupport = new PropertyChangeSupport(this);
	
	public River () {
		super ();
		riverName = "Flussname";
		tripFrom = "Einstieg";
		tripTo = "Ausstieg";
		wwLevel = "1-6";
		wwTopLevel = 1;
		tripLength = 0;
		distanceToStart = 0;
		riverInfo = "";
		minWaterLevel = 0;
		maxWaterLevel = 0;
		unitOfWaterLevel = "cm";
		defaultGroupSize = 1;
		country = "";
	}

	public River (PropertyChangeListener parent, String riverName, String tripFrom, String tripTo,
					String wwLevel, int wwTopLevel, int tripLength, int distanceToStart, String country, 
					String riverInfo, int minWaterLevel, int maxWaterLevel, String unitOfWaterLevel, Integer defaultGroupSize) {
		super ();
		this.riverName = riverName;
		this.tripFrom = tripFrom;
		this.tripTo = tripTo;
		this.wwLevel = wwLevel;
		this.wwTopLevel = wwTopLevel;
		this.tripLength = tripLength;
		this.distanceToStart = distanceToStart;
		this.riverInfo = riverInfo;
		this.minWaterLevel = minWaterLevel;
		this.maxWaterLevel = maxWaterLevel;
		this.unitOfWaterLevel = unitOfWaterLevel;
		this.country = country;
		this.defaultGroupSize = defaultGroupSize;
		riverChangeSupport.addPropertyChangeListener(parent);
	}
	
    public River(PropertyChangeListener parent) {

		super ();
		riverName = "Flussname";
		tripFrom = "Einstieg";
		tripTo = "Ausstieg";
		wwLevel = "1-6";
		wwTopLevel = 1;
		tripLength = 0;
		distanceToStart = 0;
		minWaterLevel = 0;
		maxWaterLevel = 0;
		riverInfo = "";
		country = "";
		unitOfWaterLevel = "cm";
		defaultGroupSize = 6;
		riverChangeSupport.addPropertyChangeListener(parent);

	}

	public River(River r, PropertyChangeListener parent) {

		riverName = r.getRiverName();
		tripFrom = r.getTripFrom();
		tripTo = r.getTripTo();
		wwLevel = r.getWwLevel();
		wwTopLevel = r.getWwTopLevel();
		tripLength = r.getTripLength();
		distanceToStart = r.getDistanceToStart();
		minWaterLevel = r.getMinWaterLevel();
		maxWaterLevel = r.getMaxWaterLevel();
		unitOfWaterLevel = r.getUnitOfWaterLevel();
		defaultGroupSize = r.getDefaultGroupSize();
		riverChangeSupport.addPropertyChangeListener(parent);
		
	}

	@Attribute
    private String riverName;

    @Attribute
    private String riverInfo;

    @Attribute
    private String country;
    
    @Attribute
    private String tripFrom;

    @Attribute
    private String tripTo;
    
    @Attribute
    private String wwLevel;

    @Attribute
    private int wwTopLevel;
 
    @Attribute
    private int tripLength;

    @Attribute
    private int distanceToStart;

    /*
     * recommended min water level in unitOfWaterLevel
     */
    @Attribute
    private int minWaterLevel;
    
    /*
     * recommended max water level in unitOfWaterLevel
     */
    @Attribute
    private int maxWaterLevel;

    @Attribute
    private String unitOfWaterLevel;

    @Attribute
    private Integer defaultGroupSize;
    
	/**
	 * @return the defaultGroupSize
	 */
	public Integer getDefaultGroupSize() {
		return defaultGroupSize;
	}

	/**
	 * @param defaultGroupSize the defaultGroupSize to set
	 */
	public void setDefaultGroupSize(Integer defaultGroupSize) {
		Integer oldDefaultGroupSize = this.defaultGroupSize;
		this.defaultGroupSize = defaultGroupSize;
		riverChangeSupport.firePropertyChange("defaultGroupSize", oldDefaultGroupSize, defaultGroupSize);
	}

	/**
	 * @return the riverName
	 */
	public String getRiverName() {
		return riverName;
	}

	/**
	 * @param riverName the riverName to set
	 */
	public void setRiverName(String riverName) {
		String oldRiverName = this.riverName;
		this.riverName = riverName;
		riverChangeSupport.firePropertyChange("riverName", oldRiverName, riverName);
	}

	/**
	 * @return the tripFrom
	 */
	public String getTripFrom() {
		return tripFrom;
	}

	/**
	 * @param tripFrom the tripFrom to set
	 */
	public void setTripFrom(String tripFrom) {
		String oldTripFrom = this.tripFrom;
		this.tripFrom = tripFrom;
		riverChangeSupport.firePropertyChange("tripFrom", oldTripFrom, tripFrom);
	}

	/**
	 * @return the tripTo
	 */
	public String getTripTo() {
		return tripTo;
	}

	/**
	 * @param tripTo the tripTo to set
	 */
	public void setTripTo(String tripTo) {
		String oldTripTo = this.tripTo;
		this.tripTo = tripTo;
		riverChangeSupport.firePropertyChange("tripTo", oldTripTo, tripTo);
	}

	/**
	 * @return the wwLevel
	 */
	public String getWwLevel() {
		return wwLevel;
	}

	/**
	 * @param wwLevel the wwLevel to set
	 */
	public void setWwLevel(String wwLevel) {
		String oldwwLevel = this.wwLevel;
		this.wwLevel = wwLevel;
		riverChangeSupport.firePropertyChange("wwLevel", oldwwLevel, wwLevel);
	}

	/**
	 * @return the wwTopLevel
	 */
	public int getWwTopLevel() {
		return wwTopLevel;
	}

	/**
	 * @param wwTopLevel the wwTopLevel to set
	 */
	public void setWwTopLevel(int wwTopLevel) {
		int oldWwTopLevel = this.wwTopLevel;
		this.wwTopLevel = wwTopLevel;
		riverChangeSupport.firePropertyChange("wwTopLevel", oldWwTopLevel, wwTopLevel);
	}

	/**
	 * @return the tripLength
	 */
	public int getTripLength() {
		return tripLength;
	}

	/**
	 * @param tripLength the tripLength to set
	 */
	public void setTripLength(int tripLength) {
		int oldTripLength = this.tripLength;
		this.tripLength = tripLength;
		riverChangeSupport.firePropertyChange("tripLength", oldTripLength, tripLength);
	}

	/**
	 * @return the distanceToStart
	 */
	public int getDistanceToStart() {
		return distanceToStart;
	}

	/**
	 * @param distanceToStart the distanceToStart to set
	 */
	public void setDistanceToStart(int distanceToStart) {
		int oldDistanceToStart = this.distanceToStart;
		this.distanceToStart = distanceToStart;
		riverChangeSupport.firePropertyChange("distanceToStart", oldDistanceToStart, distanceToStart);
	}
	
	/**
	 * @return the riverInfo
	 */
	public String getRiverInfo() {
		return riverInfo;
	}

	/**
	 * @param riverInfo the riverInfo to set
	 */
	public void setRiverInfo(String riverInfo) {
		String oldRiverInfo = this.riverInfo;
		this.riverInfo = riverInfo;
		riverChangeSupport.firePropertyChange("riverInfo", oldRiverInfo, riverInfo);
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		String oldCountry = this.country;
		this.country = country;
		riverChangeSupport.firePropertyChange("country", oldCountry, country);
	}
	
	

	/**
	 * @return the minWaterLevel
	 */
	public int getMinWaterLevel() {
		return minWaterLevel;
	}

	/**
	 * @param minWaterLevel the minWaterLevel to set
	 */
	public void setMinWaterLevel(int minWaterLevel) {
		int oldMinWaterLevel = this.minWaterLevel;
		this.minWaterLevel = minWaterLevel;
		riverChangeSupport.firePropertyChange("minWaterLevel", oldMinWaterLevel, minWaterLevel);
	}

	/**
	 * @return the maxWaterLevel
	 */
	public int getMaxWaterLevel() {
		return maxWaterLevel;
	}

	/**
	 * @param maxWaterLevel the maxWaterLevel to set
	 */
	public void setMaxWaterLevel(int maxWaterLevel) {
		int oldMaxWaterLevel = this.maxWaterLevel;
		this.maxWaterLevel = maxWaterLevel;
		riverChangeSupport.firePropertyChange("maxWaterLevel", oldMaxWaterLevel, maxWaterLevel);
	}

	/**
	 * @return the unitOfWaterLevel
	 */
	public String getUnitOfWaterLevel() {
		return unitOfWaterLevel;
	}

	/**
	 * @param unitOfWaterLevel the unitOfWaterLevel to set
	 */
	public void setUnitOfWaterLevel(String unitOfWaterLevel) {
		String oldUnitOfWaterLevel = this.unitOfWaterLevel;
		this.unitOfWaterLevel = unitOfWaterLevel;
		riverChangeSupport.firePropertyChange("unitOfWaterLevel", oldUnitOfWaterLevel, unitOfWaterLevel);
	}

	@Override
	public String toString () {
		return riverName + " (" + tripFrom + " - " + tripTo + ") " + tripLength + "km WW" + wwLevel;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.riverChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.riverChangeSupport.removePropertyChangeListener(listener);
    }

	@Override
	public int compareTo(River r) {
		
		String mySortString = riverName + tripFrom + tripTo;
		String otherSortString = r.riverName + r.tripFrom + r.tripTo;
		
		return mySortString.compareTo(otherSortString);
	}

}
