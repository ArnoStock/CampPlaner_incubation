package tripDB;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import riverDB.River;

@SuppressWarnings("serial")
public class RiverComponent extends Component implements Transferable {

	River river;
    public static final DataFlavor RIVER_COMPONENT_DATA_FLAVOR = new DataFlavor(River.class, "java/RiverComponent");
	
	public RiverComponent (River river) {
		this.river = river;
	}

	/**
	 * @return the river
	 */
	public River getRiver() {
		return river;
	}

	/**
	 * @param river the river to set
	 */
	public void setRiver(River river) {
		this.river = river;
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		return river;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[]{RIVER_COMPONENT_DATA_FLAVOR};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(RIVER_COMPONENT_DATA_FLAVOR);
	}	
	
}
