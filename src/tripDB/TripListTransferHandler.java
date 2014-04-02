package tripDB;

import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import riverDB.River;

public class TripListTransferHandler extends TransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2911820940677754552L;
	/**
	 * 
	 */

	private TripDBPanel tripDBPanel;
	
	public TripListTransferHandler (TripDBPanel tripDBPanel) {
		this.tripDBPanel = tripDBPanel;
	}
	
	
	@Override
    public boolean canImport(TransferSupport support) {
        return ((support.getComponent() instanceof JList) && 
        		(support.isDataFlavorSupported(RosterComponent.ROSTER_COMPONENT_DATA_FLAVOR) ||
        		 support.isDataFlavorSupported(RiverComponent.RIVER_COMPONENT_DATA_FLAVOR)   ));
    }

    @Override
    public boolean importData(TransferSupport support) {
        boolean accept = false;
        if (canImport(support)) {
            try {
                Transferable t = support.getTransferable();
                if (support.isDataFlavorSupported(RosterTransferComponent.ROSTER_COMPONENT_DATA_FLAVOR)) {
	                Object value = t.getTransferData(RosterTransferComponent.ROSTER_COMPONENT_DATA_FLAVOR);
	                if (value instanceof Integer) {
	                    Component component = support.getComponent();
	                    if (component instanceof JList) {
	//                        ((JList)component).setText(((ListItem)value).getText());
	                    	tripDBPanel.dropRoster ((Integer)value, support.getDropLocation());
	                    }
	                }
                }
                else if (support.isDataFlavorSupported(RiverComponent.RIVER_COMPONENT_DATA_FLAVOR)) {
	                Object value = t.getTransferData(RiverComponent.RIVER_COMPONENT_DATA_FLAVOR);
	                if (value instanceof River) {
	                    Component component = support.getComponent();
	                    if (component instanceof JList) {
	//                        ((JList)component).setText(((ListItem)value).getText());
	                    	tripDBPanel.dropRiver ((River)value, support.getDropLocation());
	                    }
	                }
                }

            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
        return accept;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return DnDConstants.ACTION_COPY_OR_MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        Transferable t = null;
/*        if (c instanceof JList) {
            @SuppressWarnings("unchecked")
			JList<Roster> list = (JList<Roster>) c;
            Object value = list.getSelectedValue();
            if (value instanceof RosterComponent) {
                t = (Transferable) value;
            }
        } */
        return t;
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
//        System.out.println("ExportDone");
        // Here you need to decide how to handle the completion of the transfer,
        // should you remove the item from the list or not...
    }
}
