package gui;

import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.UIManager;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import printOut.PrintElementSetupList;

@SuppressWarnings("serial")
@Root(name="Setup")
public class SetupData extends Component {
	
	private final PropertyChangeSupport setupChangeSupport = new PropertyChangeSupport(this);
	
	public SetupData () {
		super ();
		eventStartDate = new Date ();
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(eventStartDate);
		rightNow.add(Calendar.DATE, 1);
		eventEndDate = rightNow.getTime();
		printElementsSetups = new PrintElementSetupList ((Font) UIManager.getDefaults().get("TextField.font"));
		dataFileName = "TripDB.cplan";
		formFileName = "FAHRTENZETTEL11.jpg";
		printOnFormSheet = new Boolean (true);
	}
	
    @Attribute
    private Date eventStartDate;
    
    @Attribute
    private Date eventEndDate;
    
    @Attribute String dataFileName;
    
    @Attribute String formFileName;
    
    @Attribute Boolean printOnFormSheet;

    @Element
    private PrintElementSetupList printElementsSetups;
    
    public PrintElementSetupList getPrintElementSetupList () {
    	return printElementsSetups;
    }
    
    public void setPrintElementSetupList (PrintElementSetupList setupList) {
    	printElementsSetups = setupList;
    }
    
	/**
	 * @return the eventStartDate
	 */
	public Date getEventStartDate() {
		return eventStartDate;
	}

	/**
	 * @param eventStartDate the eventStartDate to set
	 */
	public void setEventStartDate(Date eventStartDate) {
		Date oldDate = this.eventStartDate;
		this.eventStartDate = eventStartDate;
		setupChangeSupport.firePropertyChange("eventStartDate", oldDate, eventStartDate);
	}

	/**
	 * @return the eventEndDate
	 */
	public Date getEventEndDate() {
		return eventEndDate;
	}

	/**
	 * @param eventEndDate the eventEndDate to set
	 */
	public void setEventEndDate(Date eventEndDate) {
		Date oldDate = this.eventEndDate;
		this.eventEndDate = eventEndDate;
		setupChangeSupport.firePropertyChange("eventEndDate", oldDate, eventEndDate);
	}

	public void write (String fileName) throws Exception{
	    Serializer serializer = new Persister();
	    File file = new File(fileName);
	    serializer.write(this, file);
	}

	public static SetupData read (String fileName) throws Exception{
		Serializer serializer = new Persister();
		File file = new File(fileName);
		SetupData setupData = serializer.read(SetupData.class, file);
		return setupData;
	}
	
	static final long ONE_HOUR = 60 * 60 * 1000L;
    public long daysBetween(Date d1, Date d2){
        return ( (d2.getTime() - d1.getTime() + ONE_HOUR) / (ONE_HOUR * 24));
    }

	public long getEventLength() {
		return daysBetween (eventStartDate, eventEndDate);
	}
	
	public String getDateAsString (int day) {

		DateFormat df = new SimpleDateFormat("dd.MM.");
		return df.format(getDateAt (day));
	}

	public Date getDateAt (int day) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(eventStartDate);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}
	
	public String getDataFileName () {
		return dataFileName;
	}

	public String getFormFileName () {
		return formFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}

	public void setFormFileName(String formFileName) {
		this.formFileName = formFileName;
	}

	/**
	 * @return the printOnFormSheet
	 */
	public Boolean getPrintOnFormSheet() {
		if (printOnFormSheet == null)
			printOnFormSheet = new Boolean (true);
		return printOnFormSheet;
	}

	/**
	 * @param printOnFormSheet the printOnFormSheet to set
	 */
	public void setPrintOnFormSheet(Boolean printOnFormSheet) {
		this.printOnFormSheet = printOnFormSheet;
	}

    public void addPropertyChangeListener(PropertyChangeListener listener) {
  	
        this.setupChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {

    	this.setupChangeSupport.removePropertyChangeListener(listener);
    }

}
