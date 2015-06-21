package tripDB;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import riverDB.River;

@SuppressWarnings("serial")
public class RiverListRenderer extends JPanel implements ListCellRenderer<RiverComponent> {

	JLabel riverNameLabel;
	JLabel tripFromToLabel;
	JLabel wwTopLevelLabel;
	JPanel topPanel;

	public RiverListRenderer () {
		super (new BorderLayout ());

		riverNameLabel = new JLabel ();
		tripFromToLabel = new JLabel ();
		wwTopLevelLabel = new JLabel ();
		topPanel = new JPanel (new BorderLayout());
		Font stdFont = riverNameLabel.getFont();
		tripFromToLabel.setFont(stdFont.deriveFont(Font.ITALIC + Font.BOLD));
		riverNameLabel.setFont(stdFont.deriveFont((float) (stdFont.getSize()*1.3)));
//		tripFromToLabel.setPreferredSize(new Dimension (280,20));
		topPanel.add (riverNameLabel,BorderLayout.CENTER);
		topPanel.add (wwTopLevelLabel, BorderLayout.EAST);
		add (topPanel,BorderLayout.NORTH);
		add (tripFromToLabel,BorderLayout.CENTER);
		
		setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
	}
	
	
	@Override
	public Component getListCellRendererComponent(JList<? extends RiverComponent> list,
			RiverComponent value, int index, boolean isSelected, boolean cellHasFocus) {
		
		if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            setTextColor (Color.BLUE);
            
        } else {
            setBackground(list.getBackground());
        	setForeground(list.getForeground());
        	setTextColor(Color.BLACK);
        }
//    	setBackground(value.getRiver().getRiverColor(isSelected));
    	topPanel.setBackground(River.getRiverColor(value.getRiver().getWwTopLevel()));
		
		riverNameLabel.setText(value.getRiver().getRiverName());
		wwTopLevelLabel.setText ("\u02AC" + value.getRiver().getWwTopLevel());
		tripFromToLabel.setText ("   " + value.getRiver().getTripFrom() + " \u25BA " + value.getRiver().getTripTo());
		
		return this;
	}
	
	private void setTextColor (Color fg) {
		riverNameLabel.setForeground(fg);
		wwTopLevelLabel.setForeground(fg);
		tripFromToLabel.setForeground(fg);
	}

}
