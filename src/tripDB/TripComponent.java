package tripDB;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import riverDB.River;
import rosterDB.Roster;

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

	public Color getRosterColor() {

		if ((trip == null) || (trip.getRosterCount() == 0)) {
			return Color.RED;
		}
		
		int rcnt = 0;
		for (Roster r : trip.getRosterList()) {
			if (!r.isAvailableAt(trip.getTripDate()))
					return Color.RED;
			if (!r.getIsAspirant())
				rcnt++;
		}
		
		if (rcnt == 0)
			return Color.RED;
		if (rcnt > 1)
			return Color.decode ("0xF07000");
		return Color.BLACK;
	}

	public List<Roster> getTripRosterList () {
		return trip.getRosterList();
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
