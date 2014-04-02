package tripDB;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;

import rosterDB.Roster;

@SuppressWarnings("serial")
public class RosterComponent extends Component {

	Roster roster;
    public static final DataFlavor ROSTER_COMPONENT_DATA_FLAVOR = new DataFlavor(Roster.class, "java/RosterComponent");
	
	public RosterComponent (Roster roster) {
		this.roster = roster;
	}
	
	/**
	 * @return the roster
	 */
	public Roster getRoster() {
		return roster;
	}

	/**
	 * @param roster the roster to set
	 */
	public void setRoster(Roster roster) {
		this.roster = roster;
	}
	
	@Override
	public String toString () {
		return roster.getGivenName() + " " + roster.getFamilyName();
	}
	
}
