package tripDB;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

@SuppressWarnings("serial")
public class RiverListRenderer extends JPanel implements ListCellRenderer<RiverComponent> {

	JLabel riverNameLabel;
	JLabel tripFromToLabel;
	JLabel wwTopLevelLabel;

	public RiverListRenderer () {
		super (new BorderLayout ());

		riverNameLabel = new JLabel ();
		tripFromToLabel = new JLabel ();
		wwTopLevelLabel = new JLabel ();
		JPanel topPanel = new JPanel (new BorderLayout());
		tripFromToLabel.setFont(riverNameLabel.getFont().deriveFont(Font.ITALIC));
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
		
		
		riverNameLabel.setText(value.getRiver().getRiverName());
		wwTopLevelLabel.setText ("\u02AC" + value.getRiver().getWwTopLevel());
		tripFromToLabel.setText ("   " + value.getRiver().getTripFrom() + " \u25BA " + value.getRiver().getTripTo());
		
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
