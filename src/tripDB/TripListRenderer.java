package tripDB;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import riverDB.River;

@SuppressWarnings("serial")
public class TripListRenderer extends JPanel implements ListCellRenderer<TripComponent> {

	JLabel riverNameLabel;
	JLabel tripFromToLabel;
	JLabel wwTopLevelLabel;
	JLabel rosterNamesLabel;
	JPanel topPanel;
	JPanel centerPanel;
	
	public TripListRenderer () {
		super (new BorderLayout());

		riverNameLabel = new JLabel ();
		tripFromToLabel = new JLabel ();
		wwTopLevelLabel = new JLabel ();
		rosterNamesLabel = new JLabel ();
		topPanel = new JPanel (new BorderLayout ());
		
		Font stdFont = riverNameLabel.getFont();
		riverNameLabel.setFont(stdFont.deriveFont((float) (stdFont.getSize()*1.3)));
		tripFromToLabel.setFont(stdFont.deriveFont(Font.ITALIC + Font.BOLD));
		tripFromToLabel.setPreferredSize(new Dimension (200,20));

		topPanel.add (riverNameLabel, BorderLayout.WEST);
		topPanel.add (wwTopLevelLabel, BorderLayout.EAST);
		add (topPanel, BorderLayout.NORTH);
		
		centerPanel = new JPanel (new GridLayout (2, 1));
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
		
		if (isSelected)
			setTextColor (Color.BLUE);
		else {
			setTextColor (Color.BLACK);
		}
		rosterNamesLabel.setForeground(value.getRosterColor());
		rosterNamesLabel.setText(value.getRosterNames());
		
		if (isSelected) {
            setBackground(list.getSelectionBackground());
            centerPanel.setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            centerPanel.setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
    	topPanel.setBackground(River.getRiverColor(Integer.decode(value.getWwTopLevel())));
    	
		return this;
	}
	
	private void setTextColor (Color fg) {
		riverNameLabel.setForeground(fg);
		wwTopLevelLabel.setForeground(fg);
		tripFromToLabel.setForeground(fg);
	}


}
