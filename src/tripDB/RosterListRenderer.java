package tripDB;

import gui.MainWindow;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

@SuppressWarnings("serial")
public class RosterListRenderer extends JPanel implements ListCellRenderer<RosterComponent> {

	private JLabel rosterLabel;
	private Date d;
	
	public RosterListRenderer (Date d) {
		super (new FlowLayout (FlowLayout.LEADING));

		rosterLabel = new JLabel ();
		add (rosterLabel);
		this.d = d;
		setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
	}
	
	
	@Override
	public Component getListCellRendererComponent(JList<? extends RosterComponent> list,
			RosterComponent value, int index, boolean isSelected, boolean cellHasFocus) {

		Integer cnt = MainWindow.tripDB.getRosterAssignmentCount(d, value.getRoster());
		Color col = Color.BLACK;
		if (value.getRoster().getIsAspirant()) {
			col = Color.BLUE;
		}
		
		if (cnt == 1) {
			col = new Color (0, 150, 0);
		}
		else if (cnt > 1) {
			col = Color.RED;
		}
		if (value.getRoster().getIsAspirant()) {
			rosterLabel.setForeground(col);
			rosterLabel.setText("HFL: " + value.getRoster().getGivenName() + " " + value.getRoster().getFamilyName());
		}
		else {
			rosterLabel.setForeground(col);
			rosterLabel.setText(value.getRoster().getGivenName() + " " + value.getRoster().getFamilyName());
		}
		
		if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
		return this;
	}


}
