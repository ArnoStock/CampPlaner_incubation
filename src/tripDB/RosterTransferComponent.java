package tripDB;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import rosterDB.Roster;

@SuppressWarnings("serial")
public class RosterTransferComponent extends Component implements Transferable {

	Integer rosterID;
    public static final DataFlavor ROSTER_COMPONENT_DATA_FLAVOR = new DataFlavor(Roster.class, "java/RosterComponent");
	
	public RosterTransferComponent (RosterComponent rc) {
		this.rosterID = rc.getRoster().getRosterID();
	}
	
	/**
	 * @return the roster
	 */
	public Integer getRosterID() {
		return rosterID;
	}

	/**
	 * @param roster the roster to set
	 */
	public void setRosterID(int rosterID) {
		this.rosterID = rosterID;
	}
	
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		return rosterID;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[]{ROSTER_COMPONENT_DATA_FLAVOR};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(ROSTER_COMPONENT_DATA_FLAVOR);
	}

	
}
