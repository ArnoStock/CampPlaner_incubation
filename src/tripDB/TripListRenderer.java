package tripDB;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

@SuppressWarnings("serial")
public class TripListRenderer extends JPanel implements ListCellRenderer<TripComponent> {

	JLabel riverNameLabel;
	JLabel tripFromToLabel;
	JLabel wwTopLevelLabel;
	JLabel rosterNamesLabel;
	
	public TripListRenderer () {
		super (new BorderLayout());

		riverNameLabel = new JLabel ();
		tripFromToLabel = new JLabel ();
		wwTopLevelLabel = new JLabel ();
		rosterNamesLabel = new JLabel ();
		JPanel topPanel = new JPanel (new BorderLayout ());
		
		tripFromToLabel.setFont(riverNameLabel.getFont().deriveFont(Font.ITALIC));
		tripFromToLabel.setPreferredSize(new Dimension (200,20));

		topPanel.add (riverNameLabel, BorderLayout.WEST);
		topPanel.add (wwTopLevelLabel, BorderLayout.EAST);
		add (topPanel, BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel (new GridLayout (2, 1));
		centerPanel.add (tripFromToLabel);
		centerPanel.add (rosterNamesLabel);
		add (centerPanel, BorderLayout.CENTER);
		
		setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

	}
	
	
	@Override
	public Component getListCellRendererComponent(JList<? extends TripComponent> list,
			TripComponent value, int index, boolean isSelected, boolean cellHasFocus) {
		
		riverNameLabel.setText("Gr " + (index+1) + ": " + value.getRiverName());
		wwTopLevelLabel.setText ("\u02AC" + value.getWwTopLevel());
		tripFromToLabel.setText ("   " + value.getFromToString());
		
		rosterNamesLabel.setForeground(value.getRosterColor());
		rosterNamesLabel.setText(value.getRosterNames());
		
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
