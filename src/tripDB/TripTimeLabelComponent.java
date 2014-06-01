package tripDB;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import riverDB.River;

@SuppressWarnings("serial")
public class TripTimeLabelComponent extends JPanel {

	public TripTimeLabelComponent (Trip trip) {
		super (new FlowLayout (FlowLayout.LEADING));
		
		JPanel labelPanel = new JPanel (new GridLayout (2,1));
		
		River r = trip.getRiver();
		String riverName;
		if (r == null) {
			riverName = "<?>";
		}
		else {
			riverName = r.getRiverName();
		}
		addLabelWithBorder (labelPanel, "Gruppe " + trip.getGroupNumber());
		addLabelWithBorder (labelPanel, riverName);

		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		this.setBorder(raisedbevel);
		add (labelPanel, BorderLayout.CENTER);
	}
	
	private void addLabelWithBorder (JPanel p, String s) {

		Border empty = BorderFactory.createEmptyBorder(2,2,2,3);
		JLabel l = new JLabel (s);
		l.setBorder(empty);
		p.add (l);

	}
	
}
