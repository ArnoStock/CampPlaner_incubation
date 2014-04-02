package tripDB;

import java.awt.Component;

import riverDB.River;

@SuppressWarnings("serial")
public class TripComponent extends Component {

	Trip trip;
	
	public TripComponent(Trip trip) {
		
		this.trip = trip;
		
	}
	
	public void setTrip (Trip trip) {
		this.trip = trip;
	}
	
	public Trip getTrip () {
		return trip;
	}
	
	public String getRiverName () {
		River r = trip.getRiver();
		if (r == null) {
			return "<?>";
		}
		return r.getRiverName ();
	}

	public String getRosterNames() {
		
		if ((trip == null) || (trip.getRosterCount() == 0)) {
			return "<?>";
		}
		
		String r ="";
		
		for (int i = 0; i < trip.getRosterCount(); i++) {
			if (i > 0)
				r = r + ", ";
			r = r + trip.getRosterName(i);
		}
		return r;
	}

	public String getWwTopLevel() {
		River r = trip.getRiver();
		if (r == null) {
			return "0";
		}
		return ""+r.getWwTopLevel ();
	}

	public String getFromToString() {

		River r = trip.getRiver();
		if (r == null) {
			return "<?> \u25BA <?>";
		}

		return r.getTripFrom() + " \u25BA " + r.getTripTo();
	}
	
}
