package gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;


@SuppressWarnings("serial")
public class FilesSetupPanel extends JPanel implements ActionListener, FocusListener {
	
	JFileChooser fc = new JFileChooser();
	JTextField tripFormFileNameLabel = new JTextField();
	JTextField dataFileFileNameLabel = new JTextField();
	JButton chooseDataFileNameButton = new JButton ("\u00c4ndern ...");
	JButton choosetripFormFileNameButton = new JButton ("\u00c4ndern ...");
	
	public FilesSetupPanel () {
		
		super (new BorderLayout());
		
		add (new JLabel ("Dateinamen und Pfade"), BorderLayout.NORTH);
		
		JPanel gp = new JPanel (new BorderLayout());
		JPanel cp = new JPanel (new GridBagLayout ());
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets (0, 4, 0, 2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		cp.add (new JLabel("Dateiname der Fahrtenplanungen"), c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy += 1;
		cp.add (dataFileFileNameLabel, c);
		dataFileFileNameLabel.setText(MainWindow.setupData.getDataFileName());
		c.fill = GridBagConstraints.NONE;
		c.gridx += 1;
		cp.add (chooseDataFileNameButton, c);
		chooseDataFileNameButton.setActionCommand("editDataFileName");
		chooseDataFileNameButton.addActionListener (this);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy += 1;
		cp.add (new JLabel("Dateiname des Ausschreibungsformulars"), c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy += 1;
		cp.add (tripFormFileNameLabel, c);
		tripFormFileNameLabel.setText(MainWindow.setupData.getFormFileName());
		c.fill = GridBagConstraints.NONE;
		c.gridx += 1;
		cp.add (choosetripFormFileNameButton, c);
		choosetripFormFileNameButton.setActionCommand("editTripFormFileName");
		choosetripFormFileNameButton.addActionListener (this);

		gp.add (cp, BorderLayout.NORTH);
		JScrollPane sp = new JScrollPane(gp);
		add (sp, BorderLayout.CENTER);
		
		tripFormFileNameLabel.addFocusListener(this);
		dataFileFileNameLabel.addFocusListener(this);
	}



	@Override
	public void actionPerformed(ActionEvent evt) {
		
		if (evt.getActionCommand().equals("editDataFileName")) {
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "JPG & GIF Images", "jpg", "gif");
			    fc.setFileFilter(filter);
			    int returnVal = fc.showOpenDialog(this);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	MainWindow.setupData.setDataFileName (fc.getSelectedFile().getAbsolutePath());
			       dataFileFileNameLabel.setText(MainWindow.setupData.getDataFileName());			    }
		}

		
		if (evt.getActionCommand().equals("editTripFormFileName")) {
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "JPG & GIF Images", "jpg", "gif");
			    fc.setFileFilter(filter);
			    int returnVal = fc.showOpenDialog(this);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	MainWindow.setupData.setFormFileName (fc.getSelectedFile().getAbsolutePath());
			       tripFormFileNameLabel.setText(MainWindow.setupData.getFormFileName());
			    }
		}
		
	}

	@Override
	public void focusLost(FocusEvent evt) {
		
		if (evt.getSource().equals(tripFormFileNameLabel)) {
			MainWindow.setupData.setFormFileName (tripFormFileNameLabel.getText());
		}
		
		if (evt.getSource().equals(dataFileFileNameLabel)) {
			MainWindow.setupData.setDataFileName (dataFileFileNameLabel.getText());
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
	}

}
