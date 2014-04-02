package riverDB;

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
	
	public void write (String fileName) throws Exception{
	    Serializer serializer = new Persister();
	    File file = new File(fileName);
	    serializer.write(this, file);
    	isDataChanged = false;

	}

	public static RiverDB read (String fileName) throws Exception{
		Serializer serializer = new Persister();
		File file = new File(fileName);
		RiverDB riverDB = serializer.read(RiverDB.class, file);
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

	
}
