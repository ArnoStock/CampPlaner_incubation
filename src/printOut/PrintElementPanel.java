package printOut;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.JFontChooser;

@SuppressWarnings("serial")
public class PrintElementPanel extends JPanel implements ActionListener {

	private PrintElementSetup setupData;
	
	private JButton fontSetup;
	private JButton colorSetup;
	private JTextField xPos;
	private JTextField yPos;
	private JLabel parameterName;
	private JLabel example;
	private int myIndex;

	private JFontChooser fontChooser;
	
	public PrintElementPanel (PrintElementSetup setupData, String n, int i) {
		
		super (new FlowLayout (FlowLayout.LEADING));
		
		this.setupData = setupData;
		myIndex = i;
		
		parameterName = new JLabel (n);
		parameterName.setPreferredSize(new Dimension ( 150, parameterName.getPreferredSize().height ));
		fontSetup = new JButton ("Zeichen");
		fontSetup.setActionCommand("Font");
		fontSetup.addActionListener(this);
		colorSetup = new JButton ("Farbe");
		colorSetup.addActionListener(this);
		colorSetup.setActionCommand("Color");
		xPos = new JTextField ("0000");
		xPos.setPreferredSize(xPos.getPreferredSize());
		yPos = new JTextField ("0000");
		yPos.setPreferredSize(yPos.getPreferredSize());
		
		add (parameterName);
		add (fontSetup);
		add (colorSetup);
		add (xPos);
		add (yPos);
		example = new JLabel ("Beispiel"); 
		add (example);
		updateElements ();

		fontChooser = new JFontChooser ();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("Font")) {
			fontChooser.setSelectedFont(setupData.getPrintFont());
        	if (fontChooser.showDialog(null) == JFontChooser.OK_OPTION) {
        		setupData.setPrintFont(fontChooser.getSelectedFont());
        		updateElements ();
        	}
		}

		if (e.getActionCommand().equals("Color")) {
			setupData.setPrintColor(JColorChooser.showDialog(null, "Farbauswahl", setupData.getPrintColor()));
    		updateElements ();
		}
		
	}
	
	private void updateElements () {

		example.setFont(setupData.getPrintFont());
		example.setForeground(setupData.getPrintColor());
		xPos.setText("" + setupData.getAnchorPoint().x);
		yPos.setText("" + setupData.getAnchorPoint().y);
		
	}
	
	public void updateSetupData (PrintElementSetup setupData) {
		this.setupData = setupData;
		updateElements();
	}
	
	public int getIndex () {
		return myIndex;
	}
	
}
