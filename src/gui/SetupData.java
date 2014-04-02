package gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.TextField;
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
	
	public SetupData () {
		super ();
		eventStartDate = new Date ();
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(eventStartDate);
		rightNow.add(Calendar.DATE, 1);
		eventEndDate = rightNow.getTime();
		printElementsSetups = new PrintElementSetupList ((Font) UIManager.getDefaults().get("TextField.font"));
		fileName = "TripDB.cplan";
	}
	
    @Attribute
    private Date eventStartDate;
    
    @Attribute
    private Date eventEndDate;
    
    @Attribute String fileName;
    
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
		this.eventStartDate = eventStartDate;
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
		this.eventEndDate = eventEndDate;
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
	
	public String getFileName () {
		
		return fileName;
		
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

   
}
