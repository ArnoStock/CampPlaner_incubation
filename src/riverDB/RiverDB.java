package riverDB;

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

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.Ostermiller.util.CSVParser;
import com.Ostermiller.util.LabeledCSVParser;
 
@Root(name="RiverDB")
public class RiverDB implements PropertyChangeListener {

    @ElementList(name="Rivers")
    private List <River> rivers;
	private boolean isDataChanged;
	private final PropertyChangeSupport riverChangeSupport = new PropertyChangeSupport(this);
    
    public RiverDB () {
    	super ();
    	rivers = new ArrayList<River>();
    	isDataChanged = false;
    }
    
	public void add() {
		
		rivers.add(new River(this));
    	isDataChanged = true;
		
	}

	public void add(	String riverName, String tripFrom, String tripTo, String wwLevel, int wwTopLevel, 
						int tripLength, int distanceToStart, String country, String riverInfo, 
						int minWaterLevel, int maxWaterLevel, String unitOfWaterLevel, int defaultGroupSize) {
		
		rivers.add(	new River( 	this, riverName, tripFrom, tripTo, wwLevel, wwTopLevel, tripLength, distanceToStart, 
								country, riverInfo, minWaterLevel, maxWaterLevel, unitOfWaterLevel, defaultGroupSize));
    	isDataChanged = true;
	
	}
	
	public void write (String fileName) throws Exception {
	    Serializer serializer = new Persister();
	    File file = new File(fileName);
	    serializer.write(this, file);
    	isDataChanged = false;
	}

	public void write(ZipOutputStream zipOut) throws Exception {
		// save XML document to ZIP file
		ZipEntry entry = new ZipEntry("Rivers.xml");
		zipOut.putNextEntry(entry);
		
		Serializer serializer = new Persister();
	    serializer.write(this, zipOut);

		zipOut.closeEntry();
	    isDataChanged = false;
	}

	public static RiverDB read (String fileName) throws Exception {
		
		Serializer serializer = new Persister();
		File file = new File(fileName);
		RiverDB riverDB = serializer.read(RiverDB.class, file);
		return riverDB;

	}
	
	public static RiverDB read (ZipFile zipIn) throws Exception {
		
		ZipEntry riverZipObj = zipIn.getEntry("Rivers.xml");
		InputStream zipInStream = zipIn.getInputStream(riverZipObj);
		
		Serializer serializer = new Persister();
		RiverDB riverDB = serializer.read(RiverDB.class, zipInStream);

		zipInStream.close();
		
		return riverDB;
	}

	
	public List<River> getAllRivers () {
		
		return rivers;
	}
	
	@Override
	public String toString () {
		String s = "";
		for (River r: rivers) {
			s += r.toString() + "\n";
		}
		return s;
	}

	public boolean isDataChanged() {
		return isDataChanged;
	}
	
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    	
        this.riverChangeSupport.addPropertyChangeListener(listener);
		for (River r: rivers) {
			r.addPropertyChangeListener(this);
		}
        
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.riverChangeSupport.removePropertyChangeListener(listener);
		for (River r: rivers) {
			r.removePropertyChangeListener(this);
		}
    }

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		isDataChanged = true;
		riverChangeSupport.firePropertyChange(evt);
		
	}

	public void removeRiver(River r) {
		
		isDataChanged = true;
		rivers.remove(r);
		riverChangeSupport.firePropertyChange("removedRiver", true, false);
		
	}

	public River cloneRiver(River r) {
		
		River nr = new River (r, this);
		rivers.add (nr);
		riverChangeSupport.firePropertyChange("addedRiver", true, false);
		return nr;
	}
	
	public River newRiver() {

		River nr = new River (this);
		rivers.add (nr);
		riverChangeSupport.firePropertyChange("addedRiver", true, false);
		return nr;
	}
	
	public static RiverDB readCSV (String fName) {
		
		RiverDB riverDB = new RiverDB ();
	
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
					        "Parse Fluss: " + lcsvp.getValueByLabel("Fluss") +
					        " Start: " + lcsvp.getValueByLabel("Einstieg") +
					        " Ziel: " + lcsvp.getValueByLabel("Ausstieg")
					    );

					
					riverDB.add (lcsvp.getValueByLabel("Fluss"), lcsvp.getValueByLabel("Einstieg"), lcsvp.getValueByLabel("Ausstieg"), 
							lcsvp.getValueByLabel("WW"), Integer.parseInt(lcsvp.getValueByLabel("WW Stufe")), 
							Integer.parseInt(lcsvp.getValueByLabel("Km")), Integer.parseInt(lcsvp.getValueByLabel("Entfernung")),
							lcsvp.getValueByLabel("Land"), lcsvp.getValueByLabel("Info"),
							Integer.parseInt(lcsvp.getValueByLabel("Min")), Integer.parseInt(lcsvp.getValueByLabel("Max")), lcsvp.getValueByLabel("Einheit"), 
							Integer.parseInt(lcsvp.getValueByLabel("Teilnehmer")));

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
			 
		return riverDB;
	}
	
	public static boolean writeCSV(RiverDB riverDB, String absolutePath) {
		// create output file
        FileWriter outputStream = null;

	    try {
	    	outputStream = new FileWriter(absolutePath);
	    	outputStream.write("Nr.,Land,Fluss,Einstieg,Ausstieg,Entfernung,Km,WW,WW Stufe,Info,Min,Max,Einheit,Teilnehmer\n");
	    	int i = 1;
	    	for (River r: riverDB.getAllRivers()) {
		    	outputStream.write( i++ + "," + r.getCountry() + "," + r.getRiverName() + "," + r.getTripFrom() + "," +
		    						r.getTripTo() + "," + r.getDistanceToStart() + "," + r.getTripLength() + "," +
		    						r.getWwLevel() + "," + r.getWwTopLevel() + "," + r.getRiverInfo() + "," +
		    						r.getMinWaterLevel() + "," + r.getMaxWaterLevel() + "," + r.getUnitOfWaterLevel() + "," +
		    						r.getDefaultGroupSize() + "\n");
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
		riverChangeSupport.firePropertyChange("loadedRivers", true, false);

	}

	
}
